package com.example.edugo.controller;

import com.example.edugo.dto.ExerciceRequest;
import com.example.edugo.dto.ExerciceResponse;
import com.example.edugo.dto.ExerciceDetailResponse;
import com.example.edugo.dto.FaireExerciceResponse;
import com.example.edugo.service.ServiceExercice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import com.example.edugo.dto.ExerciceSubmissionRequest;
import com.example.edugo.dto.CorrectionRequest;

@RestController
@RequestMapping("/api/exercices")
@RequiredArgsConstructor
@Tag(name = "Exercices", description = "Gestion des exercices et soumissions")
@SecurityRequirement(name = "bearerAuth")
public class ExerciceController {

    private final ServiceExercice serviceExercice;

    // ==================== CRUD EXERCICES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les exercices", description = "Permet de récupérer la liste de tous les exercices")
    public ResponseEntity<List<ExerciceResponse>> getAllExercices() {
        return ResponseEntity.ok(serviceExercice.getAllExercices());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un exercice par ID", description = "Permet de récupérer les détails d'un exercice")
    public ResponseEntity<ExerciceDetailResponse> getExerciceById(@Parameter(description = "ID de l'exercice") @PathVariable Long id) {
        return ResponseEntity.ok(serviceExercice.getExerciceById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Créer un nouvel exercice", description = "Création via multipart: part 'exercice' (JSON), part 'document' (obligatoire), part 'image' (optionnel)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciceResponse> createExercice(@RequestPart("exercice") ExerciceRequest exercice,
                                                           @RequestPart("document") MultipartFile document,
                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        return ResponseEntity.ok(serviceExercice.createExercice(exercice, document, image));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Mettre à jour un exercice", description = "Mise à jour via multipart: part 'exercice' (JSON), part 'document' (optionnel), part 'image' (optionnel)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciceResponse> updateExercice(@Parameter(description = "ID de l'exercice") @PathVariable Long id,
                                                           @RequestPart("exercice") ExerciceRequest exerciceDetails,
                                                           @RequestPart(value = "document", required = false) MultipartFile document,
                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        return ResponseEntity.ok(serviceExercice.updateExercice(id, exerciceDetails, document, image));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un exercice", description = "Permet à un administrateur de supprimer un exercice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExercice(@Parameter(description = "ID de l'exercice") @PathVariable Long id) {
        serviceExercice.deleteExercice(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== EXERCICES POUR ÉLÈVES ====================
    
    @GetMapping("/disponibles/{eleveId}")
    @Operation(summary = "Récupérer les exercices disponibles pour un élève", description = "Permet de récupérer les exercices actifs pour un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<ExerciceResponse>> getExercicesDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceExercice.getExercicesDisponibles(eleveId));
    }

    @GetMapping("/matiere/{matiereId}")
    @Operation(summary = "Récupérer les exercices par matière", description = "Permet de récupérer les exercices d'une matière spécifique")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<ExerciceResponse>> getExercicesByMatiere(@Parameter(description = "ID de la matière") @PathVariable Long matiereId) {
        return ResponseEntity.ok(serviceExercice.getExercicesByMatiere(matiereId));
    }

    @GetMapping("/difficulte/{niveauDifficulte}")
    @Operation(summary = "Récupérer les exercices par niveau de difficulté", description = "Permet de récupérer les exercices d'un niveau de difficulté spécifique")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<ExerciceResponse>> getExercicesByDifficulte(@Parameter(description = "Niveau de difficulté") @PathVariable Integer niveauDifficulte) {
        return ResponseEntity.ok(serviceExercice.getExercicesByDifficulte(niveauDifficulte));
    }

    // ==================== SOUMISSION D'EXERCICES ====================
    
    @PostMapping("/soumettre/{eleveId}/{exerciceId}")
    @Operation(summary = "Soumettre un exercice", description = "Permet à un élève de soumettre sa réponse à un exercice")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<FaireExerciceResponse> soumettreExercice(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                         @Parameter(description = "ID de l'exercice") @PathVariable Long exerciceId,
                                                         @RequestBody ExerciceSubmissionRequest reponseData) {
        String reponse = reponseData.getReponse();
        return ResponseEntity.ok(serviceExercice.soumettreExercice(eleveId, exerciceId, reponse));
    }

    @GetMapping("/historique/{eleveId}")
    @Operation(summary = "Récupérer l'historique des exercices d'un élève", description = "Permet de récupérer l'historique des exercices réalisés par un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<FaireExerciceResponse>> getHistoriqueExercices(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceExercice.getHistoriqueExercices(eleveId));
    }

    @GetMapping("/realise/{eleveId}/{exerciceId}")
    @Operation(summary = "Récupérer un exercice réalisé", description = "Permet de récupérer les détails d'un exercice réalisé par un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<FaireExerciceResponse> getExerciceRealise(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                           @Parameter(description = "ID de l'exercice") @PathVariable Long exerciceId) {
        FaireExerciceResponse exerciceRealise = serviceExercice.getExerciceRealise(eleveId, exerciceId);
        return ResponseEntity.ok(exerciceRealise);
    }

    // ==================== CORRECTION ET ÉVALUATION (ADMIN) ====================
    
    @PostMapping("/corriger/{faireExerciceId}")
    @Operation(summary = "Corriger un exercice", description = "Permet à un administrateur de corriger un exercice soumis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FaireExerciceResponse> corrigerExercice(@Parameter(description = "ID de l'exercice réalisé") @PathVariable Long faireExerciceId,
                                                        @RequestBody CorrectionRequest correction) {
        Integer note = correction.getNote();
        String commentaire = correction.getCommentaire();
        return ResponseEntity.ok(serviceExercice.corrigerExercice(faireExerciceId, note, commentaire));
    }

    @GetMapping("/a-corriger")
    @Operation(summary = "Récupérer les exercices à corriger", description = "Permet de récupérer la liste des exercices soumis en attente de correction")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FaireExerciceResponse>> getExercicesACorriger() {
        return ResponseEntity.ok(serviceExercice.getExercicesACorriger());
    }

    // ==================== STATISTIQUES ET ANALYTICS ====================
    
    @GetMapping("/statistiques/{exerciceId}")
    @Operation(summary = "Récupérer les statistiques d'un exercice", description = "Permet de récupérer les statistiques de réalisation d'un exercice")
    public ResponseEntity<Object> getStatistiquesExercice(@Parameter(description = "ID de l'exercice") @PathVariable Long exerciceId) {
        return ResponseEntity.ok(serviceExercice.getStatistiquesExercice(exerciceId));
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    @GetMapping("/recherche/titre")
    @Operation(summary = "Rechercher des exercices par titre", description = "Permet de rechercher des exercices par titre")
    public ResponseEntity<List<ExerciceResponse>> searchExercicesByTitre(@Parameter(description = "Titre à rechercher") @RequestParam String titre) {
        return ResponseEntity.ok(serviceExercice.searchExercicesByTitre(titre));
    }

    @GetMapping("/livre/{livreId}")
    @Operation(summary = "Récupérer les exercices d'un livre", description = "Permet de récupérer les exercices associés à un livre")
    public ResponseEntity<List<ExerciceResponse>> getExercicesByLivre(@Parameter(description = "ID du livre") @PathVariable Long livreId) {
        return ResponseEntity.ok(serviceExercice.getExercicesByLivre(livreId));
    }
}

