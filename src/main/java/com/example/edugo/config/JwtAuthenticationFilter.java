package com.example.edugo.config;

import com.example.edugo.service.CustomUserDetailsService;
import com.example.edugo.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        System.out.println("===== JWT FILTER START =====");
        System.out.println("Requête : " + request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("JWT reçu : " + token);

            try {
                username = jwtUtil.extractUsername(token);
                System.out.println("Username extrait : " + username);
            } catch (Exception e) {
                System.out.println("Erreur extraction JWT : " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
                return;
            }
        } else {
            System.out.println("Pas de header Authorization ou ne commence pas par Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails trouvé : " + userDetails.getUsername());
            System.out.println("Roles UserDetails : " + userDetails.getAuthorities());

            if (jwtUtil.validateToken(token, userDetails)) {
                List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token)
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                System.out.println("Authorities appliquées : " + authorities);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication appliquée !");
            } else {
                System.out.println("JWT invalide pour l'utilisateur");
            }
        }

        System.out.println("===== JWT FILTER END =====");
        filterChain.doFilter(request, response);
    }
}
