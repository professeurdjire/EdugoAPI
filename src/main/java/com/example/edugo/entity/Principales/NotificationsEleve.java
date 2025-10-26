package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications_eleves")
public class NotificationsEleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne(optional = false)
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Column(name = "est_lu")
    private Boolean estLu = false;

    @PrePersist
    protected void onCreate() {
        dateEnvoi = LocalDateTime.now();
    }

    // Constructeurs
    public NotificationsEleve() {}

    public NotificationsEleve(Notification notification, Eleve eleve) {
        this.notification = notification;
        this.eleve = eleve;
        this.dateEnvoi = LocalDateTime.now();
        this.estLu = false;
    }

    // Getters et Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Notification getNotification() { return notification; }
    public void setNotification(Notification notification) { this.notification = notification; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public Boolean getEstLu() { return estLu; }
    public void setEstLu(Boolean estLu) { this.estLu = estLu; }
}
