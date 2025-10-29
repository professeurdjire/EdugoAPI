package com.example.edugo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historique")
@Data
@NoArgsConstructor
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "description")
    private String description;

    @Column(name = "type_action")
    private String typeAction; // LECTURE, QUIZ, DEFI, EXERCICE, RECOMPENSE

    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @Column(name = "id_objet")
    private Long idObjet;

    @Column(name = "type_objet")
    private String typeObjet;

    @Column(name = "valeur_avant")
    private String valeurAvant;

    @Column(name = "valeur_apres")
    private String valeurApres;

    @Column(name = "date_action")
    private java.util.Date dateAction;

    @Column(name = "ip_adresse")
    private String ipAdresse;

    @Column(name = "agent_user")
    private String agentUser;
}
