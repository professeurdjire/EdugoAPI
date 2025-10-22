package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class ReponsePossible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelleReponse;

    private boolean estCorrecte;

    // --- RELATION : une réponse appartient à une seule question ---
    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference(value = "question-reponses")
    private Question question;

    // --- CONSTRUCTEURS ---

    public ReponsePossible() {}

    public ReponsePossible(String libelleReponse, boolean estCorrecte) {
        this.libelleReponse = libelleReponse;
        this.estCorrecte = estCorrecte;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleReponse() {
        return libelleReponse;
    }

    public void setLibelleReponse(String libelleReponse) {
        this.libelleReponse = libelleReponse;
    }

    public boolean isEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


}
