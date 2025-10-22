package com.example.edugo.entity.Principales;

import com.example.edugo.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eleves")
public class Eleve extends User {
    @Column(name = "date_naissance")
    private String dateNaissance;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Progression> progressions = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Defi> defis = new ArrayList<>();

    // Constructeurs
    public Eleve() {}

    public Eleve(String email, String password, String firstName, String lastName, String dateNaissance) {
        super(email, password, firstName, lastName);
        this.dateNaissance = dateNaissance;
    }

    // Getters et Setters
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }

    public List<Progression> getProgressions() { return progressions; }
    public void setProgressions(List<Progression> progressions) { this.progressions = progressions; }

    public List<Defi> getDefis() { return defis; }
    public void setDefis(List<Defi> defis) { this.defis = defis; }
}