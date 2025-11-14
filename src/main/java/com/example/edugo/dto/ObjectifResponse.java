package com.example.edugo.dto;

import com.example.edugo.entity.Principales.TypeObjectif;
import java.time.LocalDate;

public class ObjectifResponse {
    private Long idObjectif;
    private TypeObjectif typeObjectif;
    private int nbreLivre;
    private LocalDate dateEnvoie;
    private LocalDate dateFin;
    private double progression;
    private long joursRestants;
    private int livresLus;
    private String statut;
    private boolean estEnCours;

    // Constructeurs
    public ObjectifResponse() {}

    public ObjectifResponse(Long idObjectif, TypeObjectif typeObjectif, int nbreLivre,
                            LocalDate dateEnvoie, LocalDate dateFin, double progression,
                            long joursRestants, int livresLus, String statut) {
        this.idObjectif = idObjectif;
        this.typeObjectif = typeObjectif;
        this.nbreLivre = nbreLivre;
        this.dateEnvoie = dateEnvoie;
        this.dateFin = dateFin;
        this.progression = progression;
        this.joursRestants = joursRestants;
        this.livresLus = livresLus;
        this.statut = statut;
        this.estEnCours = "EN_COURS".equals(statut);
    }

    // Getters & Setters (COMPLETS et CORRECTS)
    public Long getIdObjectif() {
        return idObjectif;
    }
    public void setIdObjectif(Long idObjectif) {
        this.idObjectif = idObjectif;
    }

    public TypeObjectif getTypeObjectif() {
        return typeObjectif;
    }
    public void setTypeObjectif(TypeObjectif typeObjectif) {
        this.typeObjectif = typeObjectif;
    }

    public int getNbreLivre() {
        return nbreLivre;
    }
    public void setNbreLivre(int nbreLivre) {
        this.nbreLivre = nbreLivre;
    }

    public LocalDate getDateEnvoie() {
        return dateEnvoie;
    }
    public void setDateEnvoie(LocalDate dateEnvoie) {
        this.dateEnvoie = dateEnvoie;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public double getProgression() {
        return progression;
    }
    public void setProgression(double progression) {
        this.progression = progression;
    }

    public long getJoursRestants() {
        return joursRestants;
    }
    public void setJoursRestants(long joursRestants) {
        this.joursRestants = joursRestants;
    }

    public int getLivresLus() {
        return livresLus;
    }
    public void setLivresLus(int livresLus) {
        this.livresLus = livresLus;
    }

    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
        this.estEnCours = "EN_COURS".equals(statut);
    }

    public boolean isEstEnCours() {
        return estEnCours;
    }
    public void setEstEnCours(boolean estEnCours) {
        this.estEnCours = estEnCours;
    }


}