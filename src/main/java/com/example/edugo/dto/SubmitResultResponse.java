package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitResultResponse {
    @JsonProperty("ownerId")
    private Long ownerId;
    
    @JsonProperty("ownerType")
    private String ownerType; // QUIZ | CHALLENGE | EXERCICE | DEFI
    
    @JsonProperty("eleveId")
    private Long eleveId;
    
    @JsonProperty("score")
    private Integer score;
    
    @JsonProperty("totalPoints")
    private Integer totalPoints;
    
    @JsonProperty("details")
    private List<SubmitResultDetail> details = new ArrayList<>();
}
