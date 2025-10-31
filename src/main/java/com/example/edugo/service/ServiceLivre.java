package com.example.edugo.service;

import com.example.edugo.dto.*;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
// lombok constructor removed to avoid IDE/annotation-processing issues; explicit constructor provided
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceLivre {

    private final LivreRepository livreRepository;
    private final ProgressionRepository progressionRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;

    // Explicit constructor to ensure IDEs that don't run Lombok annotation processing still see a valid constructor
    public ServiceLivre(LivreRepository livreRepository,
                        ProgressionRepository progressionRepository,
                        EleveRepository eleveRepository,
                        NiveauRepository niveauRepository,
                        ClasseRepository classeRepository,
                        MatiereRepository matiereRepository) {
        this.livreRepository = livreRepository;
        this.progressionRepository = progressionRepository;
        this.eleveRepository = eleveRepository;
        this.niveauRepository = niveauRepository;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
    }

    // ============ CRUD LIVRES ============
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public LivreResponse createLivre(LivreRequest req) {
        Livre livre = new Livre();
        livre.setTitre(req.getTitre());
        livre.setIsbn(req.getIsbn());
        livre.setDescription(req.getDescription());
        livre.setAnneePublication(req.getAnneePublication());
        livre.setEditeur(req.getEditeur());
        livre.setAuteur(req.getAuteur());
        livre.setTotalPages(req.getTotalPages());
        livre.setImageCouverture(req.getImageCouverture());
        livre.setLectureAuto(req.getLectureAuto());
        livre.setInteractif(req.getInteractif());
        if (req.getNiveauId() != null)
            livre.setNiveau(niveauRepository.findById(req.getNiveauId()).orElse(null));
        if (req.getClasseId() != null)
            livre.setClasse(classeRepository.findById(req.getClasseId()).orElse(null));
        if (req.getMatiereId() != null)
            livre.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
        return toResponse(livreRepository.save(livre));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public LivreResponse updateLivre(Long id, LivreRequest req) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        livre.setTitre(req.getTitre());
        livre.setIsbn(req.getIsbn());
        livre.setDescription(req.getDescription());
        livre.setAnneePublication(req.getAnneePublication());
        livre.setEditeur(req.getEditeur());
        livre.setAuteur(req.getAuteur());
        livre.setTotalPages(req.getTotalPages());
        livre.setImageCouverture(req.getImageCouverture());
        livre.setLectureAuto(req.getLectureAuto());
        livre.setInteractif(req.getInteractif());
        if (req.getNiveauId() != null)
            livre.setNiveau(niveauRepository.findById(req.getNiveauId()).orElse(null));
        if (req.getClasseId() != null)
            livre.setClasse(classeRepository.findById(req.getClasseId()).orElse(null));
        if (req.getMatiereId() != null)
            livre.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
        return toResponse(livreRepository.save(livre));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        livreRepository.delete(livre);
    }

    public List<LivreResponse> getAllLivres() {
        return livreRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LivreDetailResponse getLivreById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        return toDetailResponse(livre, null, null); // Progression, stats si besoin
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<LivreResponse> getLivresDisponibles(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        if (eleve.getClasse() != null)
            return livreRepository.findByClasseIdOrNiveauId(
                eleve.getClasse().getId(),
                eleve.getClasse().getNiveau() != null ? eleve.getClasse().getNiveau().getId() : null)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return getAllLivres();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<LivreResponse> getLivresByMatiere(Long matiereId) {
        return livreRepository.findByMatiereId(matiereId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ELEVE')")
    public List<LivreResponse> getLivresByNiveau(Long niveauId) {
        return livreRepository.findByNiveauId(niveauId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ELEVE')")
    public List<LivreResponse> getLivresByClasse(Long classeId) {
        return livreRepository.findByClasseId(classeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    // ==================== Helpers mapping DTO ====================
    private LivreResponse toResponse(Livre l) {
        LivreResponse dto = new LivreResponse();
        dto.setId(l.getId());
        dto.setTitre(l.getTitre());
        dto.setIsbn(l.getIsbn());
        dto.setAuteur(l.getAuteur());
        dto.setImageCouverture(l.getImageCouverture());
        dto.setTotalPages(l.getTotalPages());
        return dto;
    }
    private LivreDetailResponse toDetailResponse(Livre l, Double progression, Object stats) {
        LivreDetailResponse dto = new LivreDetailResponse();
        dto.setId(l.getId());
        dto.setTitre(l.getTitre());
        dto.setIsbn(l.getIsbn());
        dto.setAuteur(l.getAuteur());
        dto.setImageCouverture(l.getImageCouverture());
        dto.setTotalPages(l.getTotalPages());
        dto.setLectureAuto(l.getLectureAuto());
        dto.setInteractif(l.getInteractif());
        dto.setAnneePublication(l.getAnneePublication());
        dto.setEditeur(l.getEditeur());
        dto.setNiveauId(l.getNiveau() != null ? l.getNiveau().getId() : null);
        dto.setNiveauNom(l.getNiveau() != null ? l.getNiveau().getNom() : null);
        dto.setClasseId(l.getClasse() != null ? l.getClasse().getId() : null);
        dto.setClasseNom(l.getClasse() != null ? l.getClasse().getNom() : null);
        dto.setMatiereId(l.getMatiere() != null ? l.getMatiere().getId() : null);
        dto.setMatiereNom(l.getMatiere() != null ? l.getMatiere().getNom() : null);
        dto.setLangueId(null);
        dto.setLangueNom(null);
        dto.setProgression(progression);
        dto.setStatistiques(stats);
        return dto;
    }

    // ========= Compat controllers existants (progression/stats/recherches) =========
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public com.example.edugo.dto.ProgressionResponse updateProgressionLecture(Long eleveId, Long livreId, Integer pageActuelle) {
    Eleve eleve = eleveRepository.findById(eleveId)
        .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
    Livre livre = livreRepository.findById(livreId)
        .orElseThrow(() -> new ResourceNotFoundException("Livre", livreId));
    Integer pourcentageCompletion = (pageActuelle != null && livre.getTotalPages() != null && livre.getTotalPages() > 0) ? (pageActuelle * 100) / livre.getTotalPages() : 0;
    Progression progression = progressionRepository
        .findByEleveIdAndLivreId(eleveId, livreId)
        .orElse(new Progression());
    progression.setEleve(eleve);
    progression.setLivre(livre);
    progression.setPageActuelle(pageActuelle);
    progression.setPourcentageCompletion(pourcentageCompletion);
    progression.setDateMiseAJour(java.time.LocalDateTime.now());
    Progression saved = progressionRepository.save(progression);
    return toProgressionResponse(saved);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public java.util.List<com.example.edugo.dto.ProgressionResponse> getProgressionLecture(Long eleveId) {
    return progressionRepository.findByEleveId(eleveId).stream().map(this::toProgressionResponse).collect(java.util.stream.Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public com.example.edugo.dto.ProgressionResponse getProgressionLivre(Long eleveId, Long livreId) {
    return progressionRepository.findByEleveIdAndLivreId(eleveId, livreId)
        .map(this::toProgressionResponse)
        .orElse(null);
    }

    public Object getStatistiquesLivre(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", livreId));
        List<Progression> progressions = progressionRepository.findByLivreId(livreId);
        return new Object() {
            public final Long livreId = livre.getId();
            public final String titre = livre.getTitre();
            public final String auteur = livre.getAuteur();
            public final Integer totalPages = livre.getTotalPages();
            public final Integer nombreLecteurs = progressions.size();
            public final Integer nombreLecteursComplets = (int) progressions.stream()
                    .filter(p -> p.getPourcentageCompletion() >= 100)
                    .count();
            public final Double progressionMoyenne = progressions.isEmpty() ? 0.0 :
                    progressions.stream()
                            .mapToInt(Progression::getPourcentageCompletion)
                            .average()
                            .orElse(0.0);
        };
    }

    public List<Object> getLivresPopulaires() {
        List<Progression> progressions = progressionRepository.findAll();
        return progressions.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Progression::getLivre,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(entry -> new Object() {
                    public final Long livreId = entry.getKey().getId();
                    public final String titre = entry.getKey().getTitre();
                    public final String auteur = entry.getKey().getAuteur();
                    public final Long nombreLecteurs = entry.getValue();
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @PreAuthorize("hasRole('ELEVE')")
    public java.util.List<LivreResponse> getLivresRecommandes(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        if (eleve.getClasse() != null) {
            return livreRepository.findByClasseId(eleve.getClasse().getId()).stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
        }
        return livreRepository.findAll().stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }
    public java.util.List<LivreResponse> searchLivresByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre).stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<LivreResponse> searchLivresByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur).stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<LivreResponse> getLivresRecents() {
        return livreRepository.findTop10ByOrderByIdDesc().stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    // Helper mapping for progression -> DTO
    private com.example.edugo.dto.ProgressionResponse toProgressionResponse(Progression p) {
        com.example.edugo.dto.ProgressionResponse dto = new com.example.edugo.dto.ProgressionResponse();
        dto.setId(p.getId());
        dto.setEleveId(p.getEleve() != null ? p.getEleve().getId() : null);
        dto.setEleveNom(p.getEleve() != null ? (p.getEleve().getNom() + " " + (p.getEleve().getPrenom() != null ? p.getEleve().getPrenom() : "")) : null);
        dto.setLivreId(p.getLivre() != null ? p.getLivre().getId() : null);
        dto.setLivreTitre(p.getLivre() != null ? p.getLivre().getTitre() : null);
        dto.setPageActuelle(p.getPageActuelle());
        dto.setPourcentageCompletion(p.getPourcentageCompletion());
        dto.setDateMiseAJour(p.getDateDerniereLecture());
        return dto;
    }
}
