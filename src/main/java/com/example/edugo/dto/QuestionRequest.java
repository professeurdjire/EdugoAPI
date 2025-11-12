package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionRequest {
    // Exactly one of these should be provided
    private Long quizId;
    private Long exerciceId;
    private Long challengeId;
    private Long defiId;

    private String enonce;
    private Integer points;
    // QCU | QCM | VRAI_FAUX
    private String type;
    // For QCU/QCM/VRAI_FAUX
    private List<ReponsePossibleRequest> reponses;
}
