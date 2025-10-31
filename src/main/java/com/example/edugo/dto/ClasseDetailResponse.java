package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ClasseDetailResponse {
    private Long id;
    private String nom;
    private Long niveauId;
    private String niveauNom;
    private List<EleveLiteResponse> eleves;
    private int nombreEleves;
}
