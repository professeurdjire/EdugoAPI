package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
