package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecompenseResponse {
    private Long id;
    private String nom;
    private String description;
    private String type;
    private Long utilisateurId;
    private String utilisateurNom;
    private LocalDateTime dateAttribution;
}
