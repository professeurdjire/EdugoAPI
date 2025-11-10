package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversionResponse {
    private Long id;
    private String libelleOption;
    private Integer pointsUtilises;
    private LocalDateTime dateConversion;
    private Integer pointsRestants;
}

