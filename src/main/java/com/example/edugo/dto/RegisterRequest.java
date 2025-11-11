package com.example.edugo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Demande d'inscription d'un nouvel élève")
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Nom de famille", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Schema(description = "Prénom", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "Email de l'élève", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(description = "Mot de passe", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String motDePasse;
    
    @Schema(description = "ID de la classe de l'élève", example = "1")
    private Long classeId;

    @Schema(description = "ID du niveau de l'élève", example = "1")
    private Long niveauId;
    
    @Schema(description = "ville de l'elève", example = "Bamako")
    private String ville;
    @Schema(description = "numero de telephone de l'elève", example = "97113882")
    private Integer telephone;

    @Schema(description = "lien du photo profil de l'elève", example = "https://monserveur.com/uploads/profils/avatar1.jpg")
    private String photoProfil;
}