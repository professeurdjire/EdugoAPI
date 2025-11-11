package com.example.edugo.dto;

public class LivreRequest {
    private String titre;
    private String isbn;
    private String description;
    private Integer anneePublication;
    private String editeur;
    private String auteur;
    private Integer totalPages;
    private String imageCouverture;
    private Boolean lectureAuto;
    private Boolean interactif;
    private Long niveauId;
    private Long classeId;
    private Long matiereId;
    private Long langueId;

    public LivreRequest() {}

    public LivreRequest(String titre, String isbn, String description, Integer anneePublication, String editeur, String auteur, Integer totalPages, String imageCouverture, Boolean lectureAuto, Boolean interactif, Long niveauId, Long classeId, Long matiereId, Long langueId) {
        this.titre = titre;
        this.isbn = isbn;
        this.description = description;
        this.anneePublication = anneePublication;
        this.editeur = editeur;
        this.auteur = auteur;
        this.totalPages = totalPages;
        this.imageCouverture = imageCouverture;
        this.lectureAuto = lectureAuto;
        this.interactif = interactif;
        this.niveauId = niveauId;
        this.classeId = classeId;
        this.matiereId = matiereId;
        this.langueId = langueId;
    }

    // getters and setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getAnneePublication() { return anneePublication; }
    public void setAnneePublication(Integer anneePublication) { this.anneePublication = anneePublication; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }

    public String getImageCouverture() { return imageCouverture; }
    public void setImageCouverture(String imageCouverture) { this.imageCouverture = imageCouverture; }

    public Boolean getLectureAuto() { return lectureAuto; }
    public void setLectureAuto(Boolean lectureAuto) { this.lectureAuto = lectureAuto; }

    public Boolean getInteractif() { return interactif; }
    public void setInteractif(Boolean interactif) { this.interactif = interactif; }

    public Long getNiveauId() { return niveauId; }
    public void setNiveauId(Long niveauId) { this.niveauId = niveauId; }

    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }

    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }

    public Long getLangueId() { return langueId; }
    public void setLangueId(Long langueId) { this.langueId = langueId; }
}
