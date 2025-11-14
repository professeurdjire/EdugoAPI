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
                logger.debug("Aucun token JWT pour la requête: {} (ni header, ni cookie)", requestPath);
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            logger.debug("Token JWT présent pour la requête: {} (source: {} )", requestPath, 
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
            logger.error("Token JWT expiré pour la requête: {}", requestPath, e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("Signature JWT invalide pour la requête: {}", requestPath, e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.error("Token JWT malformé pour la requête: {}", requestPath, e);
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            logger.error("Utilisateur non trouvé pour la requête: {}", requestPath, e);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du token JWT pour la requête: {}", requestPath, e);
        }
        
        filterChain.doFilter(request, response);
    }
}