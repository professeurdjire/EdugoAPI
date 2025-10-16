package com.example.edugo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progressions")
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

    @Column(name = "total_pages")
    private Integer totalPages;

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

    public Progression(Eleve eleve, Livre livre) {
        this.eleve = eleve;
        this.livre = livre;
    }

    // Méthodes utilitaires
    public void mettreAJourProgression(Integer pageActuelle, Integer totalPages) {
        this.pageActuelle = pageActuelle;
        this.totalPages = totalPages;
        if (totalPages != null && totalPages > 0) {
            this.pourcentageCompletion = (int) ((pageActuelle * 100.0) / totalPages);
        }
        this.dateDerniereLecture = LocalDateTime.now();
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

    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }

    public LocalDateTime getDateDerniereLecture() { return dateDerniereLecture; }
    public void setDateDerniereLecture(LocalDateTime dateDerniereLecture) { this.dateDerniereLecture = dateDerniereLecture; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }
}