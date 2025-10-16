package com.example.edugo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String message;

    @Column(name = "date_notification")
    private LocalDateTime dateNotification;

    @Column(name = "lu")
    private Boolean lu = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        dateNotification = LocalDateTime.now();
    }

    // Constructeurs
    public Notification() {}

    public Notification(String titre, String message, User user) {
        this.titre = titre;
        this.message = message;
        this.user = user;
    }

    // Méthodes utilitaires
    public void marquerCommeLu() {
        this.lu = true;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateNotification() { return dateNotification; }
    public void setDateNotification(LocalDateTime dateNotification) { this.dateNotification = dateNotification; }

    public Boolean getLu() { return lu; }
    public void setLu(Boolean lu) { this.lu = lu; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}