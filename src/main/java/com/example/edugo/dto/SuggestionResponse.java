package com.example.edugo.dto;
import java.time.LocalDateTime;

public class SuggestionResponse {
    private Long id;
    private String contenu;
    private LocalDateTime dateEnvoie;
    private EleveSimpleResponse eleve;

    // CONSTRUCTEUR avec 4 paramètres dans le bon ordre
    public SuggestionResponse(Long id, String contenu, LocalDateTime dateEnvoie, EleveSimpleResponse eleve) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoie = dateEnvoie;
        this.eleve = eleve;
    }

    // Getters et setters...
}



