package com.example.edugo.entity;

import com.example.edugo.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "admins")
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    public Admin() {
        this.setRole(Role.ADMIN);
    }

    // Méthodes spécifiques à l'admin
    // gererUtilisateur(), ajouterContenu(), etc.
}