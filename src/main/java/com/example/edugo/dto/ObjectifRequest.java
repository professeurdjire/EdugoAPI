package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ObjectifRequest {
    private String nom;
    private String description;
    private String cible;
    private String typeObjectif;
    private Integer valeur;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
