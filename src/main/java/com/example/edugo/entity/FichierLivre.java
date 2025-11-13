package com.example.edugo.entity;

import com.example.edugo.entity.Principales.Livre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "fichiers_livre")
public class FichierLivre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "chemin_fichier", nullable = false)
    private String cheminFichier;

    @Enumerated(EnumType.STRING)
    private TypeFichier type;

    private Long taille; // en bytes

    @Column(name = "format")
    private String format;

    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    @JsonBackReference(value = "livre-fichiers")
    private Livre livre;

    // Enum pour le type de fichier
    public enum TypeFichier {
        PDF, EPUB, IMAGE, VIDEO, AUDIO
    }

    // Constructeurs
    public FichierLivre() {}

    public FichierLivre(String nom, String cheminFichier, TypeFichier type, Livre livre) {
        this.nom = nom;
        this.cheminFichier = cheminFichier;
        this.type = type;
        this.livre = livre;
    }

    // Méthodes utilitaires
    public String getTailleFormattee() {
        if (taille == null) return "0 B";

        if (taille < 1024) return taille + " B";
        else if (taille < 1024 * 1024) return String.format("%.1f KB", taille / 1024.0);
        else return String.format("%.1f MB", taille / (1024.0 * 1024.0));
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public TypeFichier getType() { return type; }
    public void setType(TypeFichier type) { this.type = type; }

    public Long getTaille() { return taille; }
    public void setTaille(Long taille) { this.taille = taille; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }
}