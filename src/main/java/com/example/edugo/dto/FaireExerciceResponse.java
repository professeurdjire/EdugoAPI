package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FaireExerciceResponse {
    private Long id;
    private Long eleveId;
    private String eleveNom;
    private Long exerciceId;
    private String exerciceTitre;
    private String reponse;
    private Integer note;
    private String commentaire;
    private String statut;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateCorrection;
}
