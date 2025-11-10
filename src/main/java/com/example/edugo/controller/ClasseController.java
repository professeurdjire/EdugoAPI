package com.example.edugo.controller;

import com.example.edugo.dto.ClasseRequest;
import com.example.edugo.dto.ClasseResponse;
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
import com.example.edugo.entity.Principales.Eleve;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@Tag(name = "Classes", description = "Gestion des classes et affectations d'élèves")
@SecurityRequirement(name = "bearerAuth")
public class ClasseController {

    private final ClasseService classeService;

    // ==================== CRUD CLASSES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les classes")
    public ResponseEntity<List<ClasseResponse>> getAllClasses() {
        return ResponseEntity.ok(classeService.getAllClassesDto());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une classe par ID")
    public ResponseEntity<ClasseResponse> getClasseById(@Parameter(description = "ID de la classe") @PathVariable Long id) {
        return ResponseEntity.ok(classeService.getClasseByIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Créer une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClasseResponse> createClasse(@RequestBody ClasseRequest classeRequest) {
        return ResponseEntity.ok(classeService.createClasseDto(classeRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClasseResponse> updateClasse(@Parameter(description = "ID de la classe") @PathVariable Long id,
                                              @RequestBody ClasseRequest classeRequest) {
        return ResponseEntity.ok(classeService.updateClasseDto(id, classeRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une classe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClasse(@Parameter(description = "ID de la classe") @PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTION ÉLÈVES DANS LA CLASSE ====================
    
    // Pour éviter les boucles, il faudrait aussi un EleveResponse ici

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


