package com.example.edugo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "admins")
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    @Column(name = "telephone")
    private String telephone;

    public Admin() {
        this.setRole(Role.ADMIN);
    }

    // Méthodes spécifiques à l'admin
    // gererUtilisateur(), ajouterContenu(), etc.
}