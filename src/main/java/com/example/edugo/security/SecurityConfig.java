package com.example.edugo.security;

import com.example.edugo.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Security configuration for EDUGO API
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Check if we're in development mode
        String profile = System.getProperty("spring.profiles.active", "");
        boolean isDevMode = profile.contains("dev") || profile.contains("development");
        
        // Also check environment variable
        String devModeEnv = System.getenv("DEV_MODE");
        boolean isDevModeEnv = "true".equalsIgnoreCase(devModeEnv);
        
        if (isDevMode || isDevModeEnv) {
            // Development mode - bypass authentication
            http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()  // Allow all requests in dev mode
                );
        } else {
            // Production mode - normal security
            http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI documentation - ACCÈS PUBLIC
                        // Note: Avec context-path=/api, Spring Security voit les chemins SANS le préfixe /api
                        // Mais si le frontend appelle /api/api/..., Spring Security voit /api/...
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                        "/swagger-ui/**", 
                                        "/swagger-ui.html", 
                                        "/swagger-resources/**",
                                        "/webjars/**", 
                                        "/api-docs/**",
                                        "/api/v3/api-docs/**",
                                        "/api/swagger-ui/**",
                                        "/api/swagger-ui.html",
                                        "/api/swagger-resources/**",
                                        "/api/webjars/**",
                                        "/api/api-docs/**",
                                        "/",
                                        "/docs",
                                        "/swagger",
                                        "/api/docs",
                                        "/api/swagger").permitAll()
                        
                        // Endpoints d'authentification publics
                        .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                        .requestMatchers("/public/**", "/api/public/**").permitAll()
                        
                        // Admin endpoints - require ADMIN role
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        
                        // Eleve endpoints - require ELEVE or ADMIN role
                        .requestMatchers("/eleve/**", "/api/eleve/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Conversion endpoints - accessible to authenticated users
                        .requestMatchers(HttpMethod.GET, "/conversions/**", "/api/conversions/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/conversions/**", "/api/conversions/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // General data endpoints - accessible to authenticated users (GET operations)
                        // Support both /matieres/** (si frontend appelle /api/matieres) et /api/matieres/** (si frontend appelle /api/api/matieres)
                        .requestMatchers(HttpMethod.GET, "/classes/**", "/matieres/**", "/niveaux/**", "/users/**", 
                                        "/livres/**", "/exercices/**", "/defis/**", "/challenges/**", 
                                        "/badges/**", "/quizzes/**",
                                        "/api/classes/**", "/api/matieres/**", "/api/niveaux/**", "/api/users/**", 
                                        "/api/livres/**", "/api/exercices/**", "/api/defis/**", "/api/challenges/**", 
                                        "/api/badges/**", "/api/quizzes/**").authenticated()
                        
                        // General data endpoints - POST/PUT/DELETE require ADMIN role
                        .requestMatchers(HttpMethod.POST, "/classes/**", "/matieres/**", "/niveaux/**", "/users/**",
                                        "/livres/**", "/exercices/**", "/defis/**", "/challenges/**",
                                        "/badges/**", "/quizzes/**",
                                        "/api/classes/**", "/api/matieres/**", "/api/niveaux/**", "/api/users/**",
                                        "/api/livres/**", "/api/exercices/**", "/api/defis/**", "/api/challenges/**",
                                        "/api/badges/**", "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/classes/**", "/matieres/**", "/niveaux/**", "/users/**",
                                        "/livres/**", "/exercices/**", "/defis/**", "/challenges/**",
                                        "/badges/**", "/quizzes/**",
                                        "/api/classes/**", "/api/matieres/**", "/api/niveaux/**", "/api/users/**",
                                        "/api/livres/**", "/api/exercices/**", "/api/defis/**", "/api/challenges/**",
                                        "/api/badges/**", "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/classes/**", "/matieres/**", "/niveaux/**", "/users/**",
                                        "/livres/**", "/exercices/**", "/defis/**", "/challenges/**",
                                        "/badges/**", "/quizzes/**",
                                        "/api/classes/**", "/api/matieres/**", "/api/niveaux/**", "/api/users/**",
                                        "/api/livres/**", "/api/exercices/**", "/api/defis/**", "/api/challenges/**",
                                        "/api/badges/**", "/api/quizzes/**").hasRole("ADMIN")
                        
                        // Other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"" + accessDeniedException.getMessage() + "\"}");
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Provide AuthenticationManager bean so services that expect it can autowire it
    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    // Password encoder needed by services (e.g. to encode/verify passwords)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(org.springframework.security.crypto.password.PasswordEncoder.class)
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}