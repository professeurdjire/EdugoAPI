package com.example.edugo.dto;

import java.time.LocalDateTime;

public class PartenaireResponse {
    private Long id;
    private String nom;
    private String description;
    private String logoUrl;
    private String siteWeb;
    private String domaine;
    private String type;
    private String email;
    private String telephone;
    private String pays;
    private String statut;
    private String dateAjout;
    private Boolean newsletter;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Boolean actif;

    public PartenaireResponse() {}

    public PartenaireResponse(Long id, String nom, String description, String logoUrl,
                             String siteWeb, String domaine, String type,
                             String email, String telephone, String pays,
                             String statut, String dateAjout, Boolean newsletter,
                             LocalDateTime dateCreation,
                             LocalDateTime dateModification, Boolean actif) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.logoUrl = logoUrl;
        this.siteWeb = siteWeb;
        this.domaine = domaine;
        this.type = type;
        this.email = email;
        this.telephone = telephone;
        this.pays = pays;
        this.statut = statut;
        this.dateAjout = dateAjout;
        this.newsletter = newsletter;
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

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getDateAjout() { return dateAjout; }
    public void setDateAjout(String dateAjout) { this.dateAjout = dateAjout; }

    public Boolean getNewsletter() { return newsletter; }
    public void setNewsletter(Boolean newsletter) { this.newsletter = newsletter; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}