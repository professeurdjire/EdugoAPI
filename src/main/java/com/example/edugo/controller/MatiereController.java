package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.service.ServiceMatiere;
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
@RequestMapping("/api/matieres")
@RequiredArgsConstructor
@Tag(name = "Matières", description = "Gestion des matières, livres et exercices par matière")
@SecurityRequirement(name = "bearerAuth")
public class MatiereController {

    private final ServiceMatiere serviceMatiere;

    // ==================== CRUD MATIÈRES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les matières")
    public ResponseEntity<List<Matiere>> getAllMatieres() {
        return ResponseEntity.ok(serviceMatiere.getAllMatieres());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une matière par ID")
    public ResponseEntity<Matiere> getMatiereById(@Parameter(description = "ID de la matière") @PathVariable Long id) {
        return ResponseEntity.ok(serviceMatiere.getMatiereById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une matière")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) {
        return ResponseEntity.ok(serviceMatiere.createMatiere(matiere));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une matière")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Matiere> updateMatiere(@Parameter(description = "ID de la matière") @PathVariable Long id,
                                                 @RequestBody Matiere matiere) {
        return ResponseEntity.ok(serviceMatiere.updateMatiere(id, matiere));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une matière")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMatiere(@Parameter(description = "ID de la matière") @PathVariable Long id) {
        serviceMatiere.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LIVRES ET EXERCICES PAR MATIÈRE ====================
    
    @GetMapping("/{id}/livres")
    @Operation(summary = "Livres par matière")
    public ResponseEntity<List<Livre>> getLivresByMatiere(@Parameter(description = "ID de la matière") @PathVariable Long id) {
        return ResponseEntity.ok(serviceMatiere.getLivresByMatiere(id));
    }

    @GetMapping("/{id}/exercices")
    @Operation(summary = "Exercices par matière")
    public ResponseEntity<List<Exercice>> getExercicesByMatiere(@Parameter(description = "ID de la matière") @PathVariable Long id) {
        return ResponseEntity.ok(serviceMatiere.getExercicesByMatiere(id));
    }
}


