package com.example.edugo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les requêtes OneSignal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneSignalNotificationRequest {
    private String appId;
    private List<String> includePlayerIds; // OneSignal Player IDs
    private Map<String, String> headings;
    private Map<String, String> contents;
    private Map<String, Object> data; // Données additionnelles pour l'app
    private String smallIcon;
    private String largeIcon;
    private Map<String, String> url;
    private Map<String, Object> filters; // Pour filtrer par rôle, etc.
    
    // Pour notifications groupées
    private Boolean androidGroup;
    private String androidGroupKey;
    private Integer priority; // 10 = high priority (pour Android)
}

