package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Demande de rafraîchissement du token JWT")
public class RefreshTokenRequest {
    @NotBlank(message = "Le refresh token est obligatoire")
    @Schema(description = "Token JWT de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}

