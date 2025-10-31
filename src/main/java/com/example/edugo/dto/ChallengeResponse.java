package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChallengeResponse {
    private Long id;
    private String titre;
    private String description;
    private Integer points;
    private String theme;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
