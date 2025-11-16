package com.example.edugo.dto;

public class QuizCreateRequest {
    private Long livreId;
    private String titre;

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
}
