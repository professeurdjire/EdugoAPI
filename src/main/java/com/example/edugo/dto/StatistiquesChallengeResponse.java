package com.example.edugo.dto;

public class StatistiquesChallengeResponse {
    private Long challengeId;
    private String titre;
    private Integer nombreParticipations;
    private Integer nombreValidations;
    private Integer nombreDisqualifications;
    private Double tauxParticipation;

    public StatistiquesChallengeResponse() {}

    public StatistiquesChallengeResponse(Long challengeId, String titre, Integer nombreParticipations, Integer nombreValidations, Integer nombreDisqualifications, Double tauxParticipation) {
        this.challengeId = challengeId;
        this.titre = titre;
        this.nombreParticipations = nombreParticipations;
        this.nombreValidations = nombreValidations;
        this.nombreDisqualifications = nombreDisqualifications;
        this.tauxParticipation = tauxParticipation;
    }

    public Long getChallengeId() { return challengeId; }
    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Integer getNombreParticipations() { return nombreParticipations; }
    public void setNombreParticipations(Integer nombreParticipations) { this.nombreParticipations = nombreParticipations; }
    public Integer getNombreValidations() { return nombreValidations; }
    public void setNombreValidations(Integer nombreValidations) { this.nombreValidations = nombreValidations; }
    public Integer getNombreDisqualifications() { return nombreDisqualifications; }
    public void setNombreDisqualifications(Integer nombreDisqualifications) { this.nombreDisqualifications = nombreDisqualifications; }
    public Double getTauxParticipation() { return tauxParticipation; }
    public void setTauxParticipation(Double tauxParticipation) { this.tauxParticipation = tauxParticipation; }
}
