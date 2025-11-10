package com.example.edugo.dto;

public class BadgeResponse {
    private Long id;
    private String nom;
    private String description;
    private String type;
    private String icone;

    public BadgeResponse() {}
    public BadgeResponse(Long id, String nom, String description, String type, String icone) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.icone = icone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    @Override
    public String toString() {
        return "BadgeResponse{" + "id=" + id + ", nom='" + nom + '\'' + ", description='" + description + '\'' + ", type='" + type + '\'' + ", icone='" + icone + '\'' + '}';
    }
}
