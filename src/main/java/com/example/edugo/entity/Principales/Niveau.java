package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "niveaux")
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<Classe> classes = new ArrayList<>();

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<Eleve> eleves = new ArrayList<>();

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<Livre> livres = new ArrayList<>();

    @OneToMany(mappedBy = "niveau")
    private List<Challenge> challenges = new ArrayList<>();

    // Constructeurs
    public Niveau() {}

    public Niveau(String nom, String description) {
        this.nom = nom;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Classe> getClasses() { return classes; }
    public void setClasses(List<Classe> classes) { this.classes = classes; }

    public List<Livre> getLivres() { return livres; }
    public void setLivres(List<Livre> livres) { this.livres = livres; }

    public List<Challenge> getChallenges() { return challenges; }
    public void setChallenges(List<Challenge> challenges) { this.challenges = challenges; }

    public List<Eleve> getEleves() {return eleves;}
    public void setEleves(List<Eleve> eleves) {this.eleves = eleves;}
}