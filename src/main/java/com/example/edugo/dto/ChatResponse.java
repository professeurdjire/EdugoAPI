package com.example.edugo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {
    private Long sessionId;
    private List<Message> messages;

    public static class Message {
        private String role; // USER/ASSISTANT/SYSTEM
        private String content;
        private LocalDateTime createdAt;
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}
