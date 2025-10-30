package com.example.edugo.controller;

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
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        return ResponseEntity.ok(serviceChallenge.getAllChallenges());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un challenge par ID")
    public ResponseEntity<Challenge> getChallengeById(@Parameter(description = "ID du challenge") @PathVariable Long id) {
        return ResponseEntity.ok(serviceChallenge.getChallengeById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Challenge> createChallenge(@RequestBody Challenge challenge) {
        return ResponseEntity.ok(serviceChallenge.createChallenge(challenge));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Challenge> updateChallenge(@Parameter(description = "ID du challenge") @PathVariable Long id,
                                                     @RequestBody Challenge challenge) {
        return ResponseEntity.ok(serviceChallenge.updateChallenge(id, challenge));
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


