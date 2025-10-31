package com.example.edugo.dto;

public class ReponsePossibleResponse {
    private Long id;
    private String texte;
    private Boolean correcte;

    public ReponsePossibleResponse() {
    }

    public ReponsePossibleResponse(Long id, String texte, Boolean correcte) {
        this.id = id;
        this.texte = texte;
        this.correcte = correcte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "ReponsePossibleResponse{" +
                "id=" + id +
                ", texte='" + texte + '\'' +
                ", correcte=" + correcte +
                '}';
    }
}
