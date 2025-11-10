package com.example.edugo.dto;

public class NiveauResponse {
    private Long id;
    private String nom;

    public NiveauResponse() {}
    public NiveauResponse(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return "NiveauResponse{" + "id=" + id + ", nom='" + nom + '\'' + '}';
    }
}
