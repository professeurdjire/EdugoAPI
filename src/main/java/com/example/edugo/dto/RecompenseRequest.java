package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecompenseRequest {
    private String nom;
    private String description;
    private String type;
    private Long utilisateurId;
    private LocalDateTime dateAttribution;
    // Ajoutez ici si besoin : défiId, badgeId, etc.
}
