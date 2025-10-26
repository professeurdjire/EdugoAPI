package com.example.edugo.dto;

public class NiveauRequest {

    private String nom;

    public NiveauRequest() {}

    public NiveauRequest(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
