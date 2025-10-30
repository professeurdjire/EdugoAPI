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

@Service
@RequiredArgsConstructor
public class ServiceBadge {

    private final BadgeRepository badgeRepository;
    private final EleveRepository eleveRepository;
    private final EleveDefiRepository eleveDefiRepository;
    private final ParticipationRepository participationRepository;
    private final ProgressionRepository progressionRepository;

    // ==================== CRUD BADGES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Badge createBadge(Badge badge) {
        if (badgeRepository.existsByNom(badge.getNom())) {
            throw new RuntimeException("Ce badge existe déjà");
        }
        return badgeRepository.save(badge);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Badge updateBadge(Long id, Badge badgeDetails) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        
        badge.setNom(badgeDetails.getNom());
        badge.setDescription(badgeDetails.getDescription());
        badge.setType(badgeDetails.getType());
        badge.setIcone(badgeDetails.getIcone());
        
        return badgeRepository.save(badge);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteBadge(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        badgeRepository.delete(badge);
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
    }

    // ==================== BADGES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Badge> getBadgesDisponibles(Long eleveId) {
        return badgeRepository.findAll();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Badge> getBadgesByType(TypeBadge typeBadge) {
        return badgeRepository.findByType(typeBadge);
    }

    // ==================== ATTRIBUTION AUTOMATIQUE DE BADGES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public void verifierEtAttribuerBadges(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Badge "Premier exercice"
        if (eleve.getFaireExercices() != null && !eleve.getFaireExercices().isEmpty()) {
            attribuerBadgeSiNecessaire(eleveId, "Premier exercice");
        }
        
        // Badge "Premier défi"
        List<EleveDefi> defisParticipes = eleveDefiRepository.findByEleveId(eleveId);
        if (!defisParticipes.isEmpty()) {
            attribuerBadgeSiNecessaire(eleveId, "Premier défi");
        }
        
        // Badge "Premier challenge"
        List<Participation> challengesParticipes = participationRepository.findByEleveId(eleveId);
        if (!challengesParticipes.isEmpty()) {
            attribuerBadgeSiNecessaire(eleveId, "Premier challenge");
        }
        
        // Badge "Lecteur assidu" (basé sur la progression)
        List<Progression> progressions = progressionRepository.findByEleveId(eleveId);
        if (progressions.size() >= 5) {
            attribuerBadgeSiNecessaire(eleveId, "Lecteur assidu");
        }
        
        // Badge "Points élevés"
        if (eleve.getPointAccumule() >= 1000) {
            attribuerBadgeSiNecessaire(eleveId, "Points élevés");
        }
        
        // Badge "Défis multiples"
        if (defisParticipes.size() >= 10) {
            attribuerBadgeSiNecessaire(eleveId, "Défis multiples");
        }
    }

    private void attribuerBadgeSiNecessaire(Long eleveId, String nomBadge) {
        // Cette méthode devrait vérifier si l'élève a déjà ce badge
        // et l'attribuer s'il ne l'a pas encore
        // Pour l'instant, c'est une implémentation simplifiée
    }

    // ==================== STATISTIQUES BADGES ====================
    
    public Object getStatistiquesBadge(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", badgeId));
        
        // Cette méthode devrait compter combien d'élèves ont ce badge
        // Pour l'instant, retournons des données de base
        
        return new Object() {
            public final Long badgeId = badge.getId();
            public final String nomBadge = badge.getNom();
            public final String description = badge.getDescription();
            public final TypeBadge type = badge.getType();
            public final String icone = badge.getIcone();
            public final Integer nombreAttributions = 0; // À implémenter
        };
    }

    // ==================== BADGES LES PLUS POPULAIRES ====================
    
    public List<Object> getBadgesPopulaires() {
        // Cette méthode devrait retourner les badges les plus attribués
        // Pour l'instant, retournons tous les badges avec des statistiques fictives
        
        List<Badge> badges = badgeRepository.findAll();
        
        return badges.stream()
                .map(badge -> new Object() {
                    public final Long badgeId = badge.getId();
                    public final String nomBadge = badge.getNom();
                    public final String description = badge.getDescription();
                    public final TypeBadge type = badge.getType();
                    public final Integer nombreAttributions = 0; // À implémenter
                })
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Badge> searchBadgesByName(String nom) {
        return badgeRepository.findByNomContainingIgnoreCase(nom);
    }

    // removed duplicate getBadgesByType

    // ==================== VALIDATION BADGES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    public boolean validerBadge(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", badgeId));
        
        // Vérifier si le badge a toutes les informations nécessaires
        return badge.getNom() != null && !badge.getNom().isEmpty() &&
               badge.getDescription() != null && !badge.getDescription().isEmpty() &&
               badge.getType() != null;
    }

    // ==================== CRÉATION DE BADGES SPÉCIAUX ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Badge createBadgeSpecial(String nom, String description, TypeBadge type, String icone) {
        Badge badge = new Badge();
        badge.setNom(nom);
        badge.setDescription(description);
        badge.setType(type);
        badge.setIcone(icone);
        
        return badgeRepository.save(badge);
    }

    // ==================== BADGES DE CLASSEMENT ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Object> getClassementBadges() {
        List<Eleve> eleves = eleveRepository.findAll();
        
        return eleves.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getPointAccumule(), e1.getPointAccumule()))
                .limit(10)
                .map(eleve -> new Object() {
                    public final Long eleveId = eleve.getId();
                    public final String nom = eleve.getNom();
                    public final String prenom = eleve.getPrenom();
                    public final Integer points = eleve.getPointAccumule();
                    public final Integer position = eleves.indexOf(eleve) + 1;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}

