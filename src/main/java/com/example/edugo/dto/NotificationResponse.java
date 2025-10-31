package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String titre;
    private String message;
    private String type;
    private LocalDateTime dateEnvoi;
    private Long utilisateurId;
    private String utilisateurNom;
    private Boolean lu;
}
