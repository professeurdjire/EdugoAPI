package com.example.edugo.entity.Principales;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reponseEleve")
public class ReponseEleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String reponse; // réponse donnée par l'élève
    private double pointsAttribues;
    private LocalDateTime dateReponse;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Eleve getEleve() {return eleve;}
    public void setEleve(Eleve eleve) {this.eleve = eleve;}

    public Question getQuestion() {return question;}
    public void setQuestion(Question question) {this.question = question;}

    public String getReponse() {return reponse;}
    public void setReponse(String reponse) {this.reponse = reponse;}

    public double getPointsAttribues() {return pointsAttribues;}
    public void setPointsAttribues(double pointsAttribues) {this.pointsAttribues = pointsAttribues;}

    public LocalDateTime getDateReponse() {return dateReponse;}

    public void setDateReponse(LocalDateTime dateReponse) {this.dateReponse = dateReponse;}
}

