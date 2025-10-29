package com.example.edugo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type_statistique")
@Data
@NoArgsConstructor
public class TypeStatistique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "unite")
    private String unite;

    @Column(name = "type_calcul")
    private String typeCalcul; // CUMULATIF, MOYENNE, MAXIMUM, MINIMUM

    @Column(name = "statut")
    private String statut; // ACTIF, INACTIF

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;
}
