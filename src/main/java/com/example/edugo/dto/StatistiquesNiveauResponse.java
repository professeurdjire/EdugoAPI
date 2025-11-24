package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesNiveauResponse {
    @JsonProperty("niveauId")
    private Long niveauId;
    
    @JsonProperty("nomNiveau")
    private String nomNiveau;
    
    @JsonProperty("nombreClasses")
    private Integer nombreClasses;
    
    @JsonProperty("nombreEleves")
    private Integer nombreEleves;
    
    @JsonProperty("nombreLivres")
    private Integer nombreLivres;
    
    @JsonProperty("pointsMoyens")
    private Integer pointsMoyens;

}
