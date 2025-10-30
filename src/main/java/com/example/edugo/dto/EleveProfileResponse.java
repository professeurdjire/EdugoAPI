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
    private String dateNaissance;
    private Long classeId;
    private String classeNom;
    private Integer points;
}


