package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParticipationRequest {
    private Long utilisateurId;
    private Long activiteId;
    private String typeActivite;
    private String statut;
    private LocalDateTime dateParticipation;
}
