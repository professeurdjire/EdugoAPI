package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionRequest {
    private String intitule;
    private String type;
    private List<ReponsePossibleRequest> reponsesPossibles;
    private Integer numeroOrdre;
}
