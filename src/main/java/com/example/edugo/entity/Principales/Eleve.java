package com.example.edugo.entity.Principales;

import com.example.edugo.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eleves")
public class Eleve extends User {
    @Column(name = "ville")
    @JsonIgnore
    private String ville;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    @JsonIgnore
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "niveau_id")
    @JsonIgnore
    private Niveau niveau;

    @JsonIgnore
    private Integer pointAccumule;

    @JsonIgnore
    private Integer telephone;


    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Progression> progressions = new ArrayList<>();

    // Relation avec EleveDefi
    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EleveDefi> eleveDefis = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FaireExercice> faireExercices;
    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReponseEleve> reponsesUtilisateurs = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ConversionEleve> conversions = new ArrayList<>();
    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Suggestion> suggestions = new ArrayList<>();

    // Constructeurs
    public Eleve() {}

    public Eleve(String email, String password, String firstName, String lastName, String photoProfil, String ville, Classe classe, Integer pointAccumule, Integer telephone) {
        this.setEmail(email);
        this.setMotDePasse(password);
        this.setNom(lastName);
        this.setPrenom(firstName);
        this.setPhotoProfil(photoProfil);
        this.ville = ville;
        this.telephone = telephone;
        this.classe = classe;
        this.pointAccumule = pointAccumule;
    }

    // Getters et Setters
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

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

    public List<ReponseEleve> getReponsesUtilisateurs() {return reponsesUtilisateurs;}
    public void setReponsesUtilisateurs(List<ReponseEleve> reponsesUtilisateurs) {this.reponsesUtilisateurs = reponsesUtilisateurs;}

    public List<ConversionEleve> getConversions() {return conversions;}
    public void setConversions(List<ConversionEleve> conversions) {this.conversions = conversions;}

    public List<Suggestion> getSuggestions() {return suggestions;}
    public void setSuggestions(List<Suggestion> suggestions) {this.suggestions = suggestions;}

    public Niveau getNiveau() {return niveau;}
    public void setNiveau(Niveau niveau) {this.niveau = niveau;}

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }
}