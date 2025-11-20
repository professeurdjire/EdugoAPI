package com.example.edugo.controller;

import com.example.edugo.dto.StatistiquesClasseResponse;
import com.example.edugo.dto.StatistiquesNiveauResponse;
import com.example.edugo.dto.StatistiquesPlateformeResponse;
import com.example.edugo.service.StatistiqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
@Tag(name = "Statistiques", description = "Endpoints pour la gestion des statistiques de la plateforme")
@SecurityRequirement(name = "bearerAuth")
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/plateforme")
    @Operation(summary = "Obtenir les statistiques globales de la plateforme")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiquesPlateformeResponse> getStatistiquesPlateforme() {
        return ResponseEntity.ok(statistiqueService.getStatistiquesPlateforme());
    }

    @GetMapping("/plateforme/public")
    @Operation(summary = "Obtenir les statistiques publiques de la plateforme")
    public ResponseEntity<StatistiquesPlateformeResponse> getStatistiquesPlateformePublic() {
        // Retourne un sous-ensemble des statistiques pour les utilisateurs non authentifiés
        StatistiquesPlateformeResponse stats = statistiqueService.getStatistiquesPlateforme();
        // On peut filtrer certaines informations sensibles si nécessaire
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/niveaux/{id}")
    @Operation(summary = "Statistiques pour un niveau spécifique")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiquesNiveauResponse> getStatistiquesNiveau(
            @Parameter(description = "ID du niveau") @PathVariable Long id) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesNiveau(id));
    }

    @GetMapping("/classes/{id}")
    @Operation(summary = "Statistiques pour une classe spécifique")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiquesClasseResponse> getStatistiquesClasse(
            @Parameter(description = "ID de la classe") @PathVariable Long id) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesClasse(id));
    }
}