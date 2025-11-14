package com.example.edugo.dto;

import com.example.edugo.entity.Principales.TypeObjectif;
import java.time.LocalDate;

public class ObjectifRequest {
    private TypeObjectif typeObjectif;
    private int nbreLivre;
    private LocalDate dateEnvoie;

    // Constructeurs
    public ObjectifRequest() {}

    public ObjectifRequest(TypeObjectif typeObjectif, int nbreLivre, LocalDate dateEnvoie) {
        this.typeObjectif = typeObjectif;
        this.nbreLivre = nbreLivre;
        this.dateEnvoie = dateEnvoie;
    }

    // Getters & Setters
    public TypeObjectif getTypeObjectif() { return typeObjectif; }
    public void setTypeObjectif(TypeObjectif typeObjectif) { this.typeObjectif = typeObjectif; }

    public int getNbreLivre() { return nbreLivre; }
    public void setNbreLivre(int nbreLivre) { this.nbreLivre = nbreLivre; }

    public LocalDate getDateEnvoie() { return dateEnvoie; }
    public void setDateEnvoie(LocalDate dateEnvoie) { this.dateEnvoie = dateEnvoie; }
}