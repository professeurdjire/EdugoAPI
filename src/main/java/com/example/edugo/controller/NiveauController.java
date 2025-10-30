package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.service.ServiceNiveau;
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
@RequestMapping("/api/niveaux")
@RequiredArgsConstructor
@Tag(name = "Niveaux", description = "Gestion des niveaux et classes associées")
@SecurityRequirement(name = "bearerAuth")
public class NiveauController {

    private final ServiceNiveau serviceNiveau;

    // ==================== CRUD NIVEAUX (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les niveaux")
    public ResponseEntity<List<Niveau>> getAllNiveaux() {
        return ResponseEntity.ok(serviceNiveau.getAllNiveaux());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un niveau par ID")
    public ResponseEntity<Niveau> getNiveauById(@Parameter(description = "ID du niveau") @PathVariable Long id) {
        return ResponseEntity.ok(serviceNiveau.getNiveauById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Niveau> createNiveau(@RequestBody Niveau niveau) {
        return ResponseEntity.ok(serviceNiveau.createNiveau(niveau));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Niveau> updateNiveau(@Parameter(description = "ID du niveau") @PathVariable Long id,
                                               @RequestBody Niveau niveau) {
        return ResponseEntity.ok(serviceNiveau.updateNiveau(id, niveau));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNiveau(@Parameter(description = "ID du niveau") @PathVariable Long id) {
        serviceNiveau.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CLASSES PAR NIVEAU ====================
    
    @GetMapping("/{id}/classes")
    @Operation(summary = "Classes par niveau")
    public ResponseEntity<List<Classe>> getClassesByNiveau(@Parameter(description = "ID du niveau") @PathVariable Long id) {
        return ResponseEntity.ok(serviceNiveau.getClassesByNiveau(id));
    }
}


