package com.example.edugo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tableau_bord")
@Data
@NoArgsConstructor
public class TableauBord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periode")
    private String periode; // JOURNALIER, HEBDOMADAIRE, MENSUEL

    @Column(name = "indicateur")
    private String indicateur; // LECTURES, QUIZZES, DEFIS, RECOMPENSES

    @Column(name = "valeur")
    private Integer valeur;

    @Column(name = "pourcentage_evolution")
    private Double pourcentageEvolution;

    @Column(name = "date_calcule")
    private java.util.Date dateCalcule;

    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @Column(name = "id_type_indicateur")
    private Long idTypeIndicateur;
}
