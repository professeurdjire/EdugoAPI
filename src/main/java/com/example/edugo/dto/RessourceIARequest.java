package com.example.edugo.dto;

public class RessourceIARequest {
    private Long eleveId;    // required
    private Long livreId;    // optional
    private Long matiereId;  // optional
    private String titre;    // optional (autogénéré si null)
    private String type;     // FICHE, QUIZ, RESUME, EXPLICATION
    private String prompt;   // consigne utilisateur

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }
    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
