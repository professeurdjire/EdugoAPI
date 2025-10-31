package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecompenseDetailResponse {
    private Long id;
    private String nom;
    private String description;
    private String type;
    private Long utilisateurId;
    private String utilisateurNom;
    private LocalDateTime dateAttribution;
    private String contexte;
    // Ajoutez ici des liens, statistiques ou l'activité/defi/badge associé si pertinent.
}
