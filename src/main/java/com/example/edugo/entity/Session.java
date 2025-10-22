package com.example.edugo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_utilisateur", nullable = false)
    private Long idUtilisateur;

    @Column(name = "token_session", nullable = false)
    private String tokenSession;

    @Column(name = "ip_adresse")
    private String ipAdresse;

    @Column(name = "agent_user")
    private String agentUser;

    @Column(name = "date_creation")
    private java.util.Date dateCreation;

    @Column(name = "date_expiration")
    private java.util.Date dateExpiration;

    @Column(name = "statut")
    private String statut; // ACTIVE, EXPIRE, INVALIDE
}
