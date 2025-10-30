package com.example.edugo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recompense")
@Data
@NoArgsConstructor
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "type_recompense")
    private String typeRecompense; // DATA, BADGE, CERTIFICAT

    @Column(name = "valeur")
    private String valeur;

    @Column(name = "unite")
    private String unite;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "statut")
    private String statut; // ACTIF, INACTIF

    @Column(name = "date_creation")
    private java.util.Date dateCreation;

    @Column(name = "date_expiration")
    private java.util.Date dateExpiration;

    @Column(name = "nombre_distribue")
    private Integer nombreDistribue;

    @Column(name = "nombre_disponible")
    private Integer nombreDisponible;
}
