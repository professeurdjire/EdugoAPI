package com.example.edugo.controller;


import com.example.edugo.dto.*;
import com.example.edugo.service.AuthService;
import com.example.edugo.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/auth"})
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des utilisateurs")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

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

    @Operation(
            summary = "Demander la réinitialisation de mot de passe",
            description = "Envoie un email avec un lien de réinitialisation de mot de passe à l'utilisateur (élève ou administrateur). " +
                         "Fonctionne pour tous les types d'utilisateurs.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email de réinitialisation envoyé (ou message générique si l'email n'existe pas)", 
                    content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur de validation de l'email")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        PasswordResetResponse response = passwordResetService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier un token de réinitialisation",
            description = "Vérifie si un token de réinitialisation est valide avant de permettre la réinitialisation du mot de passe.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token valide", 
                    content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Token invalide ou expiré")
    })
    @PostMapping("/reset-password/verify")
    public ResponseEntity<PasswordResetResponse> verifyToken(@Valid @RequestBody VerifyTokenRequest request) {
        PasswordResetResponse response = passwordResetService.verifyToken(request.getToken());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier un token de réinitialisation (GET)",
            description = "Vérifie si un token de réinitialisation est valide. Utilisé par le frontend pour vérifier le token dans l'URL.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token valide ou invalide", 
                    content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Token invalide ou expiré")
    })
    @GetMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> verifyResetToken(@RequestParam("token") String token) {
        try {
            PasswordResetResponse response = passwordResetService.verifyToken(token);
            return ResponseEntity.ok(response);
        } catch (com.example.edugo.exception.ResourceNotFoundException e) {
            return ResponseEntity.ok(PasswordResetResponse.builder()
                    .message("Token de réinitialisation invalide ou expiré")
                    .success(false)
                    .build());
        }
    }

    @Operation(
            summary = "Réinitialiser le mot de passe",
            description = "Réinitialise le mot de passe de l'utilisateur en utilisant un token valide. " +
                         "Fonctionne pour les élèves et les administrateurs.",
            tags = {"Authentification"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé avec succès", 
                    content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur de validation (mots de passe ne correspondent pas, token invalide, etc.)"),
            @ApiResponse(responseCode = "404", description = "Token invalide ou expiré")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // Vérifier que les mots de passe correspondent
        if (!request.getNouveauMotDePasse().equals(request.getConfirmationMotDePasse())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }

        PasswordResetResponse response = passwordResetService.resetPassword(
                request.getToken(), 
                request.getNouveauMotDePasse()
        );
        return ResponseEntity.ok(response);
    }


}
