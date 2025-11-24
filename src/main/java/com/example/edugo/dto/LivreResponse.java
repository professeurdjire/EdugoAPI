package com.example.edugo.dto;

import lombok.Data;

@Data
public class LivreResponse {
    private Long id;
    private String titre;
    private String isbn;
    private String auteur;
    private String imageCouverture;
    private Integer totalPages;

    // Relations pour le front
    private Long matiereId;
    private String matiereNom;
    private Long niveauId;
    private String niveauNom;
    private Long classeId;
    private String classeNom;
    private Long langueId;
    private String langueNom;

    // Quiz lié au livre (OneToOne)
    private Long quizId;
}
