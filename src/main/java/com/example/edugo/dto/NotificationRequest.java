package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private String titre;
    private String message;
    private String type;
    private LocalDateTime dateEnvoi;
    private Long utilisateurId;
    private Boolean lu;
}
