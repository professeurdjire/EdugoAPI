package com.example.edugo.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercice")
@Data
@NoArgsConstructor
public class Exercice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String contenu;

    @Column(nullable = false)
    private Integer niveauDifficulte;

    @Column(nullable = false)
    private Integer tempsAlloue;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @ManyToOne
    @JoinColumn(name = "id_matiere", nullable = false)
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "id_niveau_scolaire", nullable = false)
    private NiveauScolaire niveauScolaire;

    // Constructeur
    public Exercice(String titre, String description, String contenu, Integer niveauDifficulte, Integer tempsAlloue, Boolean active, Matiere matiere, NiveauScolaire niveauScolaire) {
        this.titre = titre;
        this.description = description;
        this.contenu = contenu;
        this.niveauDifficulte = niveauDifficulte;
        this.tempsAlloue = tempsAlloue;
        this.active = active;
        this.matiere = matiere;
        this.niveauScolaire = niveauScolaire;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }
}
