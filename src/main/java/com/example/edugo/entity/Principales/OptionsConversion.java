package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "optionsConversion")
public class OptionsConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    private Boolean etat;

    private Integer nbrePoint;

    private LocalDateTime dateAjout;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ConversionEleve> conversions = new ArrayList<>();

    // --- Constructeurs ---

    public OptionsConversion() {
        this.dateAjout = LocalDateTime.now(); // date par défaut à la création
    }

    public OptionsConversion(String libelle, Boolean etat, Integer nbrePoint) {
        this.libelle = libelle;
        this.etat = etat;
        this.nbrePoint = nbrePoint;
        this.dateAjout = LocalDateTime.now();
    }

    // --- Getters et Setters ---

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getLibelle() {return libelle;}
    public void setLibelle(String libelle) {this.libelle = libelle;}

    public Boolean getEtat() {return etat;}
    public void setEtat(Boolean etat) {this.etat = etat;}

    public Integer getNbrePoint() {return nbrePoint;}
    public void setNbrePoint(Integer nbrePoint) {this.nbrePoint = nbrePoint;}

    public LocalDateTime getDateAjout() {return dateAjout;}
    public void setDateAjout(LocalDateTime dateAjout) {this.dateAjout = dateAjout;}

    public List<ConversionEleve> getConversions() {return conversions;}
    public void setConversions(List<ConversionEleve> conversions) {this.conversions = conversions;}
}
