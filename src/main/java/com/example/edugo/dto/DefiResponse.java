package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefiResponse {
    private Long id;
    private String titre;
    private Integer pointDefi;
    private LocalDateTime dateAjout;
    private int nbreParticipations;
    private Long classeId;
    private String classeNom;
    private String typeDefi;
    private Integer nombreQuestions; // Nombre de questions associées au défi
}
