package com.example.edugo.entity;

import com.example.edugo.entity.Principales.Livre;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "langues")
public class Langue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(name = "code_iso")
    private String codeIso;

    @OneToMany(mappedBy = "langue", cascade = CascadeType.ALL)
    private List<Livre> livres = new ArrayList<>();

    // Constructeurs
    public Langue() {}

    public Langue(String nom, String codeIso) {
        this.nom = nom;
        this.codeIso = codeIso;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCodeIso() { return codeIso; }
    public void setCodeIso(String codeIso) { this.codeIso = codeIso; }

    public List<Livre> getLivres() { return livres; }
    public void setLivres(List<Livre> livres) { this.livres = livres; }
}