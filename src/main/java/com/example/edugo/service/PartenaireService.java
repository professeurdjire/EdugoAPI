package com.example.edugo.service;

import com.example.edugo.dto.PartenaireRequest;
import com.example.edugo.dto.PartenaireResponse;
import com.example.edugo.entity.Principales.Partenaire;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartenaireService {

    private final PartenaireRepository partenaireRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private PartenaireResponse toResponse(Partenaire partenaire) {
        if (partenaire == null) return null;
        return new PartenaireResponse(
                partenaire.getId(),
                partenaire.getNom(),
                partenaire.getDescription(),
                partenaire.getLogoUrl(),
                partenaire.getSiteWeb(),
                partenaire.getDomaine(),
                partenaire.getType(),
                partenaire.getEmail(),
                partenaire.getTelephone(),
                partenaire.getPays(),
                deriveStatut(partenaire.getActif()),
                partenaire.getDateCreation() != null ? partenaire.getDateCreation().toString() : null,
                partenaire.getNewsletter(),
                partenaire.getDateCreation(),
                partenaire.getDateModification(),
                partenaire.getActif()
        );
    }

    private Partenaire toEntity(PartenaireRequest dto) {
        if (dto == null) return null;
        Partenaire partenaire = new Partenaire();
        partenaire.setNom(dto.getNom());
        partenaire.setDescription(dto.getDescription());
        partenaire.setLogoUrl(dto.getLogoUrl());
        partenaire.setSiteWeb(dto.getSiteWeb());
        partenaire.setActif(dto.getActif());
        partenaire.setDomaine(dto.getDomaine());
        partenaire.setType(dto.getType());
        partenaire.setEmail(dto.getEmail());
        partenaire.setTelephone(dto.getTelephone());
        partenaire.setPays(dto.getPays());
        partenaire.setNewsletter(dto.getNewsletter());
        return partenaire;
    }

    // ====== CRUD DTO ======
    public List<PartenaireResponse> getAllPartenairesDto() {
        return partenaireRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PartenaireResponse getPartenaireByIdDto(Long id) {
        Partenaire partenaire = getPartenaireById(id);
        return toResponse(partenaire);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PartenaireResponse createPartenaireDto(PartenaireRequest dto) {
        if (partenaireRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Ce partenaire existe déjà");
        }
        Partenaire partenaire = toEntity(dto);
        Partenaire saved = partenaireRepository.save(partenaire);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PartenaireResponse updatePartenaireDto(Long id, PartenaireRequest dto) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
        partenaire.setNom(dto.getNom());
        partenaire.setDescription(dto.getDescription());
        partenaire.setLogoUrl(dto.getLogoUrl());
        partenaire.setSiteWeb(dto.getSiteWeb());
        partenaire.setActif(dto.getActif());
        Partenaire saved = partenaireRepository.save(partenaire);
        return toResponse(saved);
    }

    // ==================== CRUD ENTITES ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Partenaire createPartenaire(Partenaire partenaire) {
        if (partenaireRepository.existsByNom(partenaire.getNom())) {
            throw new RuntimeException("Ce partenaire existe déjà");
        }
        return partenaireRepository.save(partenaire);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Partenaire updatePartenaire(Long id, Partenaire partenaireDetails) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));

        partenaire.setNom(partenaireDetails.getNom());
        partenaire.setDescription(partenaireDetails.getDescription());
        partenaire.setLogoUrl(partenaireDetails.getLogoUrl());
        partenaire.setSiteWeb(partenaireDetails.getSiteWeb());
        partenaire.setActif(partenaireDetails.getActif());
        return partenaireRepository.save(partenaire);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deletePartenaire(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
        partenaireRepository.delete(partenaire);
    }

    public List<Partenaire> getAllPartenaires() {
        return partenaireRepository.findAll();
    }

    public Partenaire getPartenaireById(Long id) {
        return partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    public List<Partenaire> searchPartenairesByName(String nom) {
        return partenaireRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Partenaire> getPartenairesActifs() {
        return partenaireRepository.findByActifTrue();
    }

    // ====== Helpers ======
    private String deriveStatut(Boolean actif) {
        if (actif == null) return "en_attente";
        return actif ? "actif" : "inactif";
    }
}