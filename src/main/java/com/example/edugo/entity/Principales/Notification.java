package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_eleve", nullable = false)
    private Long idEleve;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String message;

    @Column(name = "date_explication")
    private LocalDateTime dateExplication;

    @Column(name = "est_vu")
    private Boolean estVu = false;

    @PrePersist
    protected void onCreate() {
        dateExplication = LocalDateTime.now();
    }

    // Constructeurs
    public Notification() {}

    public Notification(String titre, String message, Long idEleve) {
        this.titre = titre;
        this.message = message;
        this.idEleve = idEleve;
    }

    // Méthodes utilitaires
    public void marquerCommeVu() {
        this.estVu = true;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdEleve() { return idEleve; }
    public void setIdEleve(Long idEleve) { this.idEleve = idEleve; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateExplication() { return dateExplication; }
    public void setDateExplication(LocalDateTime dateExplication) { this.dateExplication = dateExplication; }

    public Boolean getEstVu() { return estVu; }
    public void setEstVu(Boolean estVu) { this.estVu = estVu; }
}