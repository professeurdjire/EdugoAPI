package com.example.edugo.entity.Principales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suggestion")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    private LocalDateTime dateEnvoie;

    @ManyToOne
    @JoinColumn(name = "id_eleve", nullable = false)
    private Eleve eleve;

    // --- Constructeurs ---
    public Suggestion() {
        this.dateEnvoie = LocalDateTime.now(); // date automatique à la création
    }

    public Suggestion(String contenu, Eleve eleve) {
        this.contenu = contenu;
        this.eleve = eleve;
        this.dateEnvoie = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoie() {
        return dateEnvoie;
    }

    public void setDateEnvoie(LocalDateTime dateEnvoie) {
        this.dateEnvoie = dateEnvoie;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

}
