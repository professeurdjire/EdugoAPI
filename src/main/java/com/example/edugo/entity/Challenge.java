package com.example.edugo.entity;

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

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "reward_mode")
    private String rewardMode;

    @Column(name = "categorie_challenge")
    private TypeChallenge typeChallenge;

    @ManyToMany
    @JoinTable(
            name = "challenge_badges",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<QuestionChallenge> questions = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    // Constructeurs
    public Challenge() {}

    public Challenge(String titre, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.titre = titre;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Méthodes utilitaires
    public boolean estActif() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getRewardMode() { return rewardMode; }
    public void setRewardMode(String rewardMode) { this.rewardMode = rewardMode; }

    public List<Badge> getRewards() { return rewards; }
    public void setRewards(List<Badge> rewards) { this.rewards = rewards; }

    public List<QuestionChallenge> getQuestions() { return questions; }
    public void setQuestions(List<QuestionChallenge> questions) { this.questions = questions; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) { this.participations = participations; }
}