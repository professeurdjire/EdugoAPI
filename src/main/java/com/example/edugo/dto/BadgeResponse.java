package com.example.edugo.dto;

import com.example.edugo.entity.Principales.TypeBadge;

public class BadgeResponse {
    private Long id;
    private String nom;
    private String description;
    private TypeBadge type;
    private String icone;

    // Constructeurs
    public BadgeResponse() {}

    public BadgeResponse(Long id, String nom, String description,  TypeBadge type, String icone) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.icone = icone;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public TypeBadge getType() {
        return type;
    }

    public void setType(TypeBadge type) {
        this.type = type;
    }
}
