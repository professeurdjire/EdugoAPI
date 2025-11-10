package com.example.edugo.service;

import com.example.edugo.dto.BadgeRequest;
import com.example.edugo.dto.BadgeResponse;
import com.example.edugo.dto.StatistiquesBadgeResponse;
import com.example.edugo.dto.ClassementEleveResponse;
import com.example.edugo.entity.Principales.Badge;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.EleveDefi;
import com.example.edugo.entity.Principales.Participation;
import com.example.edugo.entity.Principales.Progression;
import com.example.edugo.entity.Principales.TypeBadge;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceBadge {

    private final BadgeRepository badgeRepository;
    private final EleveRepository eleveRepository;
    private final EleveDefiRepository eleveDefiRepository;
    private final ParticipationRepository participationRepository;
    private final ProgressionRepository progressionRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private BadgeResponse toResponse(Badge badge) {
        if (badge == null) return null;
        return new BadgeResponse(
                badge.getId(),
                badge.getNom(),
                badge.getDescription(),
                badge.getType() != null ? badge.getType().name() : null,
                badge.getIcone()
        );
    }

    private Badge toEntity(BadgeRequest dto) {
        if (dto == null) return null;
        Badge badge = new Badge();
        badge.setNom(dto.getNom());
        badge.setDescription(dto.getDescription());
        badge.setType(dto.getType());
        badge.setIcone(dto.getIcone());
        return badge;
    }

    // ====== CRUD DTO ======
    public List<BadgeResponse> getAllBadgesDto() {
        return badgeRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BadgeResponse getBadgeByIdDto(Long id) {
        Badge badge = getBadgeById(id);
        return toResponse(badge);
    }

    // Entity-level helper to fetch a Badge by id
    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BadgeResponse createBadgeDto(BadgeRequest dto) {
        if (badgeRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Ce badge existe déjà");
        }
        Badge badge = toEntity(dto);
        Badge saved = badgeRepository.save(badge);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BadgeResponse updateBadgeDto(Long id, BadgeRequest dto) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        badge.setNom(dto.getNom());
        badge.setDescription(dto.getDescription());
        badge.setType(dto.getType());
        badge.setIcone(dto.getIcone());
        Badge saved = badgeRepository.save(badge);
        return toResponse(saved);
    }

    public List<BadgeResponse> getBadgesByTypeDto(String type) {
        // TypeBadge est un enum, on convertit le string
        TypeBadge typeBadge = null;
        try {
            typeBadge = TypeBadge.valueOf(type);
        } catch (Exception e) {}
        if (typeBadge == null) return List.of();
        return badgeRepository.findByType(typeBadge).stream().map(this::toResponse).toList();
    }

    // ==================== STATISTIQUES BADGES ====================
    
    public StatistiquesBadgeResponse getStatistiquesBadge(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", badgeId));
        // For now number of attributions is not calculated
        return new StatistiquesBadgeResponse(
                badge.getId(),
                badge.getNom(),
                badge.getDescription(),
                badge.getType() != null ? badge.getType().name() : null,
                badge.getIcone(),
                0
        );
    }

    // ==================== BADGES LES PLUS POPULAIRES ====================
    
    public List<StatistiquesBadgeResponse> getBadgesPopulaires() {
        List<Badge> badges = badgeRepository.findAll();
        return badges.stream()
                .map(badge -> new StatistiquesBadgeResponse(
                        badge.getId(),
                        badge.getNom(),
                        badge.getDescription(),
                        badge.getType() != null ? badge.getType().name() : null,
                        badge.getIcone(),
                        0
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Badge> searchBadgesByName(String nom) {
        return badgeRepository.findByNomContainingIgnoreCase(nom);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteBadge(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        badgeRepository.delete(badge);
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
    public List<ClassementEleveResponse> getClassementBadges() {
        List<Eleve> eleves = eleveRepository.findAll();
        return eleves.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getPointAccumule(), e1.getPointAccumule()))
                .limit(10)
                .map(eleve -> new ClassementEleveResponse(
                        eleve.getId(),
                        eleve.getNom(),
                        eleve.getPrenom(),
                        eleve.getPointAccumule(),
                        eleves.indexOf(eleve) + 1
                ))
                .collect(java.util.stream.Collectors.toList());
    }
}

