package com.example.edugo.dto;

public class ClassementEleveResponse {
    private Long eleveId;
    private String nom;
    private String prenom;
    private Integer points;
    private Integer position;

    public ClassementEleveResponse() {}

    public ClassementEleveResponse(Long eleveId, String nom, String prenom, Integer points, Integer position) {
        this.eleveId = eleveId;
        this.nom = nom;
        this.prenom = prenom;
        this.points = points;
        this.position = position;
    }

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
