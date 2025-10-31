package com.example.edugo.dto;

import lombok.Data;
import java.util.List;

@Data
public class TagDetailResponse {
    private Long id;
    private String libelle;
    private String description;
    private String couleur;
    // Ajoutez ici éventuellement : liste d'entités associées
}
