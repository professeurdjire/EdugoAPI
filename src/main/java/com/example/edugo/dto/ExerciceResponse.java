package com.example.edugo.dto;

import lombok.Data;

@Data
public class ExerciceResponse {
    private Long id;
    private String titre;
    private Boolean active;
    private Integer niveauDifficulte;
    private Integer tempsAlloue;
    private Long matiereId;
    private String matiereNom;
}
