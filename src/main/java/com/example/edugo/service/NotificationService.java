package com.example.edugo.service;

import com.example.edugo.dto.NotificationResponse;
import com.example.edugo.entity.Principales.Notification;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Récupère toutes les notifications d'un élève
     */
    public List<NotificationResponse> getNotificationsByEleveId(Long eleveId, Boolean unreadOnly) {
        List<Notification> notifications;
        
        if (Boolean.TRUE.equals(unreadOnly)) {
            notifications = notificationRepository.findUnreadByEleveId(eleveId);
        } else {
            notifications = notificationRepository.findByEleveIdOrderByDateDesc(eleveId);
        }
        
        return notifications.stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère le nombre de notifications non lues d'un élève
     */
    public Long getUnreadCount(Long eleveId) {
        return notificationRepository.countUnreadByEleveId(eleveId);
    }

    /**
     * Marque une notification comme lue
     */
    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        
        notification.marquerCommeVu();
        notificationRepository.save(notification);
        
        return toNotificationResponse(notification);
    }

    /**
     * Marque toutes les notifications d'un élève comme lues
     */
    @Transactional
    public void markAllAsRead(Long eleveId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadByEleveId(eleveId);
        unreadNotifications.forEach(Notification::marquerCommeVu);
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Convertit une entité Notification en DTO NotificationResponse
     */
    private NotificationResponse toNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitre(notification.getTitre());
        response.setMessage(notification.getMessage());
        response.setDateEnvoi(notification.getDateExplication());
        response.setUtilisateurId(notification.getIdEleve());
        response.setLu(notification.getEstVu() != null && notification.getEstVu());
        return response;
    }
}

