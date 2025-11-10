package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OptionsConversionResponse {
    private Long id;
    private String libelle;
    private Boolean etat;
    private Integer nbrePoint;
    private LocalDateTime dateAjout;
}

