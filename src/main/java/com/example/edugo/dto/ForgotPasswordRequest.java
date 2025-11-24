package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Demande de réinitialisation de mot de passe")
public class ForgotPasswordRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "Email de l'utilisateur (élève ou administrateur)", 
            example = "user@example.com", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}

