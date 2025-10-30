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
public class ServiceExercice {

    private final ExerciceRepository exerciceRepository;
    private final FaireExerciceRepository faireExerciceRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final MatiereRepository matiereRepository;

    // ==================== CRUD EXERCICES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Exercice createExercice(Exercice exercice) {
        return exerciceRepository.save(exercice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Exercice updateExercice(Long id, Exercice exerciceDetails) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        
        exercice.setTitre(exerciceDetails.getTitre());
        exercice.setDescription(exerciceDetails.getDescription());
        exercice.setNiveauDifficulte(exerciceDetails.getNiveauDifficulte());
        exercice.setTempsAlloue(exerciceDetails.getTempsAlloue());
        exercice.setActive(exerciceDetails.getActive());
        
        return exerciceRepository.save(exercice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteExercice(Long id) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        exerciceRepository.delete(exercice);
    }

    public List<Exercice> getAllExercices() {
        return exerciceRepository.findAll();
    }

    public Exercice getExerciceById(Long id) {
        return exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
    }

    // ==================== EXERCICES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Exercice> getExercicesDisponibles(Long eleveId) {
        return exerciceRepository.findByActiveTrue();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Exercice> getExercicesByMatiere(Long matiereId) {
        return exerciceRepository.findByMatiereIdAndActiveTrue(matiereId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Exercice> getExercicesByDifficulte(Integer niveauDifficulte) {
        return exerciceRepository.findByNiveauDifficulteAndActiveTrue(niveauDifficulte);
    }

    // ==================== SOUMISSION D'EXERCICES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public FaireExercice soumettreExercice(Long eleveId, Long exerciceId, String reponse) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", exerciceId));
        
        FaireExercice faireExercice = new FaireExercice();
        faireExercice.setEleve(eleve);
        faireExercice.setExercice(exercice);
        faireExercice.setReponse(reponse);
        faireExercice.setDateSoumission(LocalDateTime.now());
        faireExercice.setStatut(StatutExercice.SOUMIS);
        
        return faireExerciceRepository.save(faireExercice);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<FaireExercice> getHistoriqueExercices(Long eleveId) {
        return faireExerciceRepository.findByEleveId(eleveId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public FaireExercice getExerciceRealise(Long eleveId, Long exerciceId) {
        return faireExerciceRepository.findByEleveIdAndExerciceId(eleveId, exerciceId)
                .orElse(null);
    }

    // ==================== CORRECTION ET ÉVALUATION ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FaireExercice corrigerExercice(Long faireExerciceId, Integer note, String commentaire) {
        FaireExercice faireExercice = faireExerciceRepository.findById(faireExerciceId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice réalisé", faireExerciceId));
        
        faireExercice.setNote(note);
        faireExercice.setCommentaire(commentaire);
        faireExercice.setStatut(StatutExercice.CORRIGE);
        faireExercice.setDateCorrection(LocalDateTime.now());
        
        return faireExerciceRepository.save(faireExercice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<FaireExercice> getExercicesACorriger() {
        return faireExerciceRepository.findByStatut(StatutExercice.SOUMIS);
    }

    // ==================== STATISTIQUES EXERCICES ====================
    
    public Object getStatistiquesExercice(Long exerciceId) {
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", exerciceId));
        
        List<FaireExercice> realisations = faireExerciceRepository.findByExerciceId(exerciceId);
        
        return new Object() {
            public final Long exerciceId = exercice.getId();
            public final String titre = exercice.getTitre();
            public final Integer nombreTentatives = realisations.size();
            public final Integer nombreReussites = (int) realisations.stream()
                    .filter(r -> r.getNote() != null && r.getNote() >= 10)
                    .count();
            public final Double noteMoyenne = realisations.stream()
                    .filter(r -> r.getNote() != null)
                    .mapToInt(FaireExercice::getNote)
                    .average()
                    .orElse(0.0);
        };
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Exercice> searchExercicesByTitre(String titre) {
        return exerciceRepository.findByTitreContainingIgnoreCaseAndActiveTrue(titre);
    }

    public List<Exercice> getExercicesByLivre(Long livreId) {
        return exerciceRepository.findByLivreIdAndActiveTrue(livreId);
    }
}
