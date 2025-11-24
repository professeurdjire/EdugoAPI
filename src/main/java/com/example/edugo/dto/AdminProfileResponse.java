package com.example.edugo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String photoProfil;
    private String role;
    private Boolean estActive;
    private java.time.LocalDateTime dateCreation;
    private java.time.LocalDateTime dateModification;
}

