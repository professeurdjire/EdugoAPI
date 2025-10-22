package com.example.edugo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    @Enumerated(EnumType.STRING)
    private TypeBadge type;

    private String icone;

    @Column(name = "applique_a")
    private String appliqueA; // QUIZ/DEFI/CHALLENGE

    @ManyToMany(mappedBy = "rewards")
    private List<Challenge> challenges = new ArrayList<>();

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    // Enum pour le type de badge
    public enum TypeBadge {
        OR, ARGENT, BRONZE, SPECIAL
    }

    // Constructeurs
    public Badge() {}

    public Badge(String nom, String description, TypeBadge type) {
        this.nom = nom;
        this.description = description;
        this.type = type;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TypeBadge getType() { return type; }
    public void setType(TypeBadge type) { this.type = type; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getAppliqueA() { return appliqueA; }
    public void setAppliqueA(String appliqueA) { this.appliqueA = appliqueA; }

    public List<Challenge> getChallenges() { return challenges; }
    public void setChallenges(List<Challenge> challenges) { this.challenges = challenges; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }
}