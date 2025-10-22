package com.example.edugo.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "suggestions")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String description;

    private LocalDateTime date;
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, APPROUVÉ, REJETÉ
    private Integer note; // 1-5

    // === RELATIONS ===

    // Utilisateur qui a fait la suggestion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User utilisateur;

    // Classe concernée
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classe_id")
    private Classe classe;
}
