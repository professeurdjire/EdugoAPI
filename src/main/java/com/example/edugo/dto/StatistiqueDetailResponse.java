package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatistiqueDetailResponse {
    private Long id;
    private String type;
    private Double valeur;
    private Long utilisateurId;
    private String utilisateurNom;
    private LocalDateTime date;
    private String contexte;
}
