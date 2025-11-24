package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Demande de réinitialisation de mot de passe avec token")
public class ResetPasswordRequest {

    @NotBlank(message = "Le token est obligatoire")
    @Schema(description = "Token de réinitialisation reçu par email", 
            example = "abc123def456...", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Schema(description = "Nouveau mot de passe", 
            example = "nouveauMotDePasse123", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nouveauMotDePasse;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    @Schema(description = "Confirmation du nouveau mot de passe", 
            example = "nouveauMotDePasse123", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmationMotDePasse;
}

