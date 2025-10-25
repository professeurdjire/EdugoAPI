package com.example.edugo.entity.Principales;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "objectifs")
public class Objectif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjectif;

    @Enumerated(EnumType.STRING)
    private TypeObjectif typeObjectif;

    private int nbreLivre;
    private LocalDate dateEnvoie;
    private double progression;

    // 🔹 Relation avec Eleve
    @ManyToOne
    @JoinColumn(name = "id_eleve", nullable = false)
    private Eleve eleve;

    // --- Constructeurs ---
    public Objectif() {}

    public Objectif(TypeObjectif typeObjectif, int nbreLivre, LocalDate dateEnvoie, double progression, Eleve eleve) {
        this.typeObjectif = typeObjectif;
        this.nbreLivre = nbreLivre;
        this.dateEnvoie = dateEnvoie;
        this.progression = progression;
        this.eleve = eleve;
    }

    // --- Getters & Setters ---
    public Long getIdObjectif() { return idObjectif; }
    public void setIdObjectif(Long idObjectif) { this.idObjectif = idObjectif; }

    public TypeObjectif getTypeObjectif() { return typeObjectif; }
    public void setTypeObjectif(TypeObjectif typeObjectif) { this.typeObjectif = typeObjectif; }

    public int getNbreLivre() { return nbreLivre; }
    public void setNbreLivre(int nbreLivre) { this.nbreLivre = nbreLivre; }

    public LocalDate getDateEnvoie() { return dateEnvoie; }
    public void setDateEnvoie(LocalDate dateEnvoie) { this.dateEnvoie = dateEnvoie; }

    public double getProgression() { return progression; }
    public void setProgression(double progression) { this.progression = progression; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }
}

