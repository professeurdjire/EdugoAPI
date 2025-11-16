package com.example.edugo.dto;

import lombok.Data;

@Data
public class QuizResponse {
    private Long id;
    private String titre;
    private String description;
    private String auteur;
    private String titreLivre;
    private Long matiereId;
    private String matiereNom;
    private Long niveauId;
    private String niveauNom;
    private int nombreQuestions;
}
