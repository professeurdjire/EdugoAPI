package com.example.edugo.controller;

import com.example.edugo.dto.SuggestionRequest;
import com.example.edugo.dto.SuggestionResponse;
import com.example.edugo.service.ServiceSuggestion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
@Tag(name = "Suggestions", description = "Endpoints pour la gestion des suggestions des élèves")
@SecurityRequirement(name = "bearerAuth")
public class SuggestionController {

    private final ServiceSuggestion serviceSuggestion;

    // ==================== ENDPOINTS POUR ÉLÈVES ====================

    @PostMapping
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Ajouter une suggestion", description = "Permet à un élève d'ajouter une suggestion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestion ajoutée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<SuggestionResponse> ajouterSuggestion(
            @RequestBody SuggestionRequest suggestionRequest) {
        return ResponseEntity.ok(serviceSuggestion.ajouterSuggestion(suggestionRequest));
    }

    @GetMapping("/mes-suggestions")
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Récupérer mes suggestions", description = "Permet à un élève de récupérer ses propres suggestions")
    public ResponseEntity<List<SuggestionResponse>> getMesSuggestions() {
        return ResponseEntity.ok(serviceSuggestion.getSuggestionsParEleve());
    }

    @GetMapping("/mes-suggestions/{id}")
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Récupérer une de mes suggestions", description = "Permet à un élève de récupérer une de ses suggestions spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestion récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Suggestion non trouvée")
    })
    public ResponseEntity<SuggestionResponse> getMaSuggestion(
            @Parameter(description = "ID de la suggestion") @PathVariable Long id) {
        return ResponseEntity.ok(serviceSuggestion.getSuggestionParEleve(id));
    }

    // ==================== ENDPOINTS POUR ADMIN ====================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer toutes les suggestions", description = "Permet à un admin de récupérer toutes les suggestions (accès admin uniquement)")
    public ResponseEntity<List<SuggestionResponse>> getAllSuggestions() {
        return ResponseEntity.ok(serviceSuggestion.getAllSuggestions());
    }

    @GetMapping("/eleve/{eleveId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer les suggestions d'un élève", description = "Permet à un admin de récupérer les suggestions d'un élève spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestions récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Élève non trouvé")
    })
    public ResponseEntity<List<SuggestionResponse>> getSuggestionsParEleve(
            @Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceSuggestion.getSuggestionsParEleveId(eleveId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer une suggestion par ID", description = "Permet à un admin de récupérer une suggestion spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestion récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Suggestion non trouvée")
    })
    public ResponseEntity<SuggestionResponse> getSuggestionById(
            @Parameter(description = "ID de la suggestion") @PathVariable Long id) {
        return ResponseEntity.ok(serviceSuggestion.getSuggestionById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une suggestion", description = "Permet à un admin de supprimer une suggestion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestion supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Suggestion non trouvée")
    })
    public ResponseEntity<Map<String, String>> deleteSuggestion(
            @Parameter(description = "ID de la suggestion") @PathVariable Long id) {
        serviceSuggestion.deleteSuggestion(id);
        return ResponseEntity.ok(Map.of("message", "Suggestion supprimée avec succès"));
    }

    @GetMapping("/statistiques")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer les statistiques des suggestions", description = "Permet à un admin de récupérer les statistiques des suggestions")
    public ResponseEntity<ServiceSuggestion.SuggestionStats> getStatistiquesSuggestions() {
        return ResponseEntity.ok(serviceSuggestion.getStatistiquesSuggestions());
    }
}