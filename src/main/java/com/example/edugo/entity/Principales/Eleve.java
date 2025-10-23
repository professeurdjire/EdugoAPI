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

    private Integer pointAccumule;

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Progression> progressions = new ArrayList<>();

    // Relation avec EleveDefi
    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EleveDefi> eleveDefis = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaireExercice> faireExercices;
    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponseUtilisateur> reponsesUtilisateurs = new ArrayList<>();

    // Constructeurs
    public Eleve() {}

    public Eleve(String email, String password, String firstName, String lastName, String dateNaissance, Classe classe, Integer pointAccumule) {
        super(email, password, firstName, lastName);
        this.dateNaissance = dateNaissance;
        this.classe = classe;
        this.pointAccumule = pointAccumule;

    }

    // Getters et Setters
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public Integer getPointAccumule() { return pointAccumule; }
    public void setPointAccumule(Integer pointAccumule) {this.pointAccumule = pointAccumule;}

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }

    public List<Progression> getProgressions() { return progressions; }
    public void setProgressions(List<Progression> progressions) { this.progressions = progressions; }

    public List<EleveDefi> getEleveDefis() {return eleveDefis;}
    public void setEleveDefis(List<EleveDefi> eleveDefis) {this.eleveDefis = eleveDefis;}

    public List<FaireExercice> getFaireExercices() {return faireExercices;}
    public void setFaireExercices(List<FaireExercice> faireExercices) {this.faireExercices = faireExercices;}

    public List<ReponseUtilisateur> getReponsesUtilisateurs() {return reponsesUtilisateurs;}
    public void setReponsesUtilisateurs(List<ReponseUtilisateur> reponsesUtilisateurs) {this.reponsesUtilisateurs = reponsesUtilisateurs;}

    @Override
    public String getPassword() {
        return "";
    }
}