package com.example.edugo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EleveProfileResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String photoProfil;
    private Integer telephone;
    private String ville;
    private Long classeId;
    private String classeNom;
    private Long niveauId;
    private String niveauNom;
    private Integer pointAccumule;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public String getClasseNom() {
        return classeNom;
    }

    public void setClasseNom(String classeNom) {
        this.classeNom = classeNom;
    }

    public Long getNiveauId() {
        return niveauId;
    }

    public void setNiveauId(Long niveauId) {
        this.niveauId = niveauId;
    }

    public String getNiveauNom() {
        return niveauNom;
    }

    public void setNiveauNom(String niveauNom) {
        this.niveauNom = niveauNom;
    }

    public Integer getPointAccumule() {
        return pointAccumule;
    }

    public void setPointAccumule(Integer pointAccumule) {
        this.pointAccumule = pointAccumule;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}



