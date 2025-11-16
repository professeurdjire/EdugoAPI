package com.example.edugo.dto;

public class PartenaireRequest {
    private String nom;
    private String description;
    private String logoUrl;
    private String siteWeb;
    private Boolean actif = true;
    private String domaine;
    private String type;
    private String email;
    private String telephone;
    private String pays;
    private Boolean newsletter = false;

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

    public String getDomaine() { return domaine; }
    public void setDomaine(String domaine) { this.domaine = domaine; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public Boolean getNewsletter() { return newsletter; }
    public void setNewsletter(Boolean newsletter) { this.newsletter = newsletter; }
}