package com.example.edugo.dto;

public class ClasseRequest {
    private String nom;
    private Long niveauId;

    // Constructeurs
    public ClasseRequest() {}

    public ClasseRequest(String nom, Long niveauId) {
        this.nom = nom;
        this.niveauId = niveauId;
    }

    // Getters et Setters
    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}

    public Long getNiveauId() {return niveauId;}
    public void setNiveauId(Long niveauId) {this.niveauId = niveauId;}
}
