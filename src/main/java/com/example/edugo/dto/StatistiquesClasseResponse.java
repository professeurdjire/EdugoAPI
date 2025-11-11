package com.example.edugo.dto;

public class StatistiquesClasseResponse {
    private Long classeId;
    private String nomClasse;
    private String niveau;
    private Integer nombreEleves;
    private Integer pointsMoyens;

    public StatistiquesClasseResponse() {}
    public StatistiquesClasseResponse(Long classeId, String nomClasse, String niveau, Integer nombreEleves, Integer pointsMoyens) {
        this.classeId = classeId;
        this.nomClasse = nomClasse;
        this.niveau = niveau;
        this.nombreEleves = nombreEleves;
        this.pointsMoyens = pointsMoyens;
    }
    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }
    public String getNomClasse() { return nomClasse; }
    public void setNomClasse(String nomClasse) { this.nomClasse = nomClasse; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public Integer getNombreEleves() { return nombreEleves; }
    public void setNombreEleves(Integer nombreEleves) { this.nombreEleves = nombreEleves; }
    public Integer getPointsMoyens() { return pointsMoyens; }
    public void setPointsMoyens(Integer pointsMoyens) { this.pointsMoyens = pointsMoyens; }
}
