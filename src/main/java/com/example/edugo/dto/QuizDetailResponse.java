package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDetailResponse {
    private Long id;
    private String titre;
    private String description;
    private Long matiereId;
    private String matiereNom;
    private Long niveauId;
    private String niveauNom;
    private List<QuestionResponse> questions;
    private int nombreQuestions;
    // Ajoute ici stats ou métadonnées spécifiques au quiz selon besoins.
}
