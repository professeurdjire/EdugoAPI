package com.example.edugo.entity.Principales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité pour stocker les informations des appareils (OneSignal Player IDs)
 * permettant l'envoi de notifications push
 */
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "onesignal_player_id", nullable = false, unique = true, length = 500)
    private String oneSignalPlayerId;

    @Column(name = "user_id")
    private Long userId; // ID de l'utilisateur (élève ou admin)

    @Column(name = "user_role", nullable = false)
    private String userRole; // "ELEVE" ou "ADMIN"

    @Column(name = "platform")
    private String platform; // "Android", "iOS", "Web"

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructeurs
    public Device() {}

    public Device(String oneSignalPlayerId, Long userId, String userRole, String platform) {
        this.oneSignalPlayerId = oneSignalPlayerId;
        this.userId = userId;
        this.userRole = userRole;
        this.platform = platform;
        this.isActive = true;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOneSignalPlayerId() { return oneSignalPlayerId; }
    public void setOneSignalPlayerId(String oneSignalPlayerId) { this.oneSignalPlayerId = oneSignalPlayerId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

