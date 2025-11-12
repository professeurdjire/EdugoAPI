package com.example.edugo.dto;

import lombok.Data;

@Data
public class LivreDetailResponse {
    private Long id;
    private String titre;
    private String isbn;
    private String auteur;
    private String imageCouverture;
    private Integer totalPages;
    private Boolean lectureAuto;
    private Boolean interactif;
    private Integer anneePublication;
    private String editeur;
    private Long niveauId;
    private String niveauNom;
    private Long classeId;
    private String classeNom;
    private Long matiereId;
    private String matiereNom;
    private Long langueId;
    private String langueNom;
    private Double progression;
    private Object statistiques;
    // À compléter suivant besoins métier plus précis
}
