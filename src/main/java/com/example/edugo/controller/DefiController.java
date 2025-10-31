package com.example.edugo.controller;

import com.example.edugo.dto.DefiRequest;
import com.example.edugo.dto.DefiResponse;
import com.example.edugo.dto.DefiDetailResponse;
import com.example.edugo.dto.EleveDefiResponse;
import com.example.edugo.service.ServiceDefi;
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
@RequestMapping("/api/defis")
@RequiredArgsConstructor
@Tag(name = "Défis", description = "Gestion des défis et participations")
@SecurityRequirement(name = "bearerAuth")
public class DefiController {

    private final ServiceDefi serviceDefi;

    // ==================== CRUD DEFIS (ADMIN) ====================
    @GetMapping
    @Operation(summary = "Récupérer tous les défis")
    public ResponseEntity<List<DefiResponse>> getAllDefis() {
        return ResponseEntity.ok(serviceDefi.getAllDefis());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un défi par ID")
    public ResponseEntity<DefiDetailResponse> getDefiById(@Parameter(description = "ID du défi") @PathVariable Long id) {
        return ResponseEntity.ok(serviceDefi.getDefiById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un défi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefiResponse> createDefi(@RequestBody DefiRequest dto) {
        return ResponseEntity.ok(serviceDefi.createDefi(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un défi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefiResponse> updateDefi(@Parameter(description = "ID du défi") @PathVariable Long id,
                                       @RequestBody DefiRequest dto) {
        return ResponseEntity.ok(serviceDefi.updateDefi(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un défi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDefi(@Parameter(description = "ID du défi") @PathVariable Long id) {
        serviceDefi.deleteDefi(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PARTICIPATION (ELEVE) ====================
    @GetMapping("/disponibles/{eleveId}")
    @Operation(summary = "Défis disponibles pour un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<DefiResponse>> getDefisDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceDefi.getDefisDisponibles(eleveId));
    }

    @PostMapping("/participer/{eleveId}/{defiId}")
    @Operation(summary = "Participer à un défi")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<EleveDefiResponse> participerDefi(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                           @Parameter(description = "ID du défi") @PathVariable Long defiId) {
        return ResponseEntity.ok(serviceDefi.participerDefi(eleveId, defiId));
    }

    @GetMapping("/participes/{eleveId}")
    @Operation(summary = "Défis participés par un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<EleveDefiResponse>> getDefisParticipes(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceDefi.getDefisParticipes(eleveId));
    }
}


