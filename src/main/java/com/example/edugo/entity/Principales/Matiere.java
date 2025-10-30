package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matieres")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "matiere", cascade = CascadeType.ALL)
    private List<Livre> livres = new ArrayList<>();
    @OneToMany(mappedBy = "matiere", cascade = CascadeType.ALL)
    private List<Exercice> Exercice = new ArrayList<>();

    // Constructeurs
    public Matiere() {}

    public Matiere(String nom) {
        this.nom = nom;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Livre> getLivres() { return livres; }
    public void setLivres(List<Livre> livres) { this.livres = livres; }
    public List<Exercice> getExercice() { return Exercice; }

    public void setExercice(List<Exercice> exercice) {
        Exercice = exercice;
    }
}