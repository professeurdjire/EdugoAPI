package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChallengeDetailResponse {
    private Long id;
    private String titre;
    private String description;
    private Integer points;
    private String theme;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private List<EleveLiteResponse> participants;
    // Ajoutez ici stats, états, taux de réussite, etc. selon besoin.
}
