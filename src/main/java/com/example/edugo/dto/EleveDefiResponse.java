package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EleveDefiResponse {
    private Long id;
    private Long eleveId;
    private String nom;
    private String prenom;
    private Long defiId;
    private String defiTitre;
    private LocalDateTime dateEnvoie;
    private String statut;
}
