package com.example.edugo.entity.Principales;

import com.example.edugo.entity.FichierLivre;
import com.example.edugo.entity.Langue;
import com.example.edugo.entity.Tag;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livres")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(unique = true)
    private String isbn;

    @Column(length = 1000)
    private String description;

    @Column(name = "annee_publication")
    private Integer anneePublication;

    private String editeur;

    private String auteur;

    @Column(name = "image_couverture")
    private String imageCouverture;

    @Column(name = "lecture_auto")
    private Boolean lectureAuto = false;

    @Column(name = "interactif")
    private Boolean interactif = false;

    @ManyToOne
    @JoinColumn(name = "niveau_id")
    private Niveau niveau;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "langue_id")
    private Langue langue;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL)
    private List<FichierLivre> fichiers = new ArrayList<>();

    // --- Relation OneToOne avec Quiz ---
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL)
    private List<Progression> progressions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "livre_tags",
            joinColumns = @JoinColumn(name = "livre_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // Constructeurs
    public Livre() {}

    public Livre(String titre, String isbn, String description) {
        this.titre = titre;
        this.isbn = isbn;
        this.description = description;
    }

    // Méthodes utilitaires
    public void ajouterFichier(FichierLivre fichier) {
        fichiers.add(fichier);
        fichier.setLivre(this);
    }

    public void supprimerFichier(FichierLivre fichier) {
        fichiers.remove(fichier);
        fichier.setLivre(null);
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getAnneePublication() { return anneePublication; }
    public void setAnneePublication(Integer anneePublication) { this.anneePublication = anneePublication; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getImageCouverture() { return imageCouverture; }
    public void setImageCouverture(String imageCouverture) { this.imageCouverture = imageCouverture; }

    public Boolean getLectureAuto() { return lectureAuto; }
    public void setLectureAuto(Boolean lectureAuto) { this.lectureAuto = lectureAuto; }

    public Boolean getInteractif() { return interactif; }
    public void setInteractif(Boolean interactif) { this.interactif = interactif; }

    public Niveau getNiveau() { return niveau; }
    public void setNiveau(Niveau niveau) { this.niveau = niveau; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }

    public Langue getLangue() { return langue; }
    public void setLangue(Langue langue) { this.langue = langue; }


    public List<FichierLivre> getFichiers() { return fichiers; }
    public void setFichiers(List<FichierLivre> fichiers) { this.fichiers = fichiers; }

    public List<Progression> getProgressions() { return progressions; }
    public void setProgressions(List<Progression> progressions) { this.progressions = progressions; }

    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}