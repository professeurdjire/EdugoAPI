package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitResultDetail {
    @JsonProperty("questionId")
    private Long questionId;
    
    @JsonProperty("points")
    private Integer points;
    
    @JsonProperty("correct")
    private Boolean correct;
}

