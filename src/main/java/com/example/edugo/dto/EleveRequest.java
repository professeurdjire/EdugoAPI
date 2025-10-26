package com.example.edugo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EleveRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "La date de naissance est obligatoire")
    private String dateNaissance;

    @NotNull(message = "L'identifiant de la classe est obligatoire")
    private Long classeId;

    private Integer pointAccumule;

    private String avatarUrl;

    // Constructeurs
    public EleveRequest() {}

    public EleveRequest(String email, String password, String firstName, String lastName,
                        String dateNaissance, Long classeId, Integer pointAccumule, String avatarUrl) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateNaissance = dateNaissance;
        this.classeId = classeId;
        this.pointAccumule = pointAccumule;
        this.avatarUrl = avatarUrl;
    }

    // Getters et Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }

    public Integer getPointAccumule() { return pointAccumule; }
    public void setPointAccumule(Integer pointAccumule) { this.pointAccumule = pointAccumule; }

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}
}
