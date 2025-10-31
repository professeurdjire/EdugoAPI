package com.example.edugo.dto;

import jakarta.validation.constraints.NotBlank;

public class MatiereRequest {
    @NotBlank(message = "Le nom de la matière est obligatoire")
    private String nom;

    public MatiereRequest() {
    }

    public MatiereRequest(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "MatiereRequest{" +
                "nom='" + nom + '\'' +
                '}';
    }
}