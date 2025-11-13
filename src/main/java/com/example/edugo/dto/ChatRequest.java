package com.example.edugo.dto;

public class ChatRequest {
    private Long sessionId; // optional, create new if null
    private Long eleveId;   // required when creating new session
    private Long livreId;   // optional
    private Long matiereId; // optional
    private String message; // user message

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }
    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }
    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
