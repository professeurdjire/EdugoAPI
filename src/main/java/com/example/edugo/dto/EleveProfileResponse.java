package com.example.edugo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EleveProfileResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String photoProfil;
    private String ville;
    private Long classeId;
    private String classeNom;
    private Long niveauId;
    private String niveauNom;
    private String photoUrl;
    private Integer points;
}


