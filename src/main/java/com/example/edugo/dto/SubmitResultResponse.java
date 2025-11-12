package com.example.edugo.dto;

import java.util.List;

public class SubmitResultResponse {
    private Long ownerId;
    private String ownerType; // QUIZ | CHALLENGE
    private Long eleveId;
    private int score;
    private int totalPoints;
    private List<Detail> details;

    public static class Detail {
        private Long questionId;
        private int points;
        private boolean correct;

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public int getPoints() { return points; }
        public void setPoints(int points) { this.points = points; }
        public boolean isCorrect() { return correct; }
        public void setCorrect(boolean correct) { this.correct = correct; }
    }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerType() { return ownerType; }
    public void setOwnerType(String ownerType) { this.ownerType = ownerType; }
    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public List<Detail> getDetails() { return details; }
    public void setDetails(List<Detail> details) { this.details = details; }
}
