package com.example.edugo.dto;

public class ReponsePossibleRequest {
    private String texte;
    private Boolean correcte;

    public ReponsePossibleRequest() {
    }

    public ReponsePossibleRequest(String texte, Boolean correcte) {
        this.texte = texte;
        this.correcte = correcte;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Boolean getCorrecte() {
        return correcte;
    }

    public void setCorrecte(Boolean correcte) {
        this.correcte = correcte;
    }

    @Override
    public String toString() {
        return "ReponsePossibleRequest{" +
                "texte='" + texte + '\'' +
                ", correcte=" + correcte +
                '}';
    }
}
