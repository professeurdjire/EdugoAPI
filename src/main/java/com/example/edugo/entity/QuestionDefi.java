package com.example.edugo.entity;

import com.example.edugo.entity.Principales.TypeQuestion;
import jakarta.persistence.*;

@Entity
@Table(name = "questions_defi")
public class QuestionDefi {
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

    @ManyToOne
    @JoinColumn(name = "defi_id", nullable = false)
    private TypeQuestion.Defi defi;

    // Constructeurs
    public QuestionDefi() {}

    public QuestionDefi(String texte, String type, TypeQuestion.Defi defi) {
        this.texte = texte;
        this.type = type;
        this.defi = defi;
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

    public TypeQuestion.Defi getDefi() { return defi; }
    public void setDefi(TypeQuestion.Defi defi) { this.defi = defi; }
}