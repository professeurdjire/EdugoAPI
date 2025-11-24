package com.example.edugo.controller;

import com.example.edugo.dto.NotificationResponse;
import com.example.edugo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Gestion des notifications pour les élèves")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Récupère les notifications d'un élève
     * Supporte le paramètre unreadOnly pour filtrer les notifications non lues
     */
    @GetMapping
    @Operation(
        summary = "Récupérer les notifications d'un élève",
        description = "Récupère les notifications d'un élève. Utilisez le paramètre 'unreadOnly=true' pour obtenir uniquement les notifications non lues."
    )
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @Parameter(description = "ID de l'élève", required = true) @RequestParam Long eleveId,
            @Parameter(description = "Filtrer uniquement les notifications non lues") @RequestParam(required = false) Boolean unreadOnly) {
        
        List<NotificationResponse> notifications = notificationService.getNotificationsByEleveId(eleveId, unreadOnly);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Récupère le nombre de notifications non lues d'un élève
     */
    @GetMapping("/unread-count")
    @Operation(
        summary = "Récupérer le nombre de notifications non lues",
        description = "Retourne le nombre de notifications non lues pour un élève"
    )
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @Parameter(description = "ID de l'élève", required = true) @RequestParam Long eleveId) {
        
        Long count = notificationService.getUnreadCount(eleveId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Marque une notification comme lue
     */
    @PutMapping("/{id}/marquer-vu")
    @Operation(
        summary = "Marquer une notification comme lue",
        description = "Marque une notification spécifique comme lue"
    )
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID de la notification") @PathVariable Long id) {
        
        NotificationResponse notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    /**
     * Marque toutes les notifications d'un élève comme lues
     */
    @PutMapping("/marquer-tout-vu")
    @Operation(
        summary = "Marquer toutes les notifications comme lues",
        description = "Marque toutes les notifications non lues d'un élève comme lues"
    )
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @Parameter(description = "ID de l'élève", required = true) @RequestParam Long eleveId) {
        
        notificationService.markAllAsRead(eleveId);
        return ResponseEntity.ok(Map.of("message", "Toutes les notifications ont été marquées comme lues"));
    }
}

