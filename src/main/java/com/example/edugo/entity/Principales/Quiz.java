package com.example.edugo.entity.Principales;

import com.example.edugo.entity.StatutQuiz;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    private StatutQuiz statut = StatutQuiz.ACTIF;

    @Column(name = "titre")
    private String titre;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Relation inverse OneToOne avec Livre ---
    @OneToOne(mappedBy = "quiz")
    @JsonBackReference(value = "livre-quiz")
    private Livre livre;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questionsQuiz = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructeurs
    public Quiz() {}

    public Quiz(Livre livre) {
        this.livre = livre;
        if (livre != null) {
            this.titre = livre.getTitre();
        }
    }

    public int getNombreQuestions() {
        return questionsQuiz.size();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StatutQuiz getStatut() { return statut; }
    public void setStatut(StatutQuiz statut) { this.statut = statut; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }

    public List<Question> getQuestionsQuiz() { return questionsQuiz; }
    public void setQuestionsQuiz(List<Question> questionsQuiz) { this.questionsQuiz = questionsQuiz; }


}