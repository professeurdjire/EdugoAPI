package com.example.edugo.controller;

import com.example.edugo.dto.*;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.entity.User;
import com.example.edugo.service.AdminService;
import com.example.edugo.service.ServiceLivre;
import com.example.edugo.service.ServiceQuiz;
import com.example.edugo.service.ServiceLangue;
import com.example.edugo.service.ServiceEleve;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administration", description = "Endpoints pour la gestion administrative complète de la plateforme (Réservé aux Administrateurs)")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;
    private final com.example.edugo.service.ServiceFichierLivre fichierLivreService;
    private final ServiceLivre serviceLivre;
    private final ServiceLangue serviceLangue;
    private final ServiceQuiz serviceQuiz;
    private final ServiceEleve serviceEleve;



    // ==================== UTILISATEURS ====================
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> request) {
        // Extract user data from request
        String nom = (String) request.get("nom");
        String prenom = (String) request.get("prenom");
        String email = (String) request.get("email");
        String motDePasse = (String) request.get("motDePasse");
        String role = (String) request.get("role");
        
        // For now, return a simple response
        // In a real scenario, you would create Eleve or Admin based on role
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "User creation logic should be implemented based on role");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        User user = adminService.getUserById(id);
        user.setNom((String) request.get("nom"));
        user.setPrenom((String) request.get("prenom"));
        user.setEmail((String) request.get("email"));
        
        if (request.containsKey("estActive")) {
            user.setEstActive((Boolean) request.get("estActive"));
        }
        
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== ÉLÈVES (profils combinés User + Eleve) ====================

    @GetMapping("/eleves")
    @Operation(summary = "Lister tous les élèves avec leurs informations complètes", description = "Retourne une liste de EleveProfileResponse combinant les données User + Eleve + Classe + Niveau")
    public ResponseEntity<List<EleveProfileResponse>> getAllElevesProfils() {
        return ResponseEntity.ok(serviceEleve.listProfilsEleves());
    }


    // Récupérer toutes les langues
    @GetMapping("/langues")
    public ResponseEntity<List<Langue>> getAllLangues() {
        try {
            List<Langue> langues = serviceLangue.getAllLangues();
            return ResponseEntity.ok(langues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer une langue par ID
    @GetMapping("/langues/{id}")
    public ResponseEntity<Langue> getLangueById(@PathVariable Long id) {
        return serviceLangue.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer une nouvelle langue
    @PostMapping("/langues")
    public ResponseEntity<Langue> createLangue(@RequestBody Langue langue) {
        try {
            Langue saved = serviceLangue.save(langue);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour une langue existante
    @PutMapping("/langues/{id}")
    public ResponseEntity<Langue> updateLangue(@PathVariable Long id, @RequestBody Langue langue) {
        return serviceLangue.findById(id)
                .map(existing -> {
                    existing.setNom(langue.getNom()); // par exemple, si Langue a un champ 'nom'
                    // Ajouter ici d'autres champs si nécessaire
                    Langue updated = serviceLangue.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Supprimer une langue
    @DeleteMapping("/langues/{id}")
    public ResponseEntity<Void> deleteLangue(@PathVariable Long id) {
        java.util.Optional<Langue> existing = serviceLangue.findById(id);
        if (existing.isPresent()) {
            serviceLangue.delete(existing.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    // ==================== NIVEAUX ====================
    
    @GetMapping("/niveaux")
    public ResponseEntity<List<NiveauResponse>> getAllNiveaux() {
        return ResponseEntity.ok(adminService.getAllNiveauxDto());
    }

    @GetMapping("/niveaux/{id}")
    public ResponseEntity<NiveauResponse> getNiveauById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getNiveauByIdDto(id));
    }

    @PostMapping("/niveaux")
    public ResponseEntity<NiveauResponse> createNiveau(@RequestBody NiveauRequest niveauRequest) {
        return ResponseEntity.ok(adminService.createNiveauDto(niveauRequest));
    }

    @PutMapping("/niveaux/{id}")
    public ResponseEntity<NiveauResponse> updateNiveau(@PathVariable Long id, @RequestBody NiveauRequest niveauRequest) {
        return ResponseEntity.ok(adminService.updateNiveauDto(id, niveauRequest));
    }

    @DeleteMapping("/niveaux/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        adminService.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CLASSES ====================
    
    @GetMapping("/classes")
    public ResponseEntity<List<ClasseResponse>> getAllClasses() {
        return ResponseEntity.ok(adminService.getAllClassesDto());
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<ClasseResponse> getClasseById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClasseByIdDto(id));
    }

    @PostMapping("/classes")
    public ResponseEntity<ClasseResponse> createClasse(@RequestBody ClasseRequest classeRequest) {
        return ResponseEntity.ok(adminService.createClasseDto(classeRequest));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<ClasseResponse> updateClasse(@PathVariable Long id, @RequestBody ClasseRequest classeRequest) {
        return ResponseEntity.ok(adminService.updateClasseDto(id, classeRequest));
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        adminService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== MATIERES ====================
    
    @GetMapping("/matieres")
    public ResponseEntity<List<MatiereResponse>> getAllMatieres() {
        return ResponseEntity.ok(adminService.getAllMatieresDto());
    }

    @GetMapping("/matieres/{id}")
    public ResponseEntity<MatiereResponse> getMatiereById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getMatiereByIdDto(id));
    }

    @PostMapping("/matieres")
    public ResponseEntity<MatiereResponse> createMatiere(@RequestBody MatiereRequest matiereRequest) {
        return ResponseEntity.ok(adminService.createMatiereDto(matiereRequest));
    }

    @PutMapping("/matieres/{id}")
    public ResponseEntity<MatiereResponse> updateMatiere(@PathVariable Long id, @RequestBody MatiereRequest matiereRequest) {
        return ResponseEntity.ok(adminService.updateMatiereDto(id, matiereRequest));
    }

    @DeleteMapping("/matieres/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        adminService.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LIVRES ====================
    
    @GetMapping("/livres")
    public ResponseEntity<List<LivreResponse>> getAllLivres() {
        return ResponseEntity.ok(adminService.getAllLivresDto());
    }

    @GetMapping("/livres/{id}")
    public ResponseEntity<LivreResponse> getLivreById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getLivreByIdDto(id));
    }

    @PostMapping(value = "/livres", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LivreResponse> createLivre(@RequestPart("livre") com.example.edugo.dto.LivreRequest livre,
                                                     @RequestPart("document") MultipartFile document,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        if (document == null || document.isEmpty()) {
            throw new IllegalArgumentException("Un document (PDF/EPUB/...) est requis pour créer un livre");
        }
        LivreResponse resp = serviceLivre.createLivre(livre, image);
        // upload du document
        fichierLivreService.upload(resp.getId(), document);
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = "/livres/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<LivreResponse> updateLivre(
        @PathVariable Long id,
        @RequestPart("livre") com.example.edugo.dto.LivreRequest livre,
        @RequestPart(value = "document", required = false) MultipartFile document,
        @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

    LivreResponse resp = serviceLivre.updateLivre(id, livre, image);

    if (document != null && !document.isEmpty()) {
        fichierLivreService.upload(resp.getId(), document);
    }

    return ResponseEntity.ok(resp);
}


    @DeleteMapping("/livres/{id}")
    public ResponseEntity<Void> deleteLivre(@PathVariable Long id) {
        adminService.deleteLivre(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== EXERCICES ====================
    
    @GetMapping("/exercices")
    public ResponseEntity<List<ExerciceResponse>> getAllExercices() {
        return ResponseEntity.ok(adminService.getAllExercicesDto());
    }

    @GetMapping("/exercices/{id}")
    public ResponseEntity<ExerciceResponse> getExerciceById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getExerciceByIdDto(id));
    }

    @PostMapping("/exercices")
    public ResponseEntity<ExerciceResponse> createExercice(@RequestBody Exercice exercice) {
        Exercice saved = adminService.createExercice(exercice);
        return ResponseEntity.ok(adminService.getExerciceByIdDto(saved.getId()));
    }

    @PutMapping("/exercices/{id}")
    public ResponseEntity<ExerciceResponse> updateExercice(@PathVariable Long id, @RequestBody Exercice exercice) {
        adminService.updateExercice(id, exercice);
        return ResponseEntity.ok(adminService.getExerciceByIdDto(id));
    }

    @DeleteMapping("/exercices/{id}")
    public ResponseEntity<Void> deleteExercice(@PathVariable Long id) {
        adminService.deleteExercice(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== DEFIS ====================
    
    @GetMapping("/defis")
    public ResponseEntity<List<DefiResponse>> getAllDefis() {
        return ResponseEntity.ok(adminService.getAllDefisDto());
    }

    @GetMapping("/defis/{id}")
    public ResponseEntity<DefiResponse> getDefiById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDefiByIdDto(id));
    }

    @PostMapping("/defis")
    public ResponseEntity<DefiResponse> createDefi(@RequestBody Defi defi) {
        Defi saved = adminService.createDefi(defi);
        return ResponseEntity.ok(adminService.getDefiByIdDto(saved.getId()));
    }

    @PutMapping("/defis/{id}")
    public ResponseEntity<DefiResponse> updateDefi(@PathVariable Long id, @RequestBody Defi defi) {
        adminService.updateDefi(id, defi);
        return ResponseEntity.ok(adminService.getDefiByIdDto(id));
    }

    @DeleteMapping("/defis/{id}")
    public ResponseEntity<Void> deleteDefi(@PathVariable Long id) {
        adminService.deleteDefi(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CHALLENGES ====================
    
    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
        return ResponseEntity.ok(adminService.getAllChallengesDto());
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeResponse> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getChallengeByIdDto(id));
    }

    @PostMapping("/challenges")
    public ResponseEntity<ChallengeResponse> createChallenge(@RequestBody Challenge challenge) {
        Challenge saved = adminService.createChallenge(challenge);
        return ResponseEntity.ok(adminService.getChallengeByIdDto(saved.getId()));
    }

    @PutMapping("/challenges/{id}")
    public ResponseEntity<ChallengeResponse> updateChallenge(@PathVariable Long id, @RequestBody Challenge challenge) {
        adminService.updateChallenge(id, challenge);
        return ResponseEntity.ok(adminService.getChallengeByIdDto(id));
    }

    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        adminService.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== BADGES ====================
    
    @GetMapping("/badges")
    public ResponseEntity<List<BadgeResponse>> getAllBadges() {
        return ResponseEntity.ok(adminService.getAllBadgesDto());
    }

    @GetMapping("/badges/{id}")
    public ResponseEntity<BadgeResponse> getBadgeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getBadgeByIdDto(id));
    }

    @PostMapping("/badges")
    public ResponseEntity<BadgeResponse> createBadge(@RequestBody Badge badge) {
        Badge saved = adminService.createBadge(badge);
        return ResponseEntity.ok(adminService.getBadgeByIdDto(saved.getId()));
    }

    @PutMapping("/badges/{id}")
    public ResponseEntity<BadgeResponse> updateBadge(@PathVariable Long id, @RequestBody Badge badge) {
        adminService.updateBadge(id, badge);
        return ResponseEntity.ok(adminService.getBadgeByIdDto(id));
    }

    @DeleteMapping("/badges/{id}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id) {
        adminService.deleteBadge(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== QUIZZES ====================
    
    @GetMapping("/quizzes")
    public ResponseEntity<List<com.example.edugo.dto.QuizResponse>> getAllQuizzes() {
        return ResponseEntity.ok(serviceQuiz.getAllQuizzesDto());
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<com.example.edugo.dto.QuizResponse> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceQuiz.getQuizByIdDto(id));
    }

    @PostMapping("/quizzes")
    public ResponseEntity<com.example.edugo.dto.QuizResponse> createQuiz(@RequestBody com.example.edugo.dto.QuizCreateRequest request) {
        return ResponseEntity.ok(serviceQuiz.createQuiz(request));
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<com.example.edugo.dto.QuizResponse> updateQuiz(@PathVariable Long id, @RequestBody com.example.edugo.entity.Principales.Quiz quiz) {
        return ResponseEntity.ok(serviceQuiz.updateQuiz(id, quiz));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        adminService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PARTENAIRES ====================
    
    @GetMapping("/partenaires")
    public ResponseEntity<List<PartenaireResponse>> getAllPartenaires() {
        return ResponseEntity.ok(adminService.getAllPartenairesDto());
    }

    @GetMapping("/partenaires/{id}")
    public ResponseEntity<PartenaireResponse> getPartenaireById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getPartenaireByIdDto(id));
    }

    @PostMapping("/partenaires")
    public ResponseEntity<PartenaireResponse> createPartenaire(@RequestBody PartenaireRequest partenaireRequest) {
        return ResponseEntity.ok(adminService.createPartenaireDto(partenaireRequest));
    }

    @PutMapping("/partenaires/{id}")
    public ResponseEntity<PartenaireResponse> updatePartenaire(@PathVariable Long id, @RequestBody PartenaireRequest partenaireRequest) {
        return ResponseEntity.ok(adminService.updatePartenaireDto(id, partenaireRequest));
    }

    @DeleteMapping("/partenaires/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        adminService.deletePartenaire(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== STATISTIQUES ====================
    
    @GetMapping("/statistiques/plateforme")
    public ResponseEntity<StatistiquesPlateformeResponse> getStatistiquesPlateforme() {
        return ResponseEntity.ok(adminService.getStatistiquesPlateforme());
    }
}

