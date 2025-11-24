package com.example.edugo.dto;

import java.util.List;

public class SubmitRequest {
    private Long eleveId;
    private List<SubmitAnswer> reponses;
    private Integer tempsPasse; // Temps passé en secondes (optionnel)

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }

    public List<SubmitAnswer> getReponses() { return reponses; }
    public void setReponses(List<SubmitAnswer> reponses) { this.reponses = reponses; }

    public Integer getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(Integer tempsPasse) { this.tempsPasse = tempsPasse; }

    public static class SubmitAnswer {
        private Long questionId;
        private java.util.List<Long> reponseIds;

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public java.util.List<Long> getReponseIds() { return reponseIds; }
        public void setReponseIds(java.util.List<Long> reponseIds) { this.reponseIds = reponseIds; }
    }
}
