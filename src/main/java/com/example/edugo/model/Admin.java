package com.example.edugo.model;

import com.example.edugo.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
    @Column(name = "admin_level")
    private String adminLevel;

    @Column(name = "can_manage_users")
    private Boolean canManageUsers = true;

    @Column(name = "can_manage_content")
    private Boolean canManageContent = true;

    // Constructeurs
    public Admin() {}

    public Admin(String email, String password, String firstName, String lastName, String adminLevel) {
        super(email, password, firstName, lastName);
        this.adminLevel = adminLevel;
    }

    // Getters et Setters
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }

    public Boolean getCanManageUsers() { return canManageUsers; }
    public void setCanManageUsers(Boolean canManageUsers) { this.canManageUsers = canManageUsers; }

    public Boolean getCanManageContent() { return canManageContent; }
    public void setCanManageContent(Boolean canManageContent) { this.canManageContent = canManageContent; }
}