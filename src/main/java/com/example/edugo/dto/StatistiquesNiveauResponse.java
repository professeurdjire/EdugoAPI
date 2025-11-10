package com.example.edugo.dto;

public class StatistiquesNiveauResponse {
    private Long niveauId;
    private String nomNiveau;
    private Integer nombreClasses;
    private Integer nombreEleves;
    private Integer nombreLivres;
    private Integer pointsMoyens;

    public StatistiquesNiveauResponse() {}
    public StatistiquesNiveauResponse(Long niveauId, String nomNiveau, Integer nombreClasses, Integer nombreEleves, Integer nombreLivres, Integer pointsMoyens) {
        this.niveauId = niveauId;
        this.nomNiveau = nomNiveau;
        this.nombreClasses = nombreClasses;
        this.nombreEleves = nombreEleves;
        this.nombreLivres = nombreLivres;
        this.pointsMoyens = pointsMoyens;
    }
    public Long getNiveauId() { return niveauId; }
    public void setNiveauId(Long niveauId) { this.niveauId = niveauId; }
    public String getNomNiveau() { return nomNiveau; }
    public void setNomNiveau(String nomNiveau) { this.nomNiveau = nomNiveau; }
    public Integer getNombreClasses() { return nombreClasses; }
    public void setNombreClasses(Integer nombreClasses) { this.nombreClasses = nombreClasses; }
    public Integer getNombreEleves() { return nombreEleves; }
    public void setNombreEleves(Integer nombreEleves) { this.nombreEleves = nombreEleves; }
    public Integer getNombreLivres() { return nombreLivres; }
    public void setNombreLivres(Integer nombreLivres) { this.nombreLivres = nombreLivres; }
    public Integer getPointsMoyens() { return pointsMoyens; }
    public void setPointsMoyens(Integer pointsMoyens) { this.pointsMoyens = pointsMoyens; }
}
