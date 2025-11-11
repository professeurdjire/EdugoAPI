package com.example.edugo.dto;

public class StatistiquesBadgeResponse {
    private Long badgeId;
    private String nomBadge;
    private String description;
    private String type;
    private String icone;
    private Integer nombreAttributions;

    public StatistiquesBadgeResponse() {}
    public StatistiquesBadgeResponse(Long badgeId, String nomBadge, String description, String type, String icone, Integer nombreAttributions) {
        this.badgeId = badgeId;
        this.nomBadge = nomBadge;
        this.description = description;
        this.type = type;
        this.icone = icone;
        this.nombreAttributions = nombreAttributions;
    }
    public Long getBadgeId() { return badgeId; }
    public void setBadgeId(Long badgeId) { this.badgeId = badgeId; }
    public String getNomBadge() { return nomBadge; }
    public void setNomBadge(String nomBadge) { this.nomBadge = nomBadge; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
    public Integer getNombreAttributions() { return nombreAttributions; }
    public void setNombreAttributions(Integer nombreAttributions) { this.nombreAttributions = nombreAttributions; }
}
