package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "faireQuiz")
public class EleveQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;


    @Column(name = "date_Quiz")
    private LocalDateTime dateQuiz;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @PrePersist
    protected void onCreate() {
        dateQuiz = LocalDateTime.now();
    }

    // Constructeurs
    public EleveQuiz() {}

    public EleveQuiz(Eleve eleve,Quiz quiz, Integer score) {
        this.eleve = eleve;
        this.quiz = quiz;
        this.score = score;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public LocalDateTime getDateQuiz() { return dateQuiz; }
    public void setDateQuiz(LocalDateTime dateSoumission) { this.dateQuiz = dateSoumission; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}