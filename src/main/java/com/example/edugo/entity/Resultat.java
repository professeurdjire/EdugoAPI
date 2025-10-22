package com.example.edugo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultats")
public class Resultat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @Column(name = "reponse_eleve")
    private String reponseEleve;

    @Column(name = "est_correct")
    private Boolean estCorrect;

    @Column(name = "date_soumission")
    private LocalDateTime dateSoumission;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionQuiz question;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @PrePersist
    protected void onCreate() {
        dateSoumission = LocalDateTime.now();
    }

    // Constructeurs
    public Resultat() {}

    public Resultat(Eleve eleve, QuestionQuiz question, String reponseEleve) {
        this.eleve = eleve;
        this.question = question;
        this.reponseEleve = reponseEleve;
        this.estCorrect = question.estCorrecte(reponseEleve);
        this.score = this.estCorrect ? question.getPoints() : 0;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getReponseEleve() { return reponseEleve; }
    public void setReponseEleve(String reponseEleve) { this.reponseEleve = reponseEleve; }

    public Boolean getEstCorrect() { return estCorrect; }
    public void setEstCorrect(Boolean estCorrect) { this.estCorrect = estCorrect; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public QuestionQuiz getQuestion() { return question; }
    public void setQuestion(QuestionQuiz question) { this.question = question; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}