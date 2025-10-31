package com.example.edugo.service;

import com.example.edugo.dto.DefiRequest;
import com.example.edugo.dto.DefiResponse;
import com.example.edugo.dto.DefiDetailResponse;
import com.example.edugo.dto.EleveDefiResponse;
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
import java.util.stream.Collectors;

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
    public DefiResponse createDefi(DefiRequest dto) {
        Defi defi = new Defi();
        defi.setTitre(dto.getTitre());
        defi.setEnnonce(dto.getEnnonce());
        defi.setPointDefi(dto.getPointDefi());
        defi.setTypeDefi(dto.getTypeDefi());
        return toResponse(defiRepository.save(defi));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DefiResponse updateDefi(Long id, DefiRequest dto) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        defi.setTitre(dto.getTitre());
        defi.setEnnonce(dto.getEnnonce());
        defi.setPointDefi(dto.getPointDefi());
        defi.setTypeDefi(dto.getTypeDefi());
        return toResponse(defiRepository.save(defi));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteDefi(Long id) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        defiRepository.delete(defi);
    }

    public List<DefiResponse> getAllDefis() {
        return defiRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public DefiDetailResponse getDefiById(Long id) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        return toDetailResponse(defi);
    }

    // ==================== DÉFIS POUR ÉLÈVES ====================
    @PreAuthorize("hasRole('ELEVE')")
    public List<DefiResponse> getDefisDisponibles(Long eleveId) {
        return defiRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<DefiResponse> getDefisByType(String typeDefi) {
        return defiRepository.findByTypeDefi(typeDefi).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<DefiResponse> getDefisByPoints(Integer pointsMin, Integer pointsMax) {
        return defiRepository.findAll().stream()
                .filter(d -> d.getPointDefi() != null && d.getPointDefi() >= pointsMin && d.getPointDefi() <= pointsMax)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ==================== PARTICIPATION AUX DÉFIS ====================
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public EleveDefiResponse participerDefi(Long eleveId, Long defiId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        Defi defi = defiRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", defiId));
        Optional<EleveDefi> existingParticipation = eleveDefiRepository.findByEleveIdAndDefiId(eleveId, defiId);
        if (existingParticipation.isPresent()) {
            throw new RuntimeException("Vous avez déjà participé à ce défi");
        }
        EleveDefi eleveDefi = new EleveDefi();
        eleveDefi.setEleve(eleve);
        eleveDefi.setDefi(defi);
        eleveDefi.setDateEnvoie(LocalDateTime.now());
        return toEleveDefiResponse(eleveDefiRepository.save(eleveDefi));
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<EleveDefiResponse> getDefisParticipes(Long eleveId) {
        return eleveDefiRepository.findByEleveId(eleveId).stream()
                .map(this::toEleveDefiResponse)
                .collect(Collectors.toList());
    }

    // ==================== MAPPING ====================
    private DefiResponse toResponse(Defi defi) {
        DefiResponse dto = new DefiResponse();
        dto.setId(defi.getId());
        dto.setTitre(defi.getTitre());
        dto.setPointDefi(defi.getPointDefi());
        dto.setDateAjout(defi.getDateAjout());
        dto.setNbreParticipations(defi.getNbreParticipations());
        if (defi.getClasse() != null) {
            dto.setClasseId(defi.getClasse().getId());
            dto.setClasseNom(defi.getClasse().getNom());
        }
        dto.setTypeDefi(defi.getTypeDefi());
        return dto;
    }

    private DefiDetailResponse toDetailResponse(Defi defi) {
        DefiDetailResponse dto = new DefiDetailResponse();
        dto.setId(defi.getId());
        dto.setTitre(defi.getTitre());
        dto.setEnnonce(defi.getEnnonce());
        dto.setPointDefi(defi.getPointDefi());
        dto.setDateAjout(defi.getDateAjout());
        dto.setNbreParticipations(defi.getNbreParticipations());
        if (defi.getClasse() != null) {
            dto.setClasseId(defi.getClasse().getId());
            dto.setClasseNom(defi.getClasse().getNom());
        }
        dto.setTypeDefi(defi.getTypeDefi());
        dto.setReponseDefi(defi.getReponseDefi());
        return dto;
    }

    private EleveDefiResponse toEleveDefiResponse(EleveDefi part) {
        EleveDefiResponse dto = new EleveDefiResponse();
        dto.setId(part.getId());
        dto.setEleveId(part.getEleve().getId());
        dto.setNom(part.getEleve().getNom());
        dto.setPrenom(part.getEleve().getPrenom());
        dto.setDefiId(part.getDefi().getId());
        dto.setDefiTitre(part.getDefi().getTitre());
        dto.setDateEnvoie(part.getDateEnvoie());
        dto.setStatut(part.getStatut() != null ? part.getStatut() : "");
        return dto;
    }
}
