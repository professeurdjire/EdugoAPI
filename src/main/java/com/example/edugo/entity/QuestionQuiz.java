package com.example.edugo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions_quiz")
public class QuestionQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texte;

    @Enumerated(EnumType.STRING)
    private TypeQuestion type;

    @ElementCollection
    @CollectionTable(name = "question_choix", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "choix")
    private List<String> choix = new ArrayList<>();

    @Column(name = "bonne_reponse")
    private String bonneReponse;

    @Column(name = "points")
    private Integer points = 1;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Resultat> resultats = new ArrayList<>();

    // Enum pour le type de question
    public enum TypeQuestion {
        CHOIX_MULTIPLE, VRAI_FAUX, REPONSE_COURTE, REPONSE_LIBRE
    }

    // Constructeurs
    public QuestionQuiz() {}

    public QuestionQuiz(String texte, TypeQuestion type, Quiz quiz) {
        this.texte = texte;
        this.type = type;
        this.quiz = quiz;
    }

    // Méthodes utilitaires
    public void ajouterChoix(String choixText) {
        this.choix.add(choixText);
    }

    public boolean estCorrecte(String reponse) {
        return bonneReponse != null && bonneReponse.equalsIgnoreCase(reponse);
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public TypeQuestion getType() { return type; }
    public void setType(TypeQuestion type) { this.type = type; }

    public List<String> getChoix() { return choix; }
    public void setChoix(List<String> choix) { this.choix = choix; }

    public String getBonneReponse() { return bonneReponse; }
    public void setBonneReponse(String bonneReponse) { this.bonneReponse = bonneReponse; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public com.example.edugo.entity.Quiz getQuiz() { return quiz; }
    public void setQuiz(com.example.edugo.entity.Quiz quiz) { this.quiz = quiz; }

    public List<Resultat> getResultats() { return resultats; }
    public void setResultats(List<Resultat> resultats) { this.resultats = resultats; }
}