package com.example.edugo.dto;

public class ReponsePossibleResponse {
    private Long id;
    private String libelle;
    private Boolean estCorrecte;

    public ReponsePossibleResponse() {
    }

    public ReponsePossibleResponse(Long id, String libelle, Boolean estCorrecte) {
        this.id = id;
        this.libelle = libelle;
        this.estCorrecte = estCorrecte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(Boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    @Override
    public String toString() {
        return "ReponsePossibleResponse{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", estCorrecte=" + estCorrecte +
                '}';
    }
}
