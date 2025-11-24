package com.example.edugo.service;

import com.example.edugo.entity.Principales.Device;
import com.example.edugo.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour envoyer des notifications push via OneSignal
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OneSignalService {

    private final DeviceRepository deviceRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${onesignal.app.id:}")
    private String oneSignalAppId;

    @Value("${onesignal.rest.api.key:}")
    private String oneSignalRestApiKey;

    @Value("${onesignal.enabled:true}")
    private boolean oneSignalEnabled;

    private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";

    /**
     * Envoie une notification à un utilisateur spécifique
     */
    public boolean sendNotificationToUser(Long userId, String userRole, String title, String message, Map<String, Object> data) {
        if (!oneSignalEnabled || oneSignalAppId.isEmpty() || oneSignalRestApiKey.isEmpty()) {
            log.warn("OneSignal n'est pas configuré. Notification non envoyée.");
            return false;
        }

        try {
            List<Device> devices = deviceRepository.findByUserIdAndRole(userId, userRole);
            if (devices.isEmpty()) {
                log.info("Aucun appareil trouvé pour l'utilisateur {} avec le rôle {}", userId, userRole);
                return false;
            }

            List<String> playerIds = devices.stream()
                    .map(Device::getOneSignalPlayerId)
                    .collect(Collectors.toList());

            return sendNotification(playerIds, title, message, data);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification à l'utilisateur {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Envoie une notification à tous les utilisateurs d'un rôle spécifique (ex: tous les admins)
     */
    public boolean sendNotificationToRole(String userRole, String title, String message, Map<String, Object> data) {
        if (!oneSignalEnabled || oneSignalAppId.isEmpty() || oneSignalRestApiKey.isEmpty()) {
            log.warn("OneSignal n'est pas configuré. Notification non envoyée.");
            return false;
        }

        try {
            List<Device> devices = deviceRepository.findByRoleAndActive(userRole);
            if (devices.isEmpty()) {
                log.info("Aucun appareil trouvé pour le rôle {}", userRole);
                return false;
            }

            List<String> playerIds = devices.stream()
                    .map(Device::getOneSignalPlayerId)
                    .collect(Collectors.toList());

            return sendNotification(playerIds, title, message, data);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification au rôle {}: {}", userRole, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Envoie une notification à plusieurs utilisateurs par leurs Player IDs
     */
    public boolean sendNotification(List<String> playerIds, String title, String message, Map<String, Object> data) {
        if (!oneSignalEnabled || oneSignalAppId.isEmpty() || oneSignalRestApiKey.isEmpty()) {
            log.warn("OneSignal n'est pas configuré. Notification non envoyée.");
            return false;
        }

        if (playerIds == null || playerIds.isEmpty()) {
            log.warn("Aucun Player ID fourni pour l'envoi de notification");
            return false;
        }

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("app_id", oneSignalAppId);
            requestBody.put("include_player_ids", playerIds);

            // Headings (titre de la notification)
            Map<String, String> headings = new HashMap<>();
            headings.put("en", title); // Anglais
            headings.put("fr", title); // Français
            requestBody.put("headings", headings);

            // Contents (message de la notification)
            Map<String, String> contents = new HashMap<>();
            contents.put("en", message);
            contents.put("fr", message);
            requestBody.put("contents", contents);

            // Données additionnelles pour l'application
            if (data != null && !data.isEmpty()) {
                requestBody.put("data", data);
            }

            // Configuration Android
            requestBody.put("priority", 10); // High priority
            Map<String, String> smallIcon = new HashMap<>();
            smallIcon.put("small_icon", "ic_notification");
            requestBody.put("small_icon", "ic_notification");

            // Configuration iOS
            Map<String, Object> iosAttachments = new HashMap<>();
            requestBody.put("ios_attachments", iosAttachments);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + oneSignalRestApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Envoi de la requête
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    ONESIGNAL_API_URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Notification envoyée avec succès via OneSignal. Réponse: {}", response.getBody());
                return true;
            } else {
                log.error("Erreur lors de l'envoi de notification OneSignal. Status: {}", response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification OneSignal: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Envoie une notification silencieuse (sans affichage, seulement données)
     */
    public boolean sendDataOnlyNotification(List<String> playerIds, Map<String, Object> data) {
        if (!oneSignalEnabled || oneSignalAppId.isEmpty() || oneSignalRestApiKey.isEmpty()) {
            log.warn("OneSignal n'est pas configuré. Notification non envoyée.");
            return false;
        }

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("app_id", oneSignalAppId);
            requestBody.put("include_player_ids", playerIds);
            requestBody.put("data", data);
            requestBody.put("content_available", true); // iOS
            requestBody.put("android_channel_id", "edugo_data_channel");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + oneSignalRestApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    ONESIGNAL_API_URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification de données OneSignal: {}", e.getMessage(), e);
            return false;
        }
    }
}

