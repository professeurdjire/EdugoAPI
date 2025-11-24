package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressionResponse {
    private Long id;
    private Long eleveId;
    private String eleveNom;
    private Long livreId;
    private String livreTitre;
    private Integer pageActuelle;
    private Integer pourcentageCompletion;
    private LocalDateTime dateMiseAJour;

    public ProgressionResponse() {
    }

    public ProgressionResponse(Long id, Long eleveId, String eleveNom, Long livreId, String livreTitre, Integer pageActuelle, Integer pourcentageCompletion, LocalDateTime dateMiseAJour) {
        this.id = id;
        this.eleveId = eleveId;
        this.eleveNom = eleveNom;
        this.livreId = livreId;
        this.livreTitre = livreTitre;
        this.pageActuelle = pageActuelle;
        this.pourcentageCompletion = pourcentageCompletion;
        this.dateMiseAJour = dateMiseAJour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEleveId() {
        return eleveId;
    }

    public void setEleveId(Long eleveId) {
        this.eleveId = eleveId;
    }

    public String getEleveNom() {
        return eleveNom;
    }

    public void setEleveNom(String eleveNom) {
        this.eleveNom = eleveNom;
    }

    public Long getLivreId() {
        return livreId;
    }

    public void setLivreId(Long livreId) {
        this.livreId = livreId;
    }

    public String getLivreTitre() {
        return livreTitre;
    }

    public void setLivreTitre(String livreTitre) {
        this.livreTitre = livreTitre;
    }

    public Integer getPageActuelle() {
        return pageActuelle;
    }

    public void setPageActuelle(Integer pageActuelle) {
        this.pageActuelle = pageActuelle;
    }

    public Integer getPourcentageCompletion() {
        return pourcentageCompletion;
    }

    public void setPourcentageCompletion(Integer pourcentageCompletion) {
        this.pourcentageCompletion = pourcentageCompletion;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    @Override
    public String toString() {
        return "ProgressionResponse{" +
                "id=" + id +
                ", eleveId=" + eleveId +
                ", eleveNom='" + eleveNom + '\'' +
                ", livreId=" + livreId +
                ", livreTitre='" + livreTitre + '\'' +
                ", pageActuelle=" + pageActuelle +
                ", pourcentageCompletion=" + pourcentageCompletion +
                ", dateMiseAJour=" + dateMiseAJour +
                '}';
    }
}
