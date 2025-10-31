package com.example.edugo.service;

import com.example.edugo.dto.ExerciceResponse;
import com.example.edugo.dto.LivreResponse;
import com.example.edugo.dto.MatiereDetailResponse;
import com.example.edugo.dto.MatiereRequest;
import com.example.edugo.dto.MatiereResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceMatiere {

    private final MatiereRepository matiereRepository;
    private final LivreRepository livreRepository;
    private final ExerciceRepository exerciceRepository;
    private final EleveRepository eleveRepository;

    // ==================== CRUD MATIÈRES ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public MatiereResponse createMatiere(MatiereRequest request) {
        if (matiereRepository.existsByNom(request.getNom())) {
            throw new RuntimeException("Cette matière existe déjà");
        }
        Matiere matiere = new Matiere();
        matiere.setNom(request.getNom());
        return toResponse(matiereRepository.save(matiere));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public MatiereResponse updateMatiere(Long id, MatiereRequest request) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        matiere.setNom(request.getNom());
        return toResponse(matiereRepository.save(matiere));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMatiere(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        List<Livre> livres = livreRepository.findByMatiereId(id);
        if (!livres.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une matière contenant des livres");
        }
        matiereRepository.delete(matiere);
    }

    public List<MatiereResponse> getAllMatieres() {
        return matiereRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MatiereDetailResponse getMatiereById(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        List<Exercice> exercices = exerciceRepository.findByMatiereId(id);
        List<Livre> livres = livreRepository.findByMatiereId(id);
        return toDetailResponse(matiere, livres.size(), exercices.size(), (int) exercices.stream().filter(e -> Boolean.TRUE.equals(e.getActive())).count());
    }

    // ==================== LIVRES PAR MATIÈRE ====================
    public List<LivreResponse> getLivresByMatiere(Long matiereId) {
        return livreRepository.findByMatiereId(matiereId).stream().map(this::toLivreResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<LivreResponse> getLivresDisponiblesByMatiere(Long eleveId, Long matiereId) {
        eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        return getLivresByMatiere(matiereId);
    }

    // ==================== EXERCICES PAR MATIÈRE ====================
    public List<ExerciceResponse> getExercicesByMatiere(Long matiereId) {
        return exerciceRepository.findByMatiereId(matiereId).stream().map(this::toExerciceResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<ExerciceResponse> getExercicesDisponiblesByMatiere(Long eleveId, Long matiereId) {
        return exerciceRepository.findByMatiereIdAndActiveTrue(matiereId).stream().map(this::toExerciceResponse).collect(Collectors.toList());
    }

    // ==================== STATISTIQUES MATIÈRES ====================
    public Object getStatistiquesMatiere(Long matiereId) {
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", matiereId));
        List<Livre> livres = livreRepository.findByMatiereId(matiereId);
        List<Exercice> exercices = exerciceRepository.findByMatiereId(matiereId);
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("matiereId", matiere.getId());
        stats.put("nomMatiere", matiere.getNom());
        stats.put("nombreLivres", livres.size());
        stats.put("nombreExercices", exercices.size());
        stats.put("nombreExercicesActifs", (int) exercices.stream().filter(e -> Boolean.TRUE.equals(e.getActive())).count());
        return stats;
    }

    // ==================== PROGRESSION PAR MATIÈRE ====================
    @PreAuthorize("hasRole('ELEVE')")
    public Object getProgressionMatiere(Long eleveId, Long matiereId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", matiereId));
        List<Livre> livresMatiere = livreRepository.findByMatiereId(matiereId);
        List<Exercice> exercicesMatiere = exerciceRepository.findByMatiereId(matiereId);
        double progressionMoyenne = 0.0;
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("eleveId", eleve.getId());
        result.put("nomEleve", eleve.getNom() + " " + eleve.getPrenom());
        result.put("matiereId", matiere.getId());
        result.put("nomMatiere", matiere.getNom());
        result.put("nombreLivresDisponibles", livresMatiere.size());
        result.put("nombreExercicesDisponibles", exercicesMatiere.size());
        result.put("progressionMoyenne", progressionMoyenne);
        return result;
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    public List<MatiereResponse> searchMatieresByName(String nom) {
        return matiereRepository.findByNomContainingIgnoreCase(nom).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ==================== MATIÈRES POPULAIRES ====================
    public List<Object> getMatieresPopulaires() {
        List<Livre> livres = livreRepository.findAll();
        return livres.stream()
                .filter(livre -> livre.getMatiere() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        Livre::getMatiere,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(entry -> {
                    java.util.Map<String, Object> m = new java.util.HashMap<>();
                    m.put("matiereId", entry.getKey().getId());
                    m.put("nomMatiere", entry.getKey().getNom());
                    m.put("nombreLivres", entry.getValue());
                    return m;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== VALIDATION MATIÈRES ====================
    @PreAuthorize("hasRole('ADMIN')")
    public boolean validerMatiere(Long matiereId) {
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", matiereId));
        List<Livre> livres = livreRepository.findByMatiereId(matiereId);
        List<Exercice> exercices = exerciceRepository.findByMatiereId(matiereId);
        return !livres.isEmpty() || !exercices.isEmpty();
    }

    // ==================== FUSION DE MATIÈRES ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void fusionnerMatieres(Long matiereIdSource, Long matiereIdDestination) {
        Matiere matiereDestination = matiereRepository.findById(matiereIdDestination)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", matiereIdDestination));
        Matiere matiereSource = matiereRepository.findById(matiereIdSource)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", matiereIdSource));
        List<Livre> livresSource = livreRepository.findByMatiereId(matiereIdSource);
        for (Livre livre : livresSource) {
            livre.setMatiere(matiereDestination);
            livreRepository.save(livre);
        }
        List<Exercice> exercicesSource = exerciceRepository.findByMatiereId(matiereIdSource);
        for (Exercice exercice : exercicesSource) {
            exercice.setMatiere(matiereDestination);
            exerciceRepository.save(exercice);
        }
        matiereRepository.delete(matiereSource);
    }

    // ==================== Mapping Helpers ====================
    private MatiereResponse toResponse(Matiere m) {
        MatiereResponse dto = new MatiereResponse();
        dto.setId(m.getId());
        dto.setNom(m.getNom());
        return dto;
    }

    private MatiereDetailResponse toDetailResponse(Matiere m, int nombreLivres, int nombreExercices, int nombreExercicesActifs) {
        MatiereDetailResponse dto = new MatiereDetailResponse();
        dto.setId(m.getId());
        dto.setNom(m.getNom());
        dto.setNombreLivres(nombreLivres);
        dto.setNombreExercices(nombreExercices);
        dto.setNombreExercicesActifs(nombreExercicesActifs);
        dto.setStatistiques(getStatistiquesMatiere(m.getId()));
        return dto;
    }

    private LivreResponse toLivreResponse(Livre l) {
        LivreResponse dto = new LivreResponse();
        dto.setId(l.getId());
        dto.setTitre(l.getTitre());
        dto.setIsbn(l.getIsbn());
        dto.setAuteur(l.getAuteur());
        dto.setImageCouverture(l.getImageCouverture());
        dto.setTotalPages(l.getTotalPages());
        return dto;
    }

    private ExerciceResponse toExerciceResponse(Exercice e) {
        ExerciceResponse dto = new ExerciceResponse();
        dto.setId(e.getId());
        dto.setTitre(e.getTitre());
        dto.setActive(e.getActive());
        dto.setNiveauDifficulte(e.getNiveauDifficulte());
        dto.setTempsAlloue(e.getTempsAlloue());
        if (e.getMatiere() != null) {
            dto.setMatiereId(e.getMatiere().getId());
            dto.setMatiereNom(e.getMatiere().getNom());
        }
        return dto;
    }
}

