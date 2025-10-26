package com.example.edugo.dto;

public class MatiereResponse {

    private Long id;
    private String nom;

    // Constructeurs
    public MatiereResponse() {}

    public MatiereResponse(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
