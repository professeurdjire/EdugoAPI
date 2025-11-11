package com.example.edugo.entity.Principales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "niveau_id", nullable = false)
    private Niveau niveau;

    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Eleve> eleves = new ArrayList<>();

    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Livre> livres = new ArrayList<>();

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    private List<Challenge> challenges = new ArrayList<>();



    // Constructeurs
    public Classe() {}

    public Classe(String nom, Niveau niveau) {
        this.nom = nom;
        this.niveau = niveau;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Niveau getNiveau() { return niveau; }
    public void setNiveau(Niveau niveau) { this.niveau = niveau; }

    public List<Eleve> getEleves() { return eleves; }
    public void setEleves(List<Eleve> eleves) { this.eleves = eleves; }

    public List<Livre> getLivres() { return livres; }
    public void setLivres(List<Livre> livres) { this.livres = livres; }

    public List<Challenge> getChallenges() { return challenges; }
    public void setChallenges(List<Challenge> challenges) { this.challenges = challenges; }

}