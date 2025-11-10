package com.example.edugo.controller;

import com.example.edugo.dto.EleveRequest;
import com.example.edugo.dto.EleveResponse;
import com.example.edugo.service.ServiceEleve;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@CrossOrigin(origins = "*")
public class EleveController {

    private final ServiceEleve eleveService;

    @Autowired
    public EleveController(ServiceEleve eleveService) {
        this.eleveService = eleveService;
    }

    //  Lister tous les élèves (réservé à l'ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EleveResponse>> getAllEleves() {
        return ResponseEntity.ok(eleveService.getAllEleves());
    }

    //  Obtenir les informations d’un élève (ADMIN ou l’élève lui-même)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ELEVE')")
    public ResponseEntity<EleveResponse> getEleveById(@PathVariable Long id, Principal principal) {
        // Si l'utilisateur est un élève, il ne peut consulter que son propre profil
        if (!principal.getName().equalsIgnoreCase(eleveService.getEmailById(id))
                && !eleveService.isAdmin(principal.getName())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(eleveService.getEleveById(id));
    }

    // Créer un compte élève (accessible sans authentification)
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<EleveResponse> createEleve(@Valid @RequestBody EleveRequest request) {
        return ResponseEntity.ok(eleveService.createEleve(request));
    }

    //  Modifier son propre compte (réservé à l'élève lui-même)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<EleveResponse> updateEleve(@PathVariable Long id,
                                                     @Valid @RequestBody EleveRequest request,
                                                     Principal principal) {
        // Vérifie que l’élève connecté modifie bien son propre profil
        if (!principal.getName().equalsIgnoreCase(eleveService.getEmailById(id))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(eleveService.updateEleve(id, request));
    }

    // Supprimer un élève (réservé à l'ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEleve(@PathVariable Long id) {
        eleveService.deleteEleve(id);
        return ResponseEntity.noContent().build();
    }
}
