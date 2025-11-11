package com.example.edugo.entity.Principales;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String enonce;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    // Relation entre une question et un challenge
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @JsonBackReference(value = "challenge-questions")
    private Challenge challenge;

    // Relation entre une question et un quiz
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonBackReference(value = "quiz-questions")
    private Quiz quiz;

    // Relation entre une question et un exercice
    @ManyToOne
    @JoinColumn(name = "exercice_id")
    @JsonBackReference(value = "exercice-questions")
    private Exercice exercice;

    // Relation entre une question et  typeQuestion
    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonBackReference(value = "type-questions")
    private TypeQuestion type;

    // Relation entre question  et les réponses possibles
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "question-reponses")
    private List<ReponsePossible> reponsesPossibles;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReponseEleve> reponsesEleves = new ArrayList<>();



    // Constructeur
    public Question(String enonce, Integer points, Quiz quiz) {
        this.enonce = enonce;
        this.points = points;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEnonce() {
        return enonce;
    }
    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public Integer getPoints() {
        return points;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public Challenge getChallenge() {
        return challenge;
    }
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Exercice getExercice() {
        return exercice;
    }
    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public TypeQuestion getType() {
        return type;
    }
    public void setType(TypeQuestion type) {
        this.type = type;
    }

    public List<ReponsePossible> getReponsesPossibles() {
        return reponsesPossibles;
    }
    public void setReponsesPossibles(List<ReponsePossible> reponsesPossibles) {this.reponsesPossibles = reponsesPossibles;}

    public List<ReponseEleve> getReponsesEleves() {return reponsesEleves;}
    public void setReponsesEleves(List<ReponseEleve> reponsesEleves) {this.reponsesEleves = reponsesEleves;}
}
