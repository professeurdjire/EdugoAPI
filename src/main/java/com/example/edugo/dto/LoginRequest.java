package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Demande de connexion")
public class LoginRequest {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "Email de l'utilisateur", example = "admin@edugo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(description = "Mot de passe", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String motDePasse;
}