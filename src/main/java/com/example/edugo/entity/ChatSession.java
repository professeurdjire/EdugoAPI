package com.example.edugo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_sessions")
public class ChatSession {
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

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
