package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.dto.*;
import com.example.edugo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import com.example.edugo.dto.ChangePasswordRequest;
import com.example.edugo.dto.ProgressionUpdateRequest;
import com.example.edugo.dto.ExerciceSubmissionRequest;

@RestController
@RequestMapping("/api/eleve")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
@Tag(name = "Élève", description = "Endpoints pour les fonctionnalités élèves de la plateforme")
@SecurityRequirement(name = "bearerAuth")
public class EleveController {

    private final ServiceEleve serviceEleve;
    private final ServiceLivre serviceLivre;
    private final ServiceExercice serviceExercice;
    private final ServiceChallenge serviceChallenge;
    private final ServiceDefi serviceDefi;
    private final ServiceBadge serviceBadge;
    private final ServiceMatiere serviceMatiere;

    // ==================== PROFIL ====================
    
    @GetMapping("/profil/{id}")
    @Operation(summary = "Récupérer le profil d'un élève", description = "Permet de récupérer les informations de profil d'un élève")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
        @ApiResponse(responseCode = "404", description = "Élève non trouvé")
    })
    public ResponseEntity<Eleve> getProfil(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceEleve.getProfil(id));
    }

    @PutMapping("/profil/{id}")
    @Operation(summary = "Mettre à jour le profil d'un élève", description = "Permet de modifier les informations de profil d'un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<Eleve> updateProfil(@Parameter(description = "ID de l'élève") @PathVariable Long id, 
                                           @RequestBody Eleve eleveDetails) {
        return ResponseEntity.ok(serviceEleve.updateProfil(id, eleveDetails));
    }

    @PostMapping("/profil/{id}/change-password")
    @Operation(summary = "Changer le mot de passe", description = "Permet à un élève de changer son mot de passe")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<Map<String, String>> changePassword(@Parameter(description = "ID de l'élève") @PathVariable Long id,
                                                            @RequestBody ChangePasswordRequest passwordData) {
        String oldPassword = passwordData.getOldPassword();
        String newPassword = passwordData.getNewPassword();
        
        serviceEleve.changePassword(id, oldPassword, newPassword);
        
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    // ==================== POINTS ET BADGES ====================
    
    @GetMapping("/points/{id}")
    @Operation(summary = "Récupérer les points d'un élève", description = "Permet de récupérer le nombre de points accumulés par un élève")
    public ResponseEntity<Map<String, Integer>> getPoints(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        Integer points = serviceEleve.getPoints(id);
        return ResponseEntity.ok(Map.of("points", points));
    }

    @GetMapping("/badges/{id}")
    @Operation(summary = "Récupérer les badges d'un élève", description = "Permet de récupérer les badges obtenus par un élève")
    public ResponseEntity<List<Badge>> getBadges(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceEleve.getBadges(id));
    }

    @GetMapping("/statistiques/{id}")
    @Operation(summary = "Récupérer les statistiques d'un élève", description = "Permet de récupérer les statistiques complètes d'un élève")
    public ResponseEntity<Object> getStatistiques(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceEleve.getStatistiques(id));
    }

    // ==================== LIVRES ====================
    
    @GetMapping("/livres/disponibles/{id}")
    @Operation(summary = "Récupérer les livres disponibles pour un élève", description = "Permet de récupérer les livres adaptés au niveau d'un élève")
    public ResponseEntity<List<LivreResponse>> getLivresDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceLivre.getLivresDisponibles(id));
    }

    @GetMapping("/livres/{id}")
    @Operation(summary = "Récupérer un livre par ID", description = "Permet de récupérer les détails d'un livre")
    public ResponseEntity<LivreDetailResponse> getLivreById(@Parameter(description = "ID du livre") @PathVariable Long id) {
        return ResponseEntity.ok(serviceLivre.getLivreById(id));
    }

    @PostMapping("/progression/{eleveId}/{livreId}")
    @Operation(summary = "Mettre à jour la progression de lecture", description = "Permet de mettre à jour la page actuelle de lecture d'un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<com.example.edugo.dto.ProgressionResponse> updateProgressionLecture(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                              @Parameter(description = "ID du livre") @PathVariable Long livreId,
                                                              @RequestBody ProgressionUpdateRequest progressionData) {
        Integer pageActuelle = progressionData.getPageActuelle();
        return ResponseEntity.ok(serviceLivre.updateProgressionLecture(eleveId, livreId, pageActuelle));
    }

    @GetMapping("/progression/{id}")
    @Operation(summary = "Récupérer la progression de lecture d'un élève", description = "Permet de récupérer l'historique de lecture d'un élève")
    public ResponseEntity<java.util.List<com.example.edugo.dto.ProgressionResponse>> getProgressionLecture(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceLivre.getProgressionLecture(id));
    }

    // ==================== EXERCICES ====================
    
    @GetMapping("/exercices/disponibles/{id}")
    @Operation(summary = "Récupérer les exercices disponibles pour un élève", description = "Permet de récupérer les exercices actifs pour un élève")
    public ResponseEntity<List<ExerciceResponse>> getExercicesDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceExercice.getExercicesDisponibles(id));
    }

    @GetMapping("/exercices/{id}")
    @Operation(summary = "Récupérer un exercice par ID", description = "Permet de récupérer les détails d'un exercice")
    public ResponseEntity<ExerciceDetailResponse> getExerciceById(@Parameter(description = "ID de l'exercice") @PathVariable Long id) {
        return ResponseEntity.ok(serviceExercice.getExerciceById(id));
    }

    @PostMapping("/exercices/soumettre/{eleveId}/{exerciceId}")
    @Operation(summary = "Soumettre un exercice", description = "Permet à un élève de soumettre sa réponse à un exercice")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<FaireExerciceResponse> soumettreExercice(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                          @Parameter(description = "ID de l'exercice") @PathVariable Long exerciceId,
                                                          @RequestBody ExerciceSubmissionRequest reponseData) {
        String reponse = reponseData.getReponse();
        return ResponseEntity.ok(serviceExercice.soumettreExercice(eleveId, exerciceId, reponse));
    }

    @GetMapping("/exercices/historique/{id}")
    @Operation(summary = "Récupérer l'historique des exercices d'un élève", description = "Permet de récupérer l'historique des exercices réalisés par un élève")
    public ResponseEntity<List<FaireExerciceResponse>> getHistoriqueExercices(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceExercice.getHistoriqueExercices(id));
    }

    // ==================== DÉFIS ====================
    
    @GetMapping("/defis/disponibles/{id}")
    @Operation(summary = "Récupérer les défis disponibles pour un élève", description = "Permet de récupérer les défis disponibles pour un élève")
    public ResponseEntity<List<DefiResponse>> getDefisDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceDefi.getDefisDisponibles(id));
    }

    @GetMapping("/defis/{id}")
    @Operation(summary = "Récupérer un défi par ID", description = "Permet de récupérer les détails d'un défi")
    public ResponseEntity<DefiDetailResponse> getDefiById(@Parameter(description = "ID du défi") @PathVariable Long id) {
        return ResponseEntity.ok(serviceDefi.getDefiById(id));
    }

    @PostMapping("/defis/participer/{eleveId}/{defiId}")
    @Operation(summary = "Participer à un défi", description = "Permet à un élève de participer à un défi")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<EleveDefiResponse> participerDefi(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                   @Parameter(description = "ID du défi") @PathVariable Long defiId) {
        return ResponseEntity.ok(serviceDefi.participerDefi(eleveId, defiId));
    }

    @GetMapping("/defis/participes/{id}")
    @Operation(summary = "Récupérer les défis participés par un élève", description = "Permet de récupérer l'historique des défis d'un élève")
    public ResponseEntity<List<EleveDefiResponse>> getDefisParticipes(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceDefi.getDefisParticipes(id));
    }

    // ==================== CHALLENGES ====================
    
    @GetMapping("/challenges/disponibles/{id}")
    @Operation(summary = "Récupérer les challenges disponibles pour un élève", description = "Permet de récupérer les challenges actifs pour un élève")
    public ResponseEntity<List<ChallengeResponse>> getChallengesDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceChallenge.getChallengesDisponibles(id));
    }

    @GetMapping("/challenges/{id}")
    @Operation(summary = "Récupérer un challenge par ID", description = "Permet de récupérer les détails d'un challenge")
    public ResponseEntity<Challenge> getChallengeById(@Parameter(description = "ID du challenge") @PathVariable Long id) {
        return ResponseEntity.ok(serviceChallenge.getChallengeById(id));
    }

    @PostMapping("/challenges/participer/{eleveId}/{challengeId}")
    @Operation(summary = "Participer à un challenge", description = "Permet à un élève de participer à un challenge")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<Participation> participerChallenge(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                             @Parameter(description = "ID du challenge") @PathVariable Long challengeId) {
        return ResponseEntity.ok(serviceChallenge.participerChallenge(eleveId, challengeId));
    }

    @GetMapping("/challenges/participes/{id}")
    @Operation(summary = "Récupérer les challenges participés par un élève", description = "Permet de récupérer l'historique des challenges d'un élève")
    public ResponseEntity<List<Participation>> getChallengesParticipes(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceChallenge.getChallengesParticipes(id));
    }

    // ==================== CAMARADES DE CLASSE ====================
    
    @GetMapping("/camarades/{id}")
    @Operation(summary = "Récupérer les camarades de classe d'un élève", description = "Permet de récupérer la liste des élèves de la même classe")
    public ResponseEntity<List<Eleve>> getCamaradesClasse(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceEleve.getCamaradesClasse(id));
    }
}