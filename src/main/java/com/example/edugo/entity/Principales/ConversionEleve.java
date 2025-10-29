package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ConversionEleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_option", nullable = false)
    private OptionsConversion option;

    @ManyToOne
    @JoinColumn(name = "id_eleve", nullable = false)
    private Eleve eleve;

    private LocalDateTime dateConversion;

    // --- Constructeurs ---
    public ConversionEleve() {
        this.dateConversion = LocalDateTime.now();
    }

    public ConversionEleve(OptionsConversion option, Eleve eleve) {
        this.option = option;
        this.eleve = eleve;
        this.dateConversion = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionsConversion getOption() {
        return option;
    }

    public void setOption(OptionsConversion option) {
        this.option = option;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public LocalDateTime getDateConversion() {
        return dateConversion;
    }

    public void setDateConversion(LocalDateTime dateConversion) {
        this.dateConversion = dateConversion;
    }

    @Override
    public String toString() {
        return "ConversionEleve{" +
                "id=" + id +
                ", option=" + (option != null ? option.getLibelle() : "null") +
                ", eleve=" + (eleve != null ? eleve.getId() : "null") +
                ", dateConversion=" + dateConversion +
                '}';
    }
}
