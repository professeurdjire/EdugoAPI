package com.example.edugo.dto;

public class ReponsePossibleRequest {
    private String libelle;
    private Boolean estCorrecte;

    public ReponsePossibleRequest() {
    }

    public ReponsePossibleRequest(String libelle, Boolean estCorrecte) {
        this.libelle = libelle;
        this.estCorrecte = estCorrecte;
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
        return "ReponsePossibleRequest{" +
                "libelle='" + libelle + '\'' +
                ", estCorrecte=" + estCorrecte +
                '}';
    }
}
