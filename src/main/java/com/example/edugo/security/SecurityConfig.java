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
                        
                        // Endpoints d'authentification publics (y compris réinitialisation de mot de passe)
                        // Exclure /auth/me et /api/auth/me qui nécessitent une authentification
                        .requestMatchers("/auth/login", "/api/auth/login").permitAll()
                        .requestMatchers("/auth/register", "/api/auth/register").permitAll()
                        .requestMatchers("/auth/refresh", "/api/auth/refresh").permitAll()
                        .requestMatchers("/auth/logout", "/api/auth/logout").permitAll()
                        .requestMatchers("/auth/forgot-password", "/api/auth/forgot-password").permitAll()
                        .requestMatchers("/auth/reset-password", "/api/auth/reset-password").permitAll()
                        .requestMatchers("/auth/reset-password/verify", "/api/auth/reset-password/verify").permitAll()
                        .requestMatchers("/reset-password", "/api/reset-password").permitAll()
                        .requestMatchers("/public/**", "/api/public/**").permitAll()
                        
                        // ========== ENDPOINTS PUBLICS (ORIGINAUX) ==========
                        // Seuls les niveaux et classes étaient publics à l'origine
                        .requestMatchers(HttpMethod.GET, "/niveaux/**", "/api/niveaux/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/classes/**", "/api/classes/**").permitAll()




                        // Admin endpoints - require ADMIN role
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        
                        // ========== ENDPOINTS DE SOUMISSION (ELEVE) ==========
                        // IMPORTANT: Ces règles DOIVENT être AVANT la règle générale /eleve/**
                        // L'ordre est critique : Spring Security évalue les règles dans l'ordre et la première correspondance gagne
                        // Si on met /eleve/** avant, il va matcher tous les endpoints /eleve/... et bloquer les soumissions
                        
                        // Soumissions via EvaluationController (QCU/QCM/VRAI_FAUX)
                        // Utiliser /*/submit au lieu de /**/submit car PathPattern ne supporte pas ** au milieu
                        .requestMatchers(HttpMethod.POST, "/quizzes/*/submit", "/api/quizzes/*/submit").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/challenges/*/submit", "/api/challenges/*/submit").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/exercices/*/submit", "/api/exercices/*/submit").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/defis/*/submit", "/api/defis/*/submit").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Soumissions via ExerciceController (texte libre)
                        .requestMatchers(HttpMethod.POST, "/exercices/soumettre/**", "/api/exercices/soumettre/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Participations via DefiController et ChallengeController
                        .requestMatchers(HttpMethod.POST, "/defis/participer/**", "/api/defis/participer/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/challenges/participer/**", "/api/challenges/participer/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Soumissions via EleveController (DOIT être avant la règle générale /eleve/**)
                        .requestMatchers(HttpMethod.POST, "/eleve/exercices/soumettre/**", "/api/eleve/exercices/soumettre/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/eleve/defis/participer/**", "/api/eleve/defis/participer/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/eleve/challenges/participer/**", "/api/eleve/challenges/participer/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Progression de lecture - accessible aux ELEVE (DOIT être avant la règle générale /livres/**)
                        .requestMatchers(HttpMethod.POST, "/livres/progression/**", "/api/livres/progression/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/livres/progression/**", "/api/livres/progression/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Eleve endpoints - require ELEVE or ADMIN role (APRÈS les règles de soumission)
                        .requestMatchers("/eleve/**", "/api/eleve/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers("/objectifs/**", "/api/objectifs/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers("/suggestions/**", "/api/suggestions/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Conversion endpoints - accessible to authenticated users
                        .requestMatchers(HttpMethod.GET, "/conversions/**", "/api/conversions/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/conversions/**", "/api/conversions/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // Endpoints nécessitant une authentification (GET operations)
                        .requestMatchers("/auth/me", "/api/auth/me").authenticated() // Profil utilisateur connecté
                        .requestMatchers(HttpMethod.GET, "/users/**", "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/matieres/**", "/api/matieres/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/livres/**", "/api/livres/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/exercices/**", "/api/exercices/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/defis/**", "/api/defis/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/challenges/**", "/api/challenges/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/quizzes/**", "/api/quizzes/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/badges/**", "/api/badges/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/eleve/**", "/api/eleve/**").hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/conversions/**", "/api/conversions/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/partenaires/**", "/api/partenaires/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/statistiques/**", "/api/statistiques/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/notifications/**", "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/notifications/**", "/api/notifications/**").hasAnyRole("ELEVE", "ADMIN")
                        
                        // General data endpoints - POST/PUT/DELETE require ADMIN role
                        // IMPORTANT: Les règles de soumission sont définies AVANT, donc elles sont évaluées en premier
                        .requestMatchers(HttpMethod.POST, "/classes/**", "/matieres/**", "/niveaux/**", "/users/**",
                                        "/livres/**", "/badges/**",
                                        "/api/classes/**", "/api/matieres/**", "/api/niveaux/**", "/api/users/**",
                                        "/api/livres/**", "/api/badges/**").hasRole("ADMIN")
                        // Pour exercices, defis, challenges, quizzes - seuls les POST de création/correction nécessitent ADMIN
                        // ATTENTION: Ne pas utiliser /** car cela bloquerait les soumissions même si elles sont définies avant
                        // Les soumissions sont gérées ci-dessus avec hasAnyRole("ELEVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/exercices", "/api/exercices").hasRole("ADMIN") // Créer exercice (exact match)
                        .requestMatchers(HttpMethod.POST, "/exercices/corriger/**", "/api/exercices/corriger/**").hasRole("ADMIN") // Corriger exercice
                        .requestMatchers(HttpMethod.POST, "/defis", "/api/defis").hasRole("ADMIN") // Créer défi (exact match)
                        .requestMatchers(HttpMethod.POST, "/challenges", "/api/challenges").hasRole("ADMIN") // Créer challenge (exact match)
                        .requestMatchers(HttpMethod.POST, "/quizzes", "/api/quizzes").hasRole("ADMIN") // Créer quiz (exact match)
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