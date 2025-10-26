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

    private String avatarUrl;

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
    private List<ReponseEleve> reponsesUtilisateurs = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConversionEleve> conversions = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suggestion> suggestions = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationsEleve> notificationsRecues = new ArrayList<>();

    // Constructeurs
    public Eleve() {}

    public Eleve(String email, String motPasse, String prenom, String nom, String dateNaissance, Classe classe, Integer pointAccumule, String avatarUrl) {
        super(email, motPasse, prenom, nom);
        this.dateNaissance = dateNaissance;
        this.classe = classe;
        this.pointAccumule = pointAccumule;
        this.avatarUrl = avatarUrl;

    }

    // Getters et Setters
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public Integer getPointAccumule() { return pointAccumule; }
    public void setPointAccumule(Integer pointAccumule) {this.pointAccumule = pointAccumule;}

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}

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

    public List<NotificationsEleve> getNotificationsRecues() {return notificationsRecues;}
    public void setNotificationsRecues(List<NotificationsEleve> notificationsRecues) {this.notificationsRecues = notificationsRecues;}

    @Override
    public String getPassword() {
        return "";
    }


}