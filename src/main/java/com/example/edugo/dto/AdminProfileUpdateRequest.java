package com.example.edugo.dto;

import lombok.Data;

@Data
public class AdminProfileUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String photoProfil;
    private Boolean estActive;
}

