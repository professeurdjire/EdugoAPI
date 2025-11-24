package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Vérification d'un token de réinitialisation")
public class VerifyTokenRequest {

    @NotBlank(message = "Le token est obligatoire")
    @Schema(description = "Token de réinitialisation à vérifier", 
            example = "abc123def456...", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}

