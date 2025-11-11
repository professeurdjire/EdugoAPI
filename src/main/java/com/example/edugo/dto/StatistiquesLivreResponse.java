package com.example.edugo.dto;

public class StatistiquesLivreResponse {
    private Long livreId;
    private String titre;
    private String auteur;
    private Integer totalPages;
    private Integer nombreLecteurs;
    private Integer nombreLecteursComplets;
    private Double progressionMoyenne;

    public StatistiquesLivreResponse() {}

    public StatistiquesLivreResponse(Long livreId, String titre, String auteur, Integer totalPages, Integer nombreLecteurs, Integer nombreLecteursComplets, Double progressionMoyenne) {
        this.livreId = livreId;
        this.titre = titre;
        this.auteur = auteur;
        this.totalPages = totalPages;
        this.nombreLecteurs = nombreLecteurs;
        this.nombreLecteursComplets = nombreLecteursComplets;
        this.progressionMoyenne = progressionMoyenne;
    }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public Integer getNombreLecteurs() { return nombreLecteurs; }
    public void setNombreLecteurs(Integer nombreLecteurs) { this.nombreLecteurs = nombreLecteurs; }
    public Integer getNombreLecteursComplets() { return nombreLecteursComplets; }
    public void setNombreLecteursComplets(Integer nombreLecteursComplets) { this.nombreLecteursComplets = nombreLecteursComplets; }
    public Double getProgressionMoyenne() { return progressionMoyenne; }
    public void setProgressionMoyenne(Double progressionMoyenne) { this.progressionMoyenne = progressionMoyenne; }
}
