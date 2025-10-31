package com.example.edugo.dto;

import lombok.Data;

@Data
public class ExerciceDetailResponse {
    private Long id;
    private String titre;
    private String description;
    private Integer niveauDifficulte;
    private Integer tempsAlloue;
    private Boolean active;
    private Long matiereId;
    private String matiereNom;
    private Long niveauId;
    private String niveauNom;
    private Long livreId;
    private String livreTitre;
}
