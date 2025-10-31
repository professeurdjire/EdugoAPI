package com.example.edugo.dto;

import lombok.Data;

@Data
public class TagResponse {
    private Long id;
    private String libelle;
    private String description;
    private String couleur;
}
