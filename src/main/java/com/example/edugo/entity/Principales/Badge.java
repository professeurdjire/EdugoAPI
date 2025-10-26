package com.example.edugo.entity.Principales;

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

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    // Constructeurs
    public Badge() {}

    public Badge(String nom, String description, TypeBadge type, String icone) {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.icone = icone;
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

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }
}