package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.service.ClasseService;
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
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Tag(name = "Classes", description = "Gestion des classes et affectations d'élèves")
@SecurityRequirement(name = "bearerAuth")
public class ClasseController {

    private final ClasseService classeService;

    // ==================== CRUD CLASSES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les classes")
    public ResponseEntity<List<Classe>> getAllClasses() {
        return ResponseEntity.ok(classeService.getAllClasses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une classe par ID")
    public ResponseEntity<Classe> getClasseById(@Parameter(description = "ID de la classe") @PathVariable Long id) {
        return ResponseEntity.ok(classeService.getClasseById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Classe> createClasse(@RequestBody Classe classe) {
        return ResponseEntity.ok(classeService.createClasse(classe));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Classe> updateClasse(@Parameter(description = "ID de la classe") @PathVariable Long id,
                                              @RequestBody Classe classe) {
        return ResponseEntity.ok(classeService.updateClasse(id, classe));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClasse(@Parameter(description = "ID de la classe") @PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTION ÉLÈVES DANS LA CLASSE ====================
    
    @GetMapping("/{id}/eleves")
    @Operation(summary = "Élèves par classe")
    public ResponseEntity<List<Eleve>> getElevesByClasse(@Parameter(description = "ID de la classe") @PathVariable Long id) {
        return ResponseEntity.ok(classeService.getElevesByClasse(id));
    }

    @PostMapping("/{classeId}/assigner/{eleveId}")
    @Operation(summary = "Assigner un élève à une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Eleve> assignerEleveAClasse(@Parameter(description = "ID de la classe") @PathVariable Long classeId,
                                                     @Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(classeService.assignerEleveAClasse(eleveId, classeId));
    }

    @PostMapping("/retirer/{eleveId}")
    @Operation(summary = "Retirer un élève de sa classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> retirerEleveDeClasse(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        classeService.retirerEleveDeClasse(eleveId);
        return ResponseEntity.noContent().build();
    }
}


