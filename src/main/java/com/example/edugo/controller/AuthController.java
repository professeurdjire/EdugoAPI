package com.example.edugo.controller;


import com.example.edugo.dto.*;
import com.example.edugo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/auth"})
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des utilisateurs")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Inscription d'un nouvel élève",
            description = "Permet à un nouvel élève de s'inscrire sur la plateforme. L'élève doit fournir ses informations personnelles.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscription réussie", 
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur de validation"),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("Ville reçue: " + request.getVille());
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Connexion à la plateforme",
            description = "Authentifie un utilisateur (Admin ou Élève) et retourne un token JWT avec un refresh token.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie", 
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "403", description = "Compte désactivé")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request);
        ResponseCookie accessCookie = ResponseCookie.from("access_token", resp.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", resp.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(resp);
    }

    @Operation(
            summary = "Rafraîchir le token JWT",
            description = "Permet d'obtenir un nouveau token JWT en utilisant un refresh token valide. " +
                         "Le nouveau token a une nouvelle date d'expiration.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token rafraîchi avec succès", 
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token invalide ou expiré")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse resp = authService.refreshToken(request.getRefreshToken());
        ResponseCookie accessCookie = ResponseCookie.from("access_token", resp.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", resp.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(resp);
    }

    @Operation(
            summary = "Déconnexion",
            description = "Déconnecte l'utilisateur. Pour un système JWT stateless, le logout est géré côté client " +
                         "en supprimant le token du stockage local.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Pour JWT stateless, le logout est géré côté client
        return ResponseEntity.ok("Déconnexion réussie");
    }

    //Recuperer l'elève courrent connecter

    @Operation(
            summary = "Récupérer l'élève connecté",
            description = "Retourne les informations de l'élève actuellement authentifié",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Élève récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "L'utilisateur n'est pas un élève")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentEleve() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        EleveProfileResponse eleve = authService.getCurrentEleve(email);
        return ResponseEntity.ok(eleve);
    }


}
