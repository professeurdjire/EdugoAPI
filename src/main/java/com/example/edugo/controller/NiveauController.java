package com.example.edugo.controller;

import com.example.edugo.dto.NiveauRequest;
import com.example.edugo.dto.NiveauResponse;
import com.example.edugo.entity.Principales.Niveau;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/niveaux")
@RequiredArgsConstructor
@Tag(name = "Niveaux", description = "Gestion des niveaux et classes associées")
@SecurityRequirement(name = "bearerAuth")
public class NiveauController {

    private final ServiceNiveau serviceNiveau;

    // ==================== CRUD NIVEAUX ====================

    @GetMapping
    @Operation(summary = "Récupérer tous les niveaux")
    public ResponseEntity<List<NiveauResponse>> getAllNiveaux() {
        List<NiveauResponse> responses = serviceNiveau.getAllNiveaux()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un niveau par ID")
    public ResponseEntity<NiveauResponse> getNiveauById(
            @Parameter(description = "ID du niveau") @PathVariable Long id) {
        Niveau niveau = serviceNiveau.getNiveauById(id);
        return ResponseEntity.ok(convertToResponse(niveau));
    }

    @PostMapping
    @Operation(summary = "Créer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NiveauResponse> createNiveau(@RequestBody NiveauRequest request) {
        Niveau niveau = new Niveau();
        niveau.setNom(request.getNom());

        Niveau saved = serviceNiveau.createNiveau(niveau);
        return ResponseEntity.ok(convertToResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NiveauResponse> updateNiveau(
            @Parameter(description = "ID du niveau") @PathVariable Long id,
            @RequestBody NiveauRequest request) {

        Niveau niveauDetails = new Niveau();
        niveauDetails.setNom(request.getNom());

        Niveau updated = serviceNiveau.updateNiveau(id, niveauDetails);
        return ResponseEntity.ok(convertToResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNiveau(
            @Parameter(description = "ID du niveau") @PathVariable Long id) {
        serviceNiveau.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Méthode utilitaire ====================

    private NiveauResponse convertToResponse(Niveau niveau) {
        NiveauResponse response = new NiveauResponse();
        response.setId(niveau.getId());
        response.setNom(niveau.getNom());
        return response;
    }
}
