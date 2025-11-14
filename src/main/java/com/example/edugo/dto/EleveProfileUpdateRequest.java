package com.example.edugo.dto;

import lombok.Data;

@Data
public class EleveProfileUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String photoProfil;
    private String ville;
    private Long classeId;
    private Long niveauId;
}


