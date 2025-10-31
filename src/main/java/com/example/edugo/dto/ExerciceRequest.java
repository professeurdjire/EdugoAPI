package com.example.edugo.dto;

import lombok.Data;

@Data
public class ExerciceRequest {
    private String titre;
    private String description;
    private Integer niveauDifficulte;
    private Integer tempsAlloue;
    private Boolean active;
    private Long matiereId;
    private Long niveauId;
    private Long livreId;
}
