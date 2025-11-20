package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChallengeRequest {
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer points;
    private String theme;
    private String typeChallenge; // INTERCLASSE ou INTERNIVEAU
    private Long niveauId;        // requis pour INTERNIVEAU
    private Long classeId;        // requis pour INTERCLASSE
}
