package com.example.edugo.service;

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
public class ServiceMatiere {

    private final MatiereRepository matiereRepository;
    private final LivreRepository livreRepository;
    private final ExerciceRepository exerciceRepository;
    private final EleveRepository eleveRepository;

    // ==================== CRUD MATIÈRES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Matiere createMatiere(Matiere matiere) {
        if (matiereRepository.existsByNom(matiere.getNom())) {
            throw new RuntimeException("Cette matière existe déjà");
        }
        return matiereRepository.save(matiere);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Matiere updateMatiere(Long id, Matiere matiereDetails) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        
        matiere.setNom(matiereDetails.getNom());
        return matiereRepository.save(matiere);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMatiere(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        
        // Vérifier s'il y a des livres associés à cette matière
        List<Livre> livres = livreRepository.findByMatiereId(id);
        if (!livres.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une matière contenant des livres");
        }
        
        matiereRepository.delete(matiere);
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Matiere getMatiereById(Long id) {
        return matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
    }

    // ==================== LIVRES PAR MATIÈRE ====================
    
    public List<Livre> getLivresByMatiere(Long matiereId) {
        return livreRepository.findByMatiereId(matiereId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresDisponiblesByMatiere(Long eleveId, Long matiereId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        return livreRepository.findByMatiereId(matiereId);
    }

    // ==================== EXERCICES PAR MATIÈRE ====================
    
    public List<Exercice> getExercicesByMatiere(Long matiereId) {
        return exerciceRepository.findByMatiereId(matiereId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Exercice> getExercicesDisponiblesByMatiere(Long eleveId, Long matiereId) {
        return exerciceRepository.findByMatiereIdAndActiveTrue(matiereId);
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
    stats.put("nombreExercicesActifs", (int) exercices.stream()
        .filter(e -> Boolean.TRUE.equals(e.getActive()))
        .count());
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
        
        // Calculer la progression moyenne pour cette matière
        double progressionMoyenne = 0.0;
        if (!livresMatiere.isEmpty()) {
            // Logique de calcul de progression basée sur les livres lus
            // Cette logique devrait être implémentée selon vos besoins spécifiques
        }
        
        java.util.Map<String,Object> result = new java.util.HashMap<>();
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
    
    public List<Matiere> searchMatieresByName(String nom) {
        return matiereRepository.findByNomContainingIgnoreCase(nom);
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
        
        // Vérifier si la matière a au moins un livre ou un exercice
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
        
        // Migrer tous les livres de la matière source vers la destination
        List<Livre> livresSource = livreRepository.findByMatiereId(matiereIdSource);
        for (Livre livre : livresSource) {
            livre.setMatiere(matiereDestination);
            livreRepository.save(livre);
        }
        
        // Migrer tous les exercices de la matière source vers la destination
        List<Exercice> exercicesSource = exerciceRepository.findByMatiereId(matiereIdSource);
        for (Exercice exercice : exercicesSource) {
            exercice.setMatiere(matiereDestination);
            exerciceRepository.save(exercice);
        }
        
        // Supprimer la matière source
        matiereRepository.delete(matiereSource);
    }
}

