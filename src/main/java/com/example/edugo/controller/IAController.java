package com.example.edugo.controller;

import com.example.edugo.dto.*;
import com.example.edugo.service.IAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ia")
@Tag(name = "IA éducative", description = "Chat éducatif et génération de ressources IA")
@SecurityRequirement(name = "bearerAuth")
public class IAController {

    private final IAService iaService;

    public IAController(IAService iaService) { this.iaService = iaService; }

    // ========= Chat =========

    @PostMapping("/chat")
    @Operation(summary = "Envoyer un message au chat IA")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(iaService.sendMessage(request));
    }

    @GetMapping("/chat/sessions")
    @Operation(summary = "Lister les sessions de chat d'un élève")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<List<SessionResponse>> listSessions(@Parameter(description = "ID de l'élève") @RequestParam Long eleveId) {
        return ResponseEntity.ok(iaService.listSessions(eleveId));
    }

    @GetMapping("/chat/sessions/{id}")
    @Operation(summary = "Récupérer l'historique d'une session")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<ChatResponse> getSession(@Parameter(description = "ID de la session") @PathVariable Long id) {
        return ResponseEntity.ok(iaService.getSession(id));
    }

    @DeleteMapping("/chat/sessions/{id}")
    @Operation(summary = "Supprimer une session de chat")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<Void> deleteSession(@Parameter(description = "ID de la session") @PathVariable Long id) {
        iaService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // ========= Ressources IA =========

    @PostMapping("/ressources")
    @Operation(summary = "Générer une ressource IA")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<RessourceIAResponse> generate(@RequestBody RessourceIARequest request) {
        return ResponseEntity.ok(iaService.generateRessource(request));
    }

    @GetMapping("/ressources")
    @Operation(summary = "Lister les ressources IA")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<List<RessourceIAResponse>> list(@RequestParam(required = false) Long eleveId,
                                                          @RequestParam(required = false) Long livreId,
                                                          @RequestParam(required = false) String type) {
        return ResponseEntity.ok(iaService.listRessources(eleveId, livreId, type));
    }

    @GetMapping("/ressources/{id}")
    @Operation(summary = "Détail d'une ressource IA")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<RessourceIAResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(iaService.getRessource(id));
    }
}
