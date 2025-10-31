package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ObjectifDetailResponse {
    private Long id;
    private String nom;
    private String description;
    private String cible;
    private String typeObjectif;
    private Integer valeur;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private List<EleveLiteResponse> participants;
    private Integer progression;
    // Ajoutez stats, métadonnées, etc. selon le besoin.
}
