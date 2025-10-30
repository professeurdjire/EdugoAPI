package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String description;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(name = "reward_mode")
    private String rewardMode;

    @Column(name = "type_challenge")
    private TypeChallenge typeChallenge;

    // Relation optionnelle avec Niveau
    @ManyToOne
    @JoinColumn(name = "niveau_id", nullable = true)
    private Niveau niveau;

    // Relation optionnelle avec Classe
    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = true)
    private Classe classe;

    @ManyToMany
    @JoinTable(
            name = "challenge_badges",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<Question> questionsChallenge = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    // Constructeurs
    public Challenge() {}

    public Challenge(String titre, String description, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Méthodes utilitaires
    public boolean estActif() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dateDebut) && now.isBefore(dateFin);
    }

    // Getters et Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public TypeChallenge getTypeChallenge() {return typeChallenge;}
    public void setTypeChallenge(TypeChallenge typeChallenge) {this.typeChallenge = typeChallenge;}

    public List<Question> getQuestionsChallenge() { return questionsChallenge; }
    public void setQuestionsChallenge(List<Question> questionsChallenge) {this.questionsChallenge = questionsChallenge;}

    public String getRewardMode() { return rewardMode; }
    public void setRewardMode(String rewardMode) { this.rewardMode = rewardMode; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public List<Badge> getRewards() {
        return rewards;
    }

    public void setRewards(List<Badge> rewards) {
        this.rewards = rewards;
    }
}