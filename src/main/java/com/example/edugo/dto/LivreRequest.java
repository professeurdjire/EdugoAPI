package com.example.edugo.dto;

import lombok.Data;

@Data
public class LivreRequest {
    private String titre;
    private String isbn;
    private String description;
    private Integer anneePublication;
    private String editeur;
    private String auteur;
    private Integer totalPages;
    private String imageCouverture;
    private Boolean lectureAuto;
    private Boolean interactif;
    private Long niveauId;
    private Long classeId;
    private Long matiereId;
    private Long langueId;
}
