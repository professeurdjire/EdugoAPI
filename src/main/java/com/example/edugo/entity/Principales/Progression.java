package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progressionsLecture")
public class Progression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pourcentage_completion")
    private Integer pourcentageCompletion = 0;

    @Column(name = "temps_lecture") // en minutes
    private Integer tempsLecture = 0;

    @Column(name = "page_actuelle")
    private Integer pageActuelle = 0;

    @Column(name = "date_derniere_lecture")
    private LocalDateTime dateDerniereLecture;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @PreUpdate
    protected void onUpdate() {
        dateDerniereLecture = LocalDateTime.now();
    }

    // Constructeurs
    public Progression() {}

    public Progression(Eleve eleve, Livre livre, Integer pourcentageCompletion, Integer tempsLecture, Integer pageActuelle, LocalDateTime dateDerniereLecture  ) {
        this.eleve = eleve;
        this.livre = livre;
        this.pourcentageCompletion = pourcentageCompletion;
        this.tempsLecture = tempsLecture;
        this.pageActuelle = pageActuelle;
        this.dateDerniereLecture = dateDerniereLecture;

    }

    public void ajouterTempsLecture(Integer minutes) {
        this.tempsLecture += minutes;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPourcentageCompletion() { return pourcentageCompletion; }
    public void setPourcentageCompletion(Integer pourcentageCompletion) { this.pourcentageCompletion = pourcentageCompletion; }

    public Integer getTempsLecture() { return tempsLecture; }
    public void setTempsLecture(Integer tempsLecture) { this.tempsLecture = tempsLecture; }

    public Integer getPageActuelle() { return pageActuelle; }
    public void setPageActuelle(Integer pageActuelle) { this.pageActuelle = pageActuelle; }


    public LocalDateTime getDateDerniereLecture() { return dateDerniereLecture; }
    public void setDateDerniereLecture(LocalDateTime dateDerniereLecture) { this.dateDerniereLecture = dateDerniereLecture; }

    // Alias attendu par certains services
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateDerniereLecture = dateMiseAJour; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }
}