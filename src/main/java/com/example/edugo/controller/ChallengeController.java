package com.example.edugo.controller;

import com.example.edugo.dto.ChallengeRequest;
import com.example.edugo.dto.ChallengeResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.service.ServiceChallenge;
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
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
@Tag(name = "Challenges", description = "Gestion des challenges et participations")
@SecurityRequirement(name = "bearerAuth")
public class ChallengeController {

    private final ServiceChallenge serviceChallenge;

    // ==================== CRUD CHALLENGES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les challenges")
    public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
        // map entities to DTOs using service mapping
        return ResponseEntity.ok(serviceChallenge.getAllChallenges().stream().map(ch -> {
            // reuse service private mapper via exposed DTO create/update; fallback manual minimal mapping
            ChallengeResponse dto = new ChallengeResponse();
            dto.setId(ch.getId());
            dto.setTitre(ch.getTitre());
            dto.setDescription(ch.getDescription());
            dto.setPoints(ch.getPoints());
            dto.setTheme(ch.getRewardMode());
            dto.setDateDebut(ch.getDateDebut());
            dto.setDateFin(ch.getDateFin());
            return dto;
        }).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un challenge par ID")
    public ResponseEntity<ChallengeResponse> getChallengeById(@Parameter(description = "ID du challenge") @PathVariable Long id) {
        Challenge ch = serviceChallenge.getChallengeById(id);
        ChallengeResponse dto = new ChallengeResponse();
        dto.setId(ch.getId());
        dto.setTitre(ch.getTitre());
        dto.setDescription(ch.getDescription());
        dto.setPoints(ch.getPoints());
        dto.setTheme(ch.getRewardMode());
        dto.setDateDebut(ch.getDateDebut());
        dto.setDateFin(ch.getDateFin());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Créer un challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChallengeResponse> createChallenge(@RequestBody ChallengeRequest dto) {
        return ResponseEntity.ok(serviceChallenge.createChallenge(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChallengeResponse> updateChallenge(@Parameter(description = "ID du challenge") @PathVariable Long id,
                                                     @RequestBody ChallengeRequest dto) {
        return ResponseEntity.ok(serviceChallenge.updateChallenge(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteChallenge(@Parameter(description = "ID du challenge") @PathVariable Long id) {
        serviceChallenge.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PARTICIPATION (ELEVE) ====================
    
    @GetMapping("/disponibles/{eleveId}")
    @Operation(summary = "Challenges disponibles pour un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<Challenge>> getChallengesDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceChallenge.getChallengesDisponibles(eleveId));
    }

    @PostMapping("/participer/{eleveId}/{challengeId}")
    @Operation(summary = "Participer à un challenge")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<Participation> participerChallenge(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                             @Parameter(description = "ID du challenge") @PathVariable Long challengeId) {
        return ResponseEntity.ok(serviceChallenge.participerChallenge(eleveId, challengeId));
    }

    @GetMapping("/participes/{eleveId}")
    @Operation(summary = "Challenges participés par un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<Participation>> getChallengesParticipes(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceChallenge.getChallengesParticipes(eleveId));
    }
}


