package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse pour les opérations de réinitialisation de mot de passe")
public class PasswordResetResponse {

    @Schema(description = "Message de confirmation", example = "Email de réinitialisation envoyé avec succès")
    private String message;

    @Schema(description = "Indique si l'opération a réussi", example = "true")
    private Boolean success;

    @Schema(description = "Email concerné (si applicable)", example = "user@example.com")
    private String email;
}

