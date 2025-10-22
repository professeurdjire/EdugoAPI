package com.example.edugo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "niveau_scolaire")
@Data
@NoArgsConstructor
public class NiveauScolaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    // Constructeur
    public NiveauScolaire(String nom, String description, Boolean active) {
        this.nom = nom;
        this.description = description;
        this.active = active;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNom() {return nom;}

    public void setNom(String nom) {this.nom = nom;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Boolean getActive() {return active;}
    public void setActive(Boolean active) {this.active = active;}
    public LocalDateTime getDateCreation() {return dateCreation;}
    public void setDateCreation(LocalDateTime dateCreation) {this.dateCreation = dateCreation;}
    public LocalDateTime getDateModification() {return dateModification;}
    public void setDateModification(LocalDateTime dateModification) {this.dateModification = dateModification;}
}
