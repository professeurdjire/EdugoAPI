package com.example.edugo.dto;

public class LivrePopulaireResponse {
    private Long livreId;
    private String titre;
    private String auteur;
    private Long nombreLecteurs;

    public LivrePopulaireResponse() {}

    public LivrePopulaireResponse(Long livreId, String titre, String auteur, Long nombreLecteurs) {
        this.livreId = livreId;
        this.titre = titre;
        this.auteur = auteur;
        this.nombreLecteurs = nombreLecteurs;
    }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public Long getNombreLecteurs() { return nombreLecteurs; }
    public void setNombreLecteurs(Long nombreLecteurs) { this.nombreLecteurs = nombreLecteurs; }
}
