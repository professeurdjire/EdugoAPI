package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesMatiereResponse {
    @JsonProperty("matiereId")
    private Long matiereId;
    
    @JsonProperty("nomMatiere")
    private String nomMatiere;
    
    @JsonProperty("nombreEleves")
    private Integer nombreEleves;
    
    @JsonProperty("nombreLivres")
    private Integer nombreLivres;
    
    @JsonProperty("nombreExercices")
    private Integer nombreExercices;
    
    @JsonProperty("nombreExercicesActifs")
    private Integer nombreExercicesActifs;
}

