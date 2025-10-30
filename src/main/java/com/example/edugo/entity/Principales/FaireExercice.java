package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime dateExercice;

    // Champs attendus par les services
    private String reponse;
    private LocalDateTime dateSoumission;
    private Integer note;
    private String commentaire;
    private LocalDateTime dateCorrection;

    // --- Constructeurs ---
    public FaireExercice() {}

    public FaireExercice(Eleve eleve, Exercice exercice, int pointExercice, StatutExercice statut, LocalDateTime dateExercice) {
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

    public LocalDateTime getDateExercice() { return dateExercice; }
    public void setDateExercice(LocalDateTime dateExercice) { this.dateExercice = dateExercice; }

    // Champs supplémentaires pour compatibilité avec les services
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDateCorrection() { return dateCorrection; }
    public void setDateCorrection(LocalDateTime dateCorrection) { this.dateCorrection = dateCorrection; }

    public void setId(Long id) {
        this.id = id;
    }
}
