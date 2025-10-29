package com.example.edugo.entity.Principales;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Type_questions")
public class TypeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelleType;

    // --- RELATION : un type peut concerner plusieurs questions ---
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "type-questions")
    private List<Question> questions;

    // --- CONSTRUCTEURS ---

    public TypeQuestion() {}

    public TypeQuestion(String libelleType) {
        this.libelleType = libelleType;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleType() {
        return libelleType;
    }

    public void setLibelleType(String libelleType) {
        this.libelleType = libelleType;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
