package com.example.edugo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametre")
@Data
@NoArgsConstructor
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "valeur", nullable = false)
    private String valeur;

    @Column(name = "description")
    private String description;

    @Column(name = "type_parametre")
    private String typeParametre; // GENERAL, SECURITE, PERFORMANCE, GAMIFICATION

    @Column(name = "statut")
    private String statut; // ACTIF, INACTIF

    @Column(name = "date_creation")
    private java.util.Date dateCreation;

    @Column(name = "date_modification")
    private java.util.Date dateModification;

}
