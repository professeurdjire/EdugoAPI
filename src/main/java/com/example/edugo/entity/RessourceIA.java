package com.example.edugo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ressources_ia")
public class RessourceIA {
    public enum Type { FICHE, QUIZ, RESUME, EXPLICATION }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private com.example.edugo.entity.Principales.Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private com.example.edugo.entity.Principales.Livre livre;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private com.example.edugo.entity.Principales.Matiere matiere;

    private String titre;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Lob
    private String contenu;

    @Column(name = "fichier_path")
    private String fichierPath;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public com.example.edugo.entity.Principales.Eleve getEleve() { return eleve; }
    public void setEleve(com.example.edugo.entity.Principales.Eleve eleve) { this.eleve = eleve; }
    public com.example.edugo.entity.Principales.Livre getLivre() { return livre; }
    public void setLivre(com.example.edugo.entity.Principales.Livre livre) { this.livre = livre; }
    public com.example.edugo.entity.Principales.Matiere getMatiere() { return matiere; }
    public void setMatiere(com.example.edugo.entity.Principales.Matiere matiere) { this.matiere = matiere; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public String getFichierPath() { return fichierPath; }
    public void setFichierPath(String fichierPath) { this.fichierPath = fichierPath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
