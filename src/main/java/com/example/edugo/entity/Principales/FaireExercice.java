package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "faireExercice")
public class FaireExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // identifiant unique de la table associative

    // Relation avec Eleve
    @ManyToOne
    @JoinColumn(name = "id_eleve", nullable = false)
    private Eleve eleve;

    // Relation avec Exercice
    @ManyToOne
    @JoinColumn(name = "id_exercice", nullable = false)
    private Exercice exercice;

    private int pointExercice;

    @Enumerated(EnumType.STRING)
    private StatutExercice statut;

    private LocalDate dateExercice;

    // --- Constructeurs ---
    public FaireExercice() {}

    public FaireExercice(Eleve eleve, Exercice exercice, int pointExercice, StatutExercice statut, LocalDate dateExercice) {
        this.eleve = eleve;
        this.exercice = exercice;
        this.pointExercice = pointExercice;
        this.statut = statut;
        this.dateExercice = dateExercice;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Exercice getExercice() { return exercice; }
    public void setExercice(Exercice exercice) { this.exercice = exercice; }

    public int getPointExercice() { return pointExercice; }
    public void setPointExercice(int pointExercice) { this.pointExercice = pointExercice; }

    public StatutExercice getStatut() { return statut; }
    public void setStatut(StatutExercice statut) { this.statut = statut; }

    public LocalDate getDateExercice() { return dateExercice; }
    public void setDateExercice(LocalDate dateExercice) { this.dateExercice = dateExercice; }

    public void setId(Long id) {
        this.id = id;
    }
}
