package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defis")
public class Defi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String ennonce;

    private LocalDateTime dateAjout;

    private String reponseDefi;

    private int pointDefi;

    private int nbreParticipations;

    private String typeDefi;

    // Relation entre défi et  une seule classe
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    // Relation entre défi et  élève
    @OneToMany(mappedBy = "defi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EleveDefi> eleveDefis = new ArrayList<>();

    // --- Constructeurs ---
    public Defi() {}

    public Defi(String titre, String ennonce, int pointDefi) {
        this.titre = titre;
        this.ennonce = ennonce;
        this.pointDefi = pointDefi;
        this.dateAjout = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getEnnonce() { return ennonce; }
    public void setEnnonce(String ennonce) { this.ennonce = ennonce; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    public String getReponseDefi() { return reponseDefi; }
    public void setReponseDefi(String reponseDefi) { this.reponseDefi = reponseDefi; }

    public int getPointDefi() { return pointDefi; }
    public void setPointDefi(int pointDefi) { this.pointDefi = pointDefi; }

    public int getNbreParticipations() { return nbreParticipations; }
    public void setNbreParticipations(int nbreParticipations) { this.nbreParticipations = nbreParticipations; }

    public String getTypeDefi() { return typeDefi; }
    public void setTypeDefi(String typeDefi) { this.typeDefi = typeDefi; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public List<EleveDefi> getEleveDefis() { return eleveDefis; }
    public void setEleveDefis(List<EleveDefi> eleveDefis) { this.eleveDefis = eleveDefis; }
}
