package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParticipationResponse {
    private Long id;
    private Long utilisateurId;
    private String utilisateurNom;
    private Long activiteId;
    private String typeActivite;
    private String statut;
    private LocalDateTime dateParticipation;
}
