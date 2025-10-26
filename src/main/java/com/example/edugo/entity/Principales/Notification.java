package com.example.edugo.entity.Principales;

import com.example.edugo.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime dateAjout;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationsEleve> notificationsEleves = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
    }

    // Constructeurs
    public Notification() {}

    public Notification(String titre, String message) {
        this.titre = titre;
        this.message = message;
    }



    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    public List<NotificationsEleve> getNotificationsEleves() {
        return notificationsEleves;
    }

    public void setNotificationsEleves(List<NotificationsEleve> notificationsEleves) {
        this.notificationsEleves = notificationsEleves;
    }
}