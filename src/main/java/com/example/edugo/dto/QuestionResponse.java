package com.example.edugo.dto;

import java.util.List;

public class QuestionResponse {
    private Long id;
    private String intitule;
    private String type;
    private Integer numeroOrdre;
    private List<ReponsePossibleResponse> reponsesPossibles;

    public QuestionResponse() {
    }

    public QuestionResponse(Long id, String intitule, String type, Integer numeroOrdre, List<ReponsePossibleResponse> reponsesPossibles) {
        this.id = id;
        this.intitule = intitule;
        this.type = type;
        this.numeroOrdre = numeroOrdre;
        this.reponsesPossibles = reponsesPossibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(Integer numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public List<ReponsePossibleResponse> getReponsesPossibles() {
        return reponsesPossibles;
    }

    public void setReponsesPossibles(List<ReponsePossibleResponse> reponsesPossibles) {
        this.reponsesPossibles = reponsesPossibles;
    }

    @Override
    public String toString() {
        return "QuestionResponse{" +
                "id=" + id +
                ", intitule='" + intitule + '\'' +
                ", type='" + type + '\'' +
                ", numeroOrdre=" + numeroOrdre +
                ", reponsesPossibles=" + reponsesPossibles +
                '}';
    }
}

class TypeQuestionResponse {
    private String code;
    private String libelle;

    public TypeQuestionResponse() {
    }

    public TypeQuestionResponse(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "TypeQuestionResponse{" +
                "code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
