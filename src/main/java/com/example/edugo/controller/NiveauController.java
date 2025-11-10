package com.example.edugo.controller;

import com.example.edugo.dto.NiveauRequest;
import com.example.edugo.dto.NiveauResponse;
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
@RequestMapping("/niveaux")
@RequiredArgsConstructor
@Tag(name = "Niveaux", description = "Gestion des niveaux et classes associées")
@SecurityRequirement(name = "bearerAuth")
public class NiveauController {

    private final ServiceNiveau serviceNiveau;

    // ==================== CRUD NIVEAUX (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les niveaux")
    public ResponseEntity<List<NiveauResponse>> getAllNiveaux() {
        return ResponseEntity.ok(serviceNiveau.getAllNiveauxDto());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un niveau par ID")
    public ResponseEntity<NiveauResponse> getNiveauById(@Parameter(description = "ID du niveau") @PathVariable Long id) {
        return ResponseEntity.ok(serviceNiveau.getNiveauByIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Créer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NiveauResponse> createNiveau(@RequestBody NiveauRequest niveauRequest) {
        return ResponseEntity.ok(serviceNiveau.createNiveauDto(niveauRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NiveauResponse> updateNiveau(@Parameter(description = "ID du niveau") @PathVariable Long id,
                                               @RequestBody NiveauRequest niveauRequest) {
        return ResponseEntity.ok(serviceNiveau.updateNiveauDto(id, niveauRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNiveau(@Parameter(description = "ID du niveau") @PathVariable Long id) {
        serviceNiveau.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CLASSES PAR NIVEAU ====================
    
    // Pour éviter les boucles, il faudrait aussi un ClasseResponse ici

}


