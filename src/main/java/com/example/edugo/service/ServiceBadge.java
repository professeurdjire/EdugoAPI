package com.example.edugo.service;

import com.example.edugo.dto.BadgeRequest;
import com.example.edugo.dto.BadgeResponse;
import com.example.edugo.entity.Principales.Badge;
import com.example.edugo.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceBadge {

    private final BadgeRepository badgeRepository;

    // -------------------- CREATE --------------------
    public BadgeResponse createBadge(BadgeRequest request) {
        Badge badge = new Badge();
        badge.setNom(request.getNom());
        badge.setDescription(request.getDescription());
        badge.setType(request.getType());
        badge.setIcone(request.getIcone());

        Badge savedBadge = badgeRepository.save(badge);
        return mapToResponse(savedBadge);
    }

    // -------------------- READ --------------------
    public List<BadgeResponse> getAllBadges() {
        return badgeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BadgeResponse getBadgeById(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + id));
        return mapToResponse(badge);
    }

    // -------------------- UPDATE --------------------
    public BadgeResponse updateBadge(Long id, BadgeRequest request) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + id));

        badge.setNom(request.getNom());
        badge.setDescription(request.getDescription());
        badge.setType(request.getType());
        badge.setIcone(request.getIcone());

        Badge updatedBadge = badgeRepository.save(badge);
        return mapToResponse(updatedBadge);
    }

    // -------------------- DELETE --------------------
    public void deleteBadge(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + id));
        badgeRepository.delete(badge);
    }

    // -------------------- MAPPER --------------------
    private BadgeResponse mapToResponse(Badge badge) {
        return new BadgeResponse(
                badge.getId(),
                badge.getNom(),
                badge.getDescription(),
                badge.getType(),
                badge.getIcone()
        );
    }
}
