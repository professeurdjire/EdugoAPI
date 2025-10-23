package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eleve_defis")
public class EleveDefi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reponseUtilisateur;

    private LocalDateTime dateEnvoie;

    // Relation entre EleveDefi et  élève
    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    // Relation entre EleveDefi et  défi
    @ManyToOne
    @JoinColumn(name = "defi_id")
    private Defi defi;

    // --- Constructeurs ---
    public EleveDefi() {}

    public EleveDefi(Eleve eleve, Defi defi, String reponseUtilisateur) {
        this.eleve = eleve;
        this.defi = defi;
        this.reponseUtilisateur = reponseUtilisateur;
        this.dateEnvoie = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReponseUtilisateur() { return reponseUtilisateur; }
    public void setReponseUtilisateur(String reponseUtilisateur) { this.reponseUtilisateur = reponseUtilisateur; }

    public LocalDateTime getDateEnvoie() { return dateEnvoie; }
    public void setDateEnvoie(LocalDateTime dateEnvoie) { this.dateEnvoie = dateEnvoie; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Defi getDefi() { return defi; }
    public void setDefi(Defi defi) { this.defi = defi; }
}
