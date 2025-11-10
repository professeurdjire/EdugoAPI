package com.example.edugo.controller;

import com.example.edugo.dto.PartenaireRequest;
import com.example.edugo.dto.PartenaireResponse;
import com.example.edugo.service.PartenaireService;
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
@RequestMapping("/api/partenaires")
@RequiredArgsConstructor
@Tag(name = "Partenaires", description = "Gestion des partenaires de la plateforme")
@SecurityRequirement(name = "bearerAuth")
public class PartenaireController {

    private final PartenaireService partenaireService;

    // ==================== CRUD PARTENAIRES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les partenaires")
    public ResponseEntity<List<PartenaireResponse>> getAllPartenaires() {
        return ResponseEntity.ok(partenaireService.getAllPartenairesDto());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un partenaire par ID")
    public ResponseEntity<PartenaireResponse> getPartenaireById(@Parameter(description = "ID du partenaire") @PathVariable Long id) {
        return ResponseEntity.ok(partenaireService.getPartenaireByIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Créer un partenaire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PartenaireResponse> createPartenaire(@RequestBody PartenaireRequest partenaireRequest) {
        return ResponseEntity.ok(partenaireService.createPartenaireDto(partenaireRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un partenaire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PartenaireResponse> updatePartenaire(@Parameter(description = "ID du partenaire") @PathVariable Long id,
                                               @RequestBody PartenaireRequest partenaireRequest) {
        return ResponseEntity.ok(partenaireService.updatePartenaireDto(id, partenaireRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un partenaire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePartenaire(@Parameter(description = "ID du partenaire") @PathVariable Long id) {
        partenaireService.deletePartenaire(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== ENDPOINTS PUBLIQUES ====================
    
    @GetMapping("/actifs")
    @Operation(summary = "Récupérer tous les partenaires actifs")
    public ResponseEntity<List<PartenaireResponse>> getPartenairesActifs() {
        return ResponseEntity.ok(partenaireService.getAllPartenairesDto().stream()
                .filter(PartenaireResponse::getActif)
                .toList());
    }
}