package com.example.edugo.service;

import com.example.edugo.dto.ExerciceRequest;
import com.example.edugo.dto.ExerciceResponse;
import com.example.edugo.dto.ExerciceDetailResponse;
import com.example.edugo.dto.FaireExerciceResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceExercice {

    private final ExerciceRepository exerciceRepository;
    private final FaireExerciceRepository faireExerciceRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final NiveauRepository niveauRepository;
    private final MatiereRepository matiereRepository;

    // ==================== CRUD EXERCICES ====================
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ExerciceResponse createExercice(ExerciceRequest req) {
        Exercice ex = new Exercice();
        ex.setTitre(req.getTitre());
        ex.setDescription(req.getDescription());
        ex.setNiveauDifficulte(req.getNiveauDifficulte());
        ex.setTempsAlloue(req.getTempsAlloue());
        ex.setActive(req.getActive());
        // Relations
        if (req.getMatiereId() != null)
            ex.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
        if (req.getNiveauId() != null)
            ex.setNiveauScolaire(niveauRepository.findById(req.getNiveauId()).orElse(null));
        if (req.getLivreId() != null)
            ex.setLivre(livreRepository.findById(req.getLivreId()).orElse(null));
        return toResponse(exerciceRepository.save(ex));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ExerciceResponse updateExercice(Long id, ExerciceRequest req) {
        Exercice ex = exerciceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        ex.setTitre(req.getTitre());
        ex.setDescription(req.getDescription());
        ex.setNiveauDifficulte(req.getNiveauDifficulte());
        ex.setTempsAlloue(req.getTempsAlloue());
        ex.setActive(req.getActive());
        if (req.getMatiereId() != null)
            ex.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
        if (req.getNiveauId() != null)
            ex.setNiveauScolaire(niveauRepository.findById(req.getNiveauId()).orElse(null));
        if (req.getLivreId() != null)
            ex.setLivre(livreRepository.findById(req.getLivreId()).orElse(null));
        return toResponse(exerciceRepository.save(ex));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteExercice(Long id) {
        Exercice ex = exerciceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        exerciceRepository.delete(ex);
    }

    public List<ExerciceResponse> getAllExercices() {
        return exerciceRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ExerciceDetailResponse getExerciceById(Long id) {
        Exercice e = exerciceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        return toDetailResponse(e);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<ExerciceResponse> getExercicesDisponibles(Long eleveId) {
        return exerciceRepository.findByActiveTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<ExerciceResponse> getExercicesByMatiere(Long matiereId) {
        return exerciceRepository.findByMatiereIdAndActiveTrue(matiereId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<ExerciceResponse> getExercicesByDifficulte(Integer niveauDifficulte) {
        return exerciceRepository.findByNiveauDifficulteAndActiveTrue(niveauDifficulte).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ========== Compat: endpoints utilisés par controllers existants ==========
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

    public List<ExerciceResponse> searchExercicesByTitre(String titre) {
        return exerciceRepository.findByTitreContainingIgnoreCaseAndActiveTrue(titre).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ExerciceResponse> getExercicesByLivre(Long livreId) {
        return exerciceRepository.findByLivreIdAndActiveTrue(livreId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // SOUMISSION/DÉTAIL/ÉVALUATION
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public FaireExerciceResponse soumettreExercice(Long eleveId, Long exerciceId, String reponse) {
        Eleve eleve = eleveRepository.findById(eleveId).orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        Exercice exercice = exerciceRepository.findById(exerciceId).orElseThrow(() -> new ResourceNotFoundException("Exercice", exerciceId));
        FaireExercice fe = new FaireExercice();
        fe.setEleve(eleve);
        fe.setExercice(exercice);
        fe.setReponse(reponse);
        fe.setDateSoumission(LocalDateTime.now());
        fe.setStatut(StatutExercice.SOUMIS);
        return toFaireExerciceResponse(faireExerciceRepository.save(fe));
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<FaireExerciceResponse> getHistoriqueExercices(Long eleveId) {
        return faireExerciceRepository.findByEleveId(eleveId).stream().map(this::toFaireExerciceResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public FaireExerciceResponse getExerciceRealise(Long eleveId, Long exerciceId) {
        FaireExercice fe = faireExerciceRepository.findByEleveIdAndExerciceId(eleveId, exerciceId).orElse(null);
        return (fe == null ? null : toFaireExerciceResponse(fe));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FaireExerciceResponse corrigerExercice(Long faireExerciceId, Integer note, String commentaire) {
        FaireExercice fe = faireExerciceRepository.findById(faireExerciceId).orElseThrow(() -> new ResourceNotFoundException("Exercice réalisé", faireExerciceId));
        fe.setNote(note);
        fe.setCommentaire(commentaire);
        fe.setStatut(StatutExercice.CORRIGE);
        fe.setDateCorrection(LocalDateTime.now());
        return toFaireExerciceResponse(faireExerciceRepository.save(fe));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<FaireExerciceResponse> getExercicesACorriger() {
        return faireExerciceRepository.findByStatut(StatutExercice.SOUMIS).stream().map(this::toFaireExerciceResponse).collect(Collectors.toList());
    }

    // ========== Helpers ============
    private ExerciceResponse toResponse(Exercice ex) {
        ExerciceResponse dto = new ExerciceResponse();
        dto.setId(ex.getId());
        dto.setTitre(ex.getTitre());
        dto.setActive(ex.getActive());
        dto.setNiveauDifficulte(ex.getNiveauDifficulte());
        dto.setTempsAlloue(ex.getTempsAlloue());
        if (ex.getMatiere() != null) {
            dto.setMatiereId(ex.getMatiere().getId());
            dto.setMatiereNom(ex.getMatiere().getNom());
        }
        return dto;
    }
    private ExerciceDetailResponse toDetailResponse(Exercice ex) {
        ExerciceDetailResponse dto = new ExerciceDetailResponse();
        dto.setId(ex.getId());
        dto.setTitre(ex.getTitre());
        dto.setDescription(ex.getDescription());
        dto.setNiveauDifficulte(ex.getNiveauDifficulte());
        dto.setTempsAlloue(ex.getTempsAlloue());
        dto.setActive(ex.getActive());
        dto.setMatiereId(ex.getMatiere() != null ? ex.getMatiere().getId() : null);
        dto.setMatiereNom(ex.getMatiere() != null ? ex.getMatiere().getNom() : null);
        dto.setNiveauId(ex.getNiveauScolaire() != null ? ex.getNiveauScolaire().getId() : null);
        dto.setNiveauNom(ex.getNiveauScolaire() != null ? ex.getNiveauScolaire().getNom() : null);
        dto.setLivreId(ex.getLivre() != null ? ex.getLivre().getId() : null);
        dto.setLivreTitre(ex.getLivre() != null ? ex.getLivre().getTitre() : null);
        // Map les questions si besoin
        return dto;
    }
    private FaireExerciceResponse toFaireExerciceResponse(FaireExercice fe) {
        FaireExerciceResponse dto = new FaireExerciceResponse();
        dto.setId(fe.getId());
        dto.setEleveId(fe.getEleve() != null ? fe.getEleve().getId() : null);
        dto.setEleveNom(fe.getEleve() != null ? (fe.getEleve().getNom() + " " + fe.getEleve().getPrenom()) : null);
        dto.setExerciceId(fe.getExercice() != null ? fe.getExercice().getId() : null);
        dto.setExerciceTitre(fe.getExercice() != null ? fe.getExercice().getTitre() : null);
        dto.setReponse(fe.getReponse());
        dto.setNote(fe.getNote());
        dto.setCommentaire(fe.getCommentaire());
        dto.setStatut(fe.getStatut() != null ? fe.getStatut().name() : null);
        dto.setDateSoumission(fe.getDateSoumission());
        dto.setDateCorrection(fe.getDateCorrection());
        return dto;
    }
}
