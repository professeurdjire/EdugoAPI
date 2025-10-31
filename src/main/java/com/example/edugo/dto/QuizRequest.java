package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizRequest {
    private String titre;
    private String description;
    private Long matiereId;
    private Long niveauId;
    private List<Long> questionsIds;
}
