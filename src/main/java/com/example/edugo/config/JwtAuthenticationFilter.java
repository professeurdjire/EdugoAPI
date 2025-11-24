package com.example.edugo.config;

import com.example.edugo.service.CustomUserDetailsService;
import com.example.edugo.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //  Liste des endpoints publics qui ne nécessitent pas de JWT
    // Seuls les endpoints d'authentification, niveaux, classes et documentation sont publics
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/",
            "/api/auth/",
            "/reset-password",
            "/api/reset-password",
            "/public/",
            "/api/public/",
            // Structures scolaires (seulement niveaux et classes étaient publics à l'origine)
            "/api/niveaux/",
            "/api/classes/",
            // Documentation
            "/swagger-ui/",
            "/v3/api-docs/"
    };
    
    /**
     * Vérifie si un endpoint est public (ne nécessite pas d'authentification)
     */
    private boolean isPublicEndpoint(String requestPath) {
        // Normaliser le chemin (gérer le double /api/api/)
        String normalizedPath = requestPath.replace("/api/api/", "/api/");
        
        // Exclure /auth/me et /api/auth/me qui nécessitent une authentification
        if (normalizedPath.equals("/auth/me") || normalizedPath.equals("/api/auth/me")) {
            return false;
        }
        
        for (String publicEndpoint : PUBLIC_ENDPOINTS) {
            if (normalizedPath.startsWith(publicEndpoint)) {
                return true;
            }
        }
        return false;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Check if we're in development mode
        String profile = System.getProperty("spring.profiles.active", "");
        boolean isDevMode = profile.contains("dev") || profile.contains("development");
        
        // Also check environment variable
        String devModeEnv = System.getenv("DEV_MODE");
        boolean isDevModeEnv = "true".equalsIgnoreCase(devModeEnv);
        
        // If in development mode, bypass authentication completely
        if (isDevMode || isDevModeEnv) {
            logger.debug("Development mode active - bypassing authentication for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String requestPath = request.getRequestURI();
        
        // Normaliser le chemin pour gérer le double /api/api/ et enlever les query parameters
        String normalizedPath = requestPath.replace("/api/api/", "/api/");
        // Enlever les query parameters pour la vérification
        if (normalizedPath.contains("?")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.indexOf("?"));
        }

        // Si c'est un endpoint public, passer sans authentification
        // Ne pas essayer de valider un token JWT même s'il est présent
        if (isPublicEndpoint(normalizedPath)) {
            logger.debug("Endpoint public détecté: {}, passage sans authentification", normalizedPath);
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // Try to read JWT from HttpOnly cookie 'access_token'
            if (request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie c : request.getCookies()) {
                    if ("access_token".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                        jwt = c.getValue();
                        break;
                    }
                }
            }
            if (jwt == null) {
                logger.debug("Aucun token JWT pour la requête: {} (ni header, ni cookie)", normalizedPath);
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            logger.debug("Token JWT présent pour la requête: {} (source: {} )", normalizedPath, 
                    (authHeader != null && authHeader.startsWith("Bearer ")) ? "Authorization" : "Cookie access_token");
            
            final String userEmail = jwtUtil.extractUsername(jwt);
            logger.debug("Email extrait du token: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.debug("UserDetails chargé: {} avec autorités: {}", userEmail, userDetails.getAuthorities());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Authentification configurée pour l'utilisateur: {} avec rôles: {}", 
                            userEmail, userDetails.getAuthorities());
                } else {
                    logger.warn("Token JWT invalide pour l'utilisateur: {}", userEmail);
                }
            } else if (userEmail == null) {
                logger.warn("Impossible d'extraire l'email du token JWT");
            } else {
                logger.debug("Authentification déjà présente dans le contexte pour: {}", userEmail);
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error("Token JWT expiré pour la requête: {}", normalizedPath, e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("Signature JWT invalide pour la requête: {}", normalizedPath, e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.error("Token JWT malformé pour la requête: {}", normalizedPath, e);
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            // Ne logger qu'en DEBUG pour les endpoints publics, ERROR pour les autres
            if (isPublicEndpoint(normalizedPath)) {
                logger.debug("Utilisateur non trouvé pour endpoint public: {} (normal, pas d'erreur)", normalizedPath);
            } else {
                logger.error("Utilisateur non trouvé pour la requête: {}", normalizedPath, e);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du token JWT pour la requête: {}", normalizedPath, e);
        }
        
        filterChain.doFilter(request, response);
    }
}