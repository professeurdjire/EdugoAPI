package com.example.edugo.dto;

import java.time.LocalDateTime;

public class ChallengeLeaderboardEntryResponse {
    private Long eleveId;
    private String nom;
    private String prenom;
    private LocalDateTime dateParticipation;
    private Integer points;

    public ChallengeLeaderboardEntryResponse() {}

    public ChallengeLeaderboardEntryResponse(Long eleveId, String nom, String prenom, LocalDateTime dateParticipation, Integer points) {
        this.eleveId = eleveId;
        this.nom = nom;
        this.prenom = prenom;
        this.dateParticipation = dateParticipation;
        this.points = points;
    }

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDateTime getDateParticipation() { return dateParticipation; }
    public void setDateParticipation(LocalDateTime dateParticipation) { this.dateParticipation = dateParticipation; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
