package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ObjectifResponse {
    private Long id;
    private String nom;
    private String description;
    private String cible;
    private String typeObjectif;
    private Integer valeur;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
