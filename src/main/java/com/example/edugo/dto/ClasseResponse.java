package com.example.edugo.dto;

public class ClasseResponse {
    private Long id;
    private String nom;
    private String niveauNom;

    // Constructeurs
    public ClasseResponse() {}

    public ClasseResponse(Long id, String nom, String niveauNom) {
        this.id = id;
        this.nom = nom;
        this.niveauNom = niveauNom;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNiveauNom() { return niveauNom; }
    public void setNiveauNom(String niveauNom) { this.niveauNom = niveauNom; }
}
