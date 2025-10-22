package com.example.edugo.entity.Principales;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "id_niveau", nullable = false)
    private Niveau niveauScolaire;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "exercice-questions")
    private List<Question> questionsExercice;


    // Constructeur
    public Exercice(String titre, String description, String contenu, Integer niveauDifficulte, Integer tempsAlloue, Boolean active, Matiere matiere, Niveau niveauScolaire) {
        this.titre = titre;
        this.description = description;
        this.niveauDifficulte = niveauDifficulte;
        this.tempsAlloue = tempsAlloue;
        this.active = active;
        this.matiere = matiere;
        this.niveauScolaire = niveauScolaire;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getNiveauDifficulte() { return niveauDifficulte; }
    public void setNiveauDifficulte(Integer niveauDifficulte) {
        this.niveauDifficulte = niveauDifficulte;}

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Integer getTempsAlloue() {
        return tempsAlloue;
    }

    public void setTempsAlloue(Integer tempsAlloue) {
        this.tempsAlloue = tempsAlloue;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Niveau getNiveauScolaire() {
        return niveauScolaire;
    }

    public void setNiveauScolaire(Niveau niveauScolaire) {
        this.niveauScolaire = niveauScolaire;
    }

    public List<Question> getQuestionsExercice() {
        return questionsExercice;
    }

    public void setQuestionsExercice(List<Question> questionsExercice) {
        this.questionsExercice = questionsExercice;
    }
}
