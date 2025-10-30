package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceDefi {

    private final DefiRepository defiRepository;
    private final EleveDefiRepository eleveDefiRepository;
    private final EleveRepository eleveRepository;
    private final BadgeRepository badgeRepository;

    // ==================== CRUD DÉFIS ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Defi createDefi(Defi defi) {
        return defiRepository.save(defi);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Defi updateDefi(Long id, Defi defiDetails) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        
        defi.setTitre(defiDetails.getTitre());
        defi.setEnnonce(defiDetails.getEnnonce());
        defi.setPointDefi(defiDetails.getPointDefi());
        defi.setTypeDefi(defiDetails.getTypeDefi());
        
        return defiRepository.save(defi);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteDefi(Long id) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        defiRepository.delete(defi);
    }

    public List<Defi> getAllDefis() {
        return defiRepository.findAll();
    }

    public Defi getDefiById(Long id) {
        return defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
    }

    // ==================== DÉFIS POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Defi> getDefisDisponibles(Long eleveId) {
        return defiRepository.findAll();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Defi> getDefisByType(TypeDefi typeDefi) {
        return defiRepository.findByTypeDefi(typeDefi.name());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Defi> getDefisByPoints(Integer pointsMin, Integer pointsMax) {
        // Fallback: filtre côté mémoire si méthode non définie au repo
        return defiRepository.findAll().stream()
                .filter(d -> d.getPointDefi() != null && d.getPointDefi() >= pointsMin && d.getPointDefi() <= pointsMax)
                .toList();
    }

    // ==================== PARTICIPATION AUX DÉFIS ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public EleveDefi participerDefi(Long eleveId, Long defiId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Defi defi = defiRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", defiId));
        
        // Vérifier si l'élève n'a pas déjà participé à ce défi
        Optional<EleveDefi> existingParticipation = eleveDefiRepository
                .findByEleveIdAndDefiId(eleveId, defiId);
        
        if (existingParticipation.isPresent()) {
            throw new RuntimeException("Vous avez déjà participé à ce défi");
        }
        
        EleveDefi eleveDefi = new EleveDefi();
        eleveDefi.setEleve(eleve);
        eleveDefi.setDefi(defi);
        eleveDefi.setDateEnvoie(LocalDateTime.now());
        
        return eleveDefiRepository.save(eleveDefi);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<EleveDefi> getDefisParticipes(Long eleveId) {
        return eleveDefiRepository.findByEleveId(eleveId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public EleveDefi getParticipationDefi(Long eleveId, Long defiId) {
        return eleveDefiRepository.findByEleveIdAndDefiId(eleveId, defiId)
                .orElse(null);
    }

    // ==================== VALIDATION ET RÉCOMPENSES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EleveDefi validerDefi(Long eleveDefiId, String commentaire) {
        EleveDefi eleveDefi = eleveDefiRepository.findById(eleveDefiId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation au défi", eleveDefiId));
        
        // pas de champs statut/commentaire/dateValidation sur EleveDefi actuel
        
        // Ajouter les points à l'élève
        Eleve eleve = eleveDefi.getEleve();
        eleve.setPointAccumule(eleve.getPointAccumule() + eleveDefi.getDefi().getPointDefi());
        eleveRepository.save(eleve);
        
        return eleveDefiRepository.save(eleveDefi);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EleveDefi rejeterDefi(Long eleveDefiId, String commentaire) {
        EleveDefi eleveDefi = eleveDefiRepository.findById(eleveDefiId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation au défi", eleveDefiId));
        
        // pas de champs statut/commentaire/dateValidation
        
        return eleveDefiRepository.save(eleveDefi);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EleveDefi> getDefisAValider() {
        // pas de statut en base: retourner toutes les participations pour revue
        return eleveDefiRepository.findAll();
    }

    // ==================== STATISTIQUES DÉFIS ====================
    
    public Object getStatistiquesDefi(Long defiId) {
        Defi defi = defiRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", defiId));
        
        List<EleveDefi> participations = eleveDefiRepository.findByDefiId(defiId);
        
        return new Object() {
            public final Long defiId = defi.getId();
            public final String titre = defi.getTitre();
            public final Integer nombreParticipations = participations.size();
            public final Integer nombreValidations = (int) participations.stream()
                    .filter(p -> "VALIDE".equals(p.getStatut()))
                    .count();
            public final Integer nombreRejets = (int) participations.stream()
                    .filter(p -> "REJETE".equals(p.getStatut()))
                    .count();
            public final Double tauxReussite = participations.isEmpty() ? 0.0 :
                    (double) participations.stream()
                            .filter(p -> "VALIDE".equals(p.getStatut()))
                            .count() / participations.size() * 100;
        };
    }

    // ==================== CLASSEMENT ET LEADERBOARD ====================
    
    public List<Object> getLeaderboardDefis() {
        List<EleveDefi> participationsValidees = eleveDefiRepository.findByStatut("VALIDE");
        
        return participationsValidees.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        EleveDefi::getEleve,
                        java.util.stream.Collectors.summingInt(p -> p.getDefi().getPointDefi())
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(entry -> new Object() {
                    public final Long eleveId = entry.getKey().getId();
                    public final String nom = entry.getKey().getNom();
                    public final String prenom = entry.getKey().getPrenom();
                    public final Integer pointsDefis = entry.getValue();
                })
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Defi> searchDefisByTitre(String titre) {
        return defiRepository.searchByTitreOrEnonce(titre);
    }

    public List<Defi> getDefisByPointsMin(Integer pointsMin) {
        return defiRepository.findAll().stream().filter(d -> d.getPointDefi() != null && d.getPointDefi() >= pointsMin).toList();
    }
}
