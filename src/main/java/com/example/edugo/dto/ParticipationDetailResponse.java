package com.example.edugo.dto;

import java.time.LocalDateTime;

public class ParticipationDetailResponse {
    private Long id;
    private Long eleveId;
    private String eleveNom;
    private String elevePrenom;
    private Long challengeId;
    private String challengeTitre;
    private Integer score;
    private Integer totalPoints;
    private Integer rang;
    private Integer tempsPasse; // en secondes
    private String statut; // EN_COURS, TERMINE, VALIDE, DISQUALIFIE
    private LocalDateTime dateParticipation;
    private Long badgeId;
    private String badgeNom;
    private String badgeIcone;
    private Double pourcentageReussite;
    private Integer pointsGagnes;

    public ParticipationDetailResponse() {
    }

    public ParticipationDetailResponse(Long id, Long eleveId, String eleveNom, String elevePrenom,
                                      Long challengeId, String challengeTitre, Integer score, Integer totalPoints,
                                      Integer rang, Integer tempsPasse, String statut, LocalDateTime dateParticipation,
                                      Long badgeId, String badgeNom, String badgeIcone, Double pourcentageReussite,
                                      Integer pointsGagnes) {
        this.id = id;
        this.eleveId = eleveId;
        this.eleveNom = eleveNom;
        this.elevePrenom = elevePrenom;
        this.challengeId = challengeId;
        this.challengeTitre = challengeTitre;
        this.score = score;
        this.totalPoints = totalPoints;
        this.rang = rang;
        this.tempsPasse = tempsPasse;
        this.statut = statut;
        this.dateParticipation = dateParticipation;
        this.badgeId = badgeId;
        this.badgeNom = badgeNom;
        this.badgeIcone = badgeIcone;
        this.pourcentageReussite = pourcentageReussite;
        this.pointsGagnes = pointsGagnes;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }

    public String getEleveNom() { return eleveNom; }
    public void setEleveNom(String eleveNom) { this.eleveNom = eleveNom; }

    public String getElevePrenom() { return elevePrenom; }
    public void setElevePrenom(String elevePrenom) { this.elevePrenom = elevePrenom; }

    public Long getChallengeId() { return challengeId; }
    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }

    public String getChallengeTitre() { return challengeTitre; }
    public void setChallengeTitre(String challengeTitre) { this.challengeTitre = challengeTitre; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Integer getRang() { return rang; }
    public void setRang(Integer rang) { this.rang = rang; }

    public Integer getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(Integer tempsPasse) { this.tempsPasse = tempsPasse; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateParticipation() { return dateParticipation; }
    public void setDateParticipation(LocalDateTime dateParticipation) { this.dateParticipation = dateParticipation; }

    public Long getBadgeId() { return badgeId; }
    public void setBadgeId(Long badgeId) { this.badgeId = badgeId; }

    public String getBadgeNom() { return badgeNom; }
    public void setBadgeNom(String badgeNom) { this.badgeNom = badgeNom; }

    public String getBadgeIcone() { return badgeIcone; }
    public void setBadgeIcone(String badgeIcone) { this.badgeIcone = badgeIcone; }

    public Double getPourcentageReussite() { return pourcentageReussite; }
    public void setPourcentageReussite(Double pourcentageReussite) { this.pourcentageReussite = pourcentageReussite; }

    public Integer getPointsGagnes() { return pointsGagnes; }
    public void setPointsGagnes(Integer pointsGagnes) { this.pointsGagnes = pointsGagnes; }
}
