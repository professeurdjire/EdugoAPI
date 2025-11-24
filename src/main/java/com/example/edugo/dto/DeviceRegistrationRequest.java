package com.example.edugo.dto;

import lombok.Data;

@Data
public class DeviceRegistrationRequest {
    private String oneSignalPlayerId;
    private Long userId;
    private String userRole; // "ELEVE" ou "ADMIN"
    private String platform; // "Android", "iOS", "Web"
    private String deviceModel;
    private String appVersion;
}

