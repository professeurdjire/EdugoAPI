package com.example.edugo.dto;

import java.time.LocalDateTime;

public class SuggestionResponse {
    private Long id;
    private String contenu;
    private LocalDateTime dateEnvoie;
    private EleveSimpleResponse eleve;

    // CONSTRUCTEUR SANS ARGUMENT OBLIGATOIRE
    public SuggestionResponse() {
    }

    // Votre constructeur existant
    public SuggestionResponse(Long id, String contenu, LocalDateTime dateEnvoie, EleveSimpleResponse eleve) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoie = dateEnvoie;
        this.eleve = eleve;
    }

    // GETTERS ET SETTERS POUR TOUS LES CHAMPS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoie() { return dateEnvoie; }
    public void setDateEnvoie(LocalDateTime dateEnvoie) { this.dateEnvoie = dateEnvoie; }

    public EleveSimpleResponse getEleve() { return eleve; }
    public void setEleve(EleveSimpleResponse eleve) { this.eleve = eleve; }
}