package com.example.edugo.dto;

import java.util.List;

public class MatiereDetailResponse {
    private Long id;
    private String nom;
    private int nombreLivres;
    private int nombreExercices;
    private int nombreExercicesActifs;
    private Object statistiques;

    public MatiereDetailResponse() {
    }

    public MatiereDetailResponse(Long id, String nom, int nombreLivres, int nombreExercices, int nombreExercicesActifs, Object statistiques) {
        this.id = id;
        this.nom = nom;
        this.nombreLivres = nombreLivres;
        this.nombreExercices = nombreExercices;
        this.nombreExercicesActifs = nombreExercicesActifs;
        this.statistiques = statistiques;
    }

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

    public int getNombreLivres() {
        return nombreLivres;
    }

    public void setNombreLivres(int nombreLivres) {
        this.nombreLivres = nombreLivres;
    }

    public int getNombreExercices() {
        return nombreExercices;
    }

    public void setNombreExercices(int nombreExercices) {
        this.nombreExercices = nombreExercices;
    }

    public int getNombreExercicesActifs() {
        return nombreExercicesActifs;
    }

    public void setNombreExercicesActifs(int nombreExercicesActifs) {
        this.nombreExercicesActifs = nombreExercicesActifs;
    }

    public Object getStatistiques() {
        return statistiques;
    }

    public void setStatistiques(Object statistiques) {
        this.statistiques = statistiques;
    }

    @Override
    public String toString() {
        return "MatiereDetailResponse{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nombreLivres=" + nombreLivres +
                ", nombreExercices=" + nombreExercices +
                ", nombreExercicesActifs=" + nombreExercicesActifs +
                ", statistiques=" + statistiques +
                '}';
    }
}