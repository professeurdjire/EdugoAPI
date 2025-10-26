package com.example.edugo.dto;

import com.example.edugo.entity.Principales.TypeBadge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BadgeRequest {
    private Long id;

    @NotBlank(message = "Le nom du badge est obligatoire")
    private String nom;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le type du badge est obligatoire")
    private TypeBadge type;

    @NotBlank(message = "L'icône est obligatoire")
    private String icone;

    // Constructeurs
    public BadgeRequest() {}

    public BadgeRequest(Long id, String nom, String description, TypeBadge type, String icone) {
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

    public TypeBadge getType() {
        return type;
    }

    public void setType(TypeBadge type) {
        this.type = type;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

}
