package com.example.edugo.dto;

import lombok.Data;

@Data
public class ClasseResponse {
    private Long id;
    private String nom;
    private Long niveauId;
    private String niveauNom;
}
