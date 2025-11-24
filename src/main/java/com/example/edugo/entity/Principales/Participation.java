package com.example.edugo.entity.Principales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participationsChallenge")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    private Integer rang;
    @Column(nullable = false)
    private Integer tempsPasse;

    private String statut;

    @Column(name = "date_participation")
    private LocalDateTime dateParticipation;

    @Column(name = "aParticiper")
    private boolean aParticiper;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "participations", "defis", "challenges"})
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "participations", "questions"})
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Badge badge;

    @PrePersist
    protected void onCreate() {
        dateParticipation = LocalDateTime.now();
    }

    // Constructeurs
    public Participation() {}

    public Participation(Eleve eleve, Integer score, Integer tempsPasse) {
        this.eleve = eleve;
        this.score = score;
        this.tempsPasse = tempsPasse;
    }

    // Méthodes utilitaires
    public void attribuerBadge(Badge badge) {
        this.badge = badge;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getRang() { return rang; }
    public void setRang(Integer rang) { this.rang = rang; }

    public Integer getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(Integer tempsPasse) {this.tempsPasse = tempsPasse; }

    public boolean isaParticiper() {return aParticiper;}
    public void setaParticiper(boolean aParticiper) {this.aParticiper = aParticiper;}

    public LocalDateTime getDateParticipation() { return dateParticipation; }
    public void setDateParticipation(LocalDateTime dateParticipation) { this.dateParticipation = dateParticipation; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Challenge getChallenge() { return challenge; }
    public void setChallenge(Challenge challenge) { this.challenge = challenge; }

    public Badge getBadge() { return badge; }
    public void setBadge(Badge badge) { this.badge = badge; }

}