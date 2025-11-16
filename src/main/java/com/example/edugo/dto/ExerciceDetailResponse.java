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
    private java.time.LocalDateTime dateCreation;
    private java.time.LocalDateTime dateModification;
    private Integer nbQuestions;
    private String fichierUrl;
    private String imageUrl;
}
