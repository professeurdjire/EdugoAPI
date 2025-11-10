package com.example.edugo.dto;

public class BadgeReponse {
    private Long id;
    private String nom;
    private String description;
    private String icone;
    private int nombreParticipations; // optionnel, pour info

    // Constructeurs
    public BadgeReponse() {}

    public BadgeReponse(Long id, String nom, String description,  String icone, int nombreParticipations) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.icone = icone;
        this.nombreParticipations = nombreParticipations;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public int getNombreParticipations() {
        return nombreParticipations;
    }

    public void setNombreParticipations(int nombreParticipations) {
        this.nombreParticipations = nombreParticipations;
    }
}
