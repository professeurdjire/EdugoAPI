package com.example.edugo.dto;

public class PartenaireRequest {
    private String nom;
    private String description;
    private String logoUrl;
    private String siteWeb;
    private Boolean actif = true;

    public PartenaireRequest() {}

    public PartenaireRequest(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}