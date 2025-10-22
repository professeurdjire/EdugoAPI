package com.example.edugo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String description;

    private Integer duree; // en minutes

    @Enumerated(EnumType.STRING)
    private StatutQuiz statut = StatutQuiz.ACTIF;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuestionQuiz> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();



    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructeurs
    public Quiz() {}

    public Quiz(String titre, String description, Integer duree, Livre livre) {
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.livre = livre;
    }

    // Méthodes utilitaires
    public void ajouterQuestion(QuestionQuiz question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public int getNombreQuestions() {
        return questions.size();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public StatutQuiz getStatut() { return statut; }
    public void setStatut(StatutQuiz statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }

    public List<QuestionQuiz> getQuestions() { return questions; }
    public void setQuestions(List<QuestionQuiz> questions) { this.questions = questions; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }
}