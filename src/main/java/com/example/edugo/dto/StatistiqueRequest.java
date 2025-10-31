package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatistiqueRequest {
    private String type;
    private Double valeur;
    private Long utilisateurId;
    private LocalDateTime date;
}
