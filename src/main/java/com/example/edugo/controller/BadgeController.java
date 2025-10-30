package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.service.ServiceBadge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
@Tag(name = "Badges", description = "Gestion des badges et attributions")
@SecurityRequirement(name = "bearerAuth")
public class BadgeController {

    private final ServiceBadge serviceBadge;

    // ==================== CRUD BADGES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les badges")
    public ResponseEntity<List<Badge>> getAllBadges() {
        return ResponseEntity.ok(serviceBadge.getAllBadges());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un badge par ID")
    public ResponseEntity<Badge> getBadgeById(@Parameter(description = "ID du badge") @PathVariable Long id) {
        return ResponseEntity.ok(serviceBadge.getBadgeById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un badge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Badge> createBadge(@RequestBody Badge badge) {
        return ResponseEntity.ok(serviceBadge.createBadge(badge));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un badge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Badge> updateBadge(@Parameter(description = "ID du badge") @PathVariable Long id,
                                             @RequestBody Badge badge) {
        return ResponseEntity.ok(serviceBadge.updateBadge(id, badge));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un badge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBadge(@Parameter(description = "ID du badge") @PathVariable Long id) {
        serviceBadge.deleteBadge(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== BADGES POUR ÉLÈVES ====================
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Badges par type")
    public ResponseEntity<List<Badge>> getBadgesByType(@Parameter(description = "Type de badge") @PathVariable TypeBadge type) {
        return ResponseEntity.ok(serviceBadge.getBadgesByType(type));
    }
}


