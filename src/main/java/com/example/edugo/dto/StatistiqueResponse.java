package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatistiqueResponse {
    private Long id;
    private String type;
    private Double valeur;
    private Long utilisateurId;
    private String utilisateurNom;
    private LocalDateTime date;
}
