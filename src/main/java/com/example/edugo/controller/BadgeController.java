package com.example.edugo.controller;

import com.example.edugo.dto.BadgeRequest;
import com.example.edugo.dto.BadgeResponse;
import com.example.edugo.service.ServiceBadge;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final ServiceBadge serviceBadge;

    public BadgeController(ServiceBadge serviceBadge) {
        this.serviceBadge = serviceBadge;
    }

    // -------------------- CREATE --------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BadgeResponse> createBadge(@Valid @RequestBody BadgeRequest request) {
        BadgeResponse response = serviceBadge.createBadge(request);
        return ResponseEntity.ok(response);
    }

    // -------------------- READ --------------------
    @GetMapping
    public ResponseEntity<List<BadgeResponse>> getAllBadges() {
        List<BadgeResponse> badges = serviceBadge.getAllBadges();
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> getBadgeById(@PathVariable Long id) {
        BadgeResponse badge = serviceBadge.getBadgeById(id);
        return ResponseEntity.ok(badge);
    }

    // -------------------- UPDATE --------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BadgeResponse> updateBadge(@PathVariable Long id,
                                                     @Valid @RequestBody BadgeRequest request) {
        BadgeResponse updated = serviceBadge.updateBadge(id, request);
        return ResponseEntity.ok(updated);
    }

    // -------------------- DELETE --------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id) {
        serviceBadge.deleteBadge(id);
        return ResponseEntity.noContent().build();
    }
}
