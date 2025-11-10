package com.example.edugo.service;

import com.example.edugo.dto.NiveauRequest;
import com.example.edugo.dto.NiveauResponse;
import com.example.edugo.dto.StatistiquesNiveauResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceNiveau {

    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private NiveauResponse toResponse(Niveau niveau) {
        if (niveau == null) return null;
        return new NiveauResponse(niveau.getId(), niveau.getNom());
    }

    private Niveau toEntity(NiveauRequest dto) {
        if (dto == null) return null;
        Niveau niveau = new Niveau();
        niveau.setNom(dto.getNom());
        return niveau;
    }

    // ====== CRUD DTO ======
    public List<NiveauResponse> getAllNiveauxDto() {
        return niveauRepository.findAll().stream().map(this::toResponse).toList();
    }

    public NiveauResponse getNiveauByIdDto(Long id) {
        Niveau niveau = getNiveauById(id);
        return toResponse(niveau);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public NiveauResponse createNiveauDto(NiveauRequest dto) {
        if (niveauRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Ce niveau existe déjà");
        }
        Niveau niveau = toEntity(dto);
        Niveau saved = niveauRepository.save(niveau);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public NiveauResponse updateNiveauDto(Long id, NiveauRequest dto) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        niveau.setNom(dto.getNom());
        Niveau saved = niveauRepository.save(niveau);
        return toResponse(saved);
    }

    // ==================== CRUD NIVEAUX ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Niveau createNiveau(Niveau niveau) {
        if (niveauRepository.existsByNom(niveau.getNom())) {
            throw new RuntimeException("Ce niveau existe déjà");
        }
        return niveauRepository.save(niveau);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Niveau updateNiveau(Long id, Niveau niveauDetails) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));

        niveau.setNom(niveauDetails.getNom());
        return niveauRepository.save(niveau);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteNiveau(Long id) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));

        List<Classe> classes = classeRepository.findByNiveauId(id);
        if (!classes.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un niveau contenant des classes");
        }

        niveauRepository.delete(niveau);
    }

    public List<Niveau> getAllNiveaux() {
        return niveauRepository.findAll();
    }

    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
    }

    // ==================== GESTION DES CLASSES PAR NIVEAU ====================
    public List<Classe> getClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Classe assignerClasseANiveau(Long classeId, Long niveauId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", classeId));

        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));

        classe.setNiveau(niveau);
        return classeRepository.save(classe);
    }

    // ==================== STATISTIQUES NIVEAUX ====================
    public StatistiquesNiveauResponse getStatistiquesNiveau(Long niveauId) {
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));
        List<Classe> classes = classeRepository.findByNiveauId(niveauId);
        List<Eleve> eleves = eleveRepository.findByClasseNiveauId(niveauId);
        List<Livre> livres = livreRepository.findByNiveauId(niveauId);
        int pointsMoyens = eleves.isEmpty() ? 0 : eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
        return new StatistiquesNiveauResponse(
                niveau.getId(),
                niveau.getNom(),
                classes.size(),
                eleves.size(),
                livres.size(),
                pointsMoyens
        );
    }

    // ==================== HIÉRARCHIE DES NIVEAUX ====================
    public List<Niveau> getNiveauxParOrdre() {
        return niveauRepository.findAllByOrderByNomAsc();
    }

    public Niveau getNiveauSuivant(Long niveauId) {
        List<Niveau> niveaux = niveauRepository.findAllByOrderByNomAsc();
        int currentIndex = -1;

        for (int i = 0; i < niveaux.size(); i++) {
            if (niveaux.get(i).getId().equals(niveauId)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex >= 0 && currentIndex < niveaux.size() - 1) {
            return niveaux.get(currentIndex + 1);
        }

        return null;
    }

    public Niveau getNiveauPrecedent(Long niveauId) {
        List<Niveau> niveaux = niveauRepository.findAllByOrderByNomAsc();
        int currentIndex = -1;

        for (int i = 0; i < niveaux.size(); i++) {
            if (niveaux.get(i).getId().equals(niveauId)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex > 0) {
            return niveaux.get(currentIndex - 1);
        }

        return null;
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    public List<Niveau> searchNiveauxByName(String nom) {
        return niveauRepository.findByNomContainingIgnoreCase(nom);
    }

    // ==================== VALIDATION NIVEAUX ====================
    @PreAuthorize("hasRole('ADMIN')")
    public boolean validerNiveau(Long niveauId) {
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));

        List<Classe> classes = classeRepository.findByNiveauId(niveauId);
        return !classes.isEmpty();
    }

    // ==================== MIGRATION DES ÉLÈVES ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void migrerElevesVersNiveau(Long niveauIdSource, Long niveauIdDestination) {
        Niveau niveauDestination = niveauRepository.findById(niveauIdDestination)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauIdDestination));

        List<Classe> classesSource = classeRepository.findByNiveauId(niveauIdSource);

        for (Classe classe : classesSource) {
            classe.setNiveau(niveauDestination);
            classeRepository.save(classe);
        }
    }
}
