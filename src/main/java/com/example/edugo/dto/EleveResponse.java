package com.example.edugo.dto;

public class EleveResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String dateNaissance;
    private String classeNom; // On peut renvoyer le nom de la classe pour le front
    private Integer pointAccumule;
    private String avatarUrl;

    // Constructeurs
    public EleveResponse() {}

    public EleveResponse(Long id, String email, String firstName, String lastName,
                         String dateNaissance, String classeNom, Integer pointAccumule, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateNaissance = dateNaissance;
        this.classeNom = classeNom;
        this.pointAccumule = pointAccumule;
        this.avatarUrl = avatarUrl;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getClasseNom() { return classeNom; }
    public void setClasseNom(String classeNom) { this.classeNom = classeNom; }

    public Integer getPointAccumule() { return pointAccumule; }
    public void setPointAccumule(Integer pointAccumule) { this.pointAccumule = pointAccumule; }

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}
}
