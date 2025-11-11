package com.example.edugo.dto;

import java.util.List;

public class NiveauRequest {

    private String nom;

    public NiveauRequest() {}
    public NiveauRequest(String nom) { this.nom = nom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return "NiveauRequest{" + "nom='" + nom + '\'' + '}';
    }
}
