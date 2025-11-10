package com.example.edugo.dto;

public class StatistiquesExerciceResponse {
    private Long exerciceId;
    private String titre;
    private Integer nombreTentatives;
    private Integer nombreReussites;
    private Double noteMoyenne;

    public StatistiquesExerciceResponse() {}

    public StatistiquesExerciceResponse(Long exerciceId, String titre, Integer nombreTentatives, Integer nombreReussites, Double noteMoyenne) {
        this.exerciceId = exerciceId;
        this.titre = titre;
        this.nombreTentatives = nombreTentatives;
        this.nombreReussites = nombreReussites;
        this.noteMoyenne = noteMoyenne;
    }

    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Integer getNombreTentatives() { return nombreTentatives; }
    public void setNombreTentatives(Integer nombreTentatives) { this.nombreTentatives = nombreTentatives; }
    public Integer getNombreReussites() { return nombreReussites; }
    public void setNombreReussites(Integer nombreReussites) { this.nombreReussites = nombreReussites; }
    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
}
