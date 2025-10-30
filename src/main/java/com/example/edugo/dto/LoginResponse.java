package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Réponse de connexion contenant les tokens JWT et les informations utilisateur")
public class LoginResponse {
    @Schema(description = "Token JWT d'accès", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Token JWT de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    @Schema(description = "Email de l'utilisateur", example = "admin@edugo.com")
    private String email;
    
    @Schema(description = "Nom de famille", example = "Admin")
    private String nom;
    
    @Schema(description = "Prénom", example = "Principal")
    private String prenom;
    
    @Schema(description = "Rôle de l'utilisateur", example = "ADMIN", allowableValues = {"ADMIN", "ELEVE"})
    private String role;
    
    @Schema(description = "ID unique de l'utilisateur", example = "1")
    private Long id;
}
