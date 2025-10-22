package com.example.edugo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions_challenge")
public class QuestionChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texte;

    private String type;

    @Column(length = 1000)
    private String choix;

    @Column(name = "bonne_reponse")
    private String bonneReponse;

    private Integer points;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    // Constructeurs
    public QuestionChallenge() {}

    public QuestionChallenge(String texte, String type, Challenge challenge) {
        this.texte = texte;
        this.type = type;
        this.challenge = challenge;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getChoix() { return choix; }
    public void setChoix(String choix) { this.choix = choix; }

    public String getBonneReponse() { return bonneReponse; }
    public void setBonneReponse(String bonneReponse) { this.bonneReponse = bonneReponse; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Challenge getChallenge() { return challenge; }
    public void setChallenge(Challenge challenge) { this.challenge = challenge; }
}