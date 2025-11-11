package com.example.edugo.dto;

import java.time.LocalDateTime;

public class PartenaireResponse {
    private Long id;
    private String nom;
    private String description;
    private String logoUrl;
    private String siteWeb;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Boolean actif;

    public PartenaireResponse() {}

    public PartenaireResponse(Long id, String nom, String description, String logoUrl, 
                             String siteWeb, LocalDateTime dateCreation, 
                             LocalDateTime dateModification, Boolean actif) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.logoUrl = logoUrl;
        this.siteWeb = siteWeb;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.actif = actif;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}