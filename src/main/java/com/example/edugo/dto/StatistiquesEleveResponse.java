package com.example.edugo.dto;

public class StatistiquesEleveResponse {
    private Long eleveId;
    private String nom;
    private String prenom;
    private Integer points;
    private Long exercicesRealises;
    private Long defisParticipes;
    private Long challengesParticipes;

    public StatistiquesEleveResponse() {}

    public StatistiquesEleveResponse(Long eleveId, String nom, String prenom, Integer points, Long exercicesRealises, Long defisParticipes, Long challengesParticipes) {
        this.eleveId = eleveId;
        this.nom = nom;
        this.prenom = prenom;
        this.points = points;
        this.exercicesRealises = exercicesRealises;
        this.defisParticipes = defisParticipes;
        this.challengesParticipes = challengesParticipes;
    }

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Long getExercicesRealises() { return exercicesRealises; }
    public void setExercicesRealises(Long exercicesRealises) { this.exercicesRealises = exercicesRealises; }
    public Long getDefisParticipes() { return defisParticipes; }
    public void setDefisParticipes(Long defisParticipes) { this.defisParticipes = defisParticipes; }
    public Long getChallengesParticipes() { return challengesParticipes; }
    public void setChallengesParticipes(Long challengesParticipes) { this.challengesParticipes = challengesParticipes; }
}
