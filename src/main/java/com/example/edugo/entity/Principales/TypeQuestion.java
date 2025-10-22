package com.example.edugo.entity.Principales;

import com.example.edugo.entity.QuestionDefi;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Type_questions")
public class TypeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelleType;

    // --- RELATION : un type peut concerner plusieurs questions ---
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "type-questions")
    private List<Question> questions;

    // --- CONSTRUCTEURS ---

    public TypeQuestion() {}

    public TypeQuestion(String libelleType) {
        this.libelleType = libelleType;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleType() {
        return libelleType;
    }

    public void setLibelleType(String libelleType) {
        this.libelleType = libelleType;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Entity
    @Table(name = "defis")
    public static class Defi {
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

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TypeDefi typeDefi;

        @ManyToOne
        @JoinColumn(name = "eleve_id")
        private Eleve eleve;

        @OneToMany(mappedBy = "defi", cascade = CascadeType.ALL)
        private List<QuestionDefi> questions = new ArrayList<>();

        // Constructeurs
        public Defi() {}

        public Defi(String titre, String description, LocalDateTime dateDebut, LocalDateTime dateFin) {
            this.titre = titre;
            this.description = description;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }

        // Méthodes utilitaires
        public boolean estEnCours() {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(dateDebut) && now.isBefore(dateFin);
        }

        public boolean estTermine() {
            return LocalDateTime.now().isAfter(dateFin);
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

        public Eleve getEleve() { return eleve; }
        public void setEleve(Eleve eleve) { this.eleve = eleve; }

        public List<QuestionDefi> getQuestions() { return questions; }
        public void setQuestions(List<QuestionDefi> questions) { this.questions = questions; }
    }
}
