package com.example.edugo.service;

import com.example.edugo.dto.ObjectifRequest;
import com.example.edugo.dto.ObjectifResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.ObjectifRepository;
import com.example.edugo.repository.ProgressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceObjectif {

    private final ObjectifRepository objectifRepository;
    private final ProgressionRepository progressionRepository;
    private final EleveRepository eleveRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private ObjectifResponse toResponse(Objectif objectif) {
        if (objectif == null) return null;

        // Calculer les valeurs dynamiques sans sauvegarder
        long joursRestants = calculerJoursRestants(objectif.getDateFin());
        int livresLus = compterLivresLus(objectif.getEleve(), objectif.getDateEnvoie(), objectif.getDateFin());
        double progressionCalculee = calculerProgression(objectif, livresLus);
        String statut = determinerStatut(objectif.getDateFin(), progressionCalculee);

        return new ObjectifResponse(
                objectif.getIdObjectif(),
                objectif.getTypeObjectif(),
                objectif.getNbreLivre(),
                objectif.getDateEnvoie(),
                objectif.getDateFin(),
                progressionCalculee, // Utiliser la progression calculée
                joursRestants,
                livresLus,
                statut
        );
    }

    private Objectif toEntity(ObjectifRequest dto, Eleve eleve) {
        if (dto == null) return null;

        LocalDate dateFin = calculerDateFin(dto.getTypeObjectif(), dto.getDateEnvoie());

        Objectif objectif = new Objectif();
        objectif.setTypeObjectif(dto.getTypeObjectif());
        objectif.setNbreLivre(dto.getNbreLivre());
        objectif.setDateEnvoie(dto.getDateEnvoie());
        objectif.setDateFin(dateFin);
        objectif.setProgression(0.0);
        objectif.setStatut("EN_COURS");
        objectif.setEleve(eleve);

        return objectif;
    }

    // ====== CRUD DTO ======

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public ObjectifResponse createObjectifDto(ObjectifRequest dto, Long eleveId) {
        // Vérifier que l'élève existe
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        // Vérifier s'il y a déjà un objectif en cours
        if (hasObjectifEnCours(eleveId)) {
            Objectif objectifEnCours = getObjectifEnCours(eleveId)
                    .orElseThrow(() -> new RuntimeException("Erreur de vérification d'objectif en cours"));

            long joursRestants = calculerJoursRestants(objectifEnCours.getDateFin());
            throw new com.example.edugo.exception.ObjectifEnCoursException(
                    objectifEnCours.getTypeObjectif().toString(), joursRestants
            );
        }

        Objectif objectif = toEntity(dto, eleve);
        Objectif saved = objectifRepository.save(objectif);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public ObjectifResponse getObjectifEnCoursDto(Long eleveId) {
        Optional<Objectif> objectifEnCours = getObjectifEnCours(eleveId);

        if (objectifEnCours.isPresent()) {
            Objectif objectif = objectifEnCours.get();
            // Mettre à jour la progression en base avant de retourner
            mettreAJourProgression(objectif);
            return toResponse(objectif);
        } else {
            // Retourner null si aucun objectif en cours (cas normal, pas une erreur)
            return null;
        }
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public List<ObjectifResponse> getObjectifsByEleveDto(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        List<Objectif> objectifs = objectifRepository.findByEleveOrderByDateEnvoieDesc(eleve);

        // Mettre à jour la progression pour chaque objectif
        objectifs.forEach(this::mettreAJourProgression);

        return objectifs.stream().map(this::toResponse).toList();
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public ObjectifResponse getObjectifByIdDto(Long id, Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        Objectif objectif = objectifRepository.findByIdObjectifAndEleve(id, eleve)
                .orElseThrow(() -> new ResourceNotFoundException("Objectif", id));

        // Mettre à jour la progression avant de retourner
        mettreAJourProgression(objectif);
        return toResponse(objectif);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<ObjectifResponse> getHistoriqueObjectifsDto(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        List<Objectif> objectifsTermines = objectifRepository.findByEleveAndStatutOrderByDateEnvoieDesc(eleve, "TERMINE");
        List<Objectif> objectifsEchec = objectifRepository.findByEleveAndStatutOrderByDateEnvoieDesc(eleve, "ECHEC");

        // Combiner les listes
        objectifsTermines.addAll(objectifsEchec);

        return objectifsTermines.stream().map(this::toResponse).toList();
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public void deleteObjectif(Long id, Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        Objectif objectif = objectifRepository.findByIdObjectifAndEleve(id, eleve)
                .orElseThrow(() -> new ResourceNotFoundException("Objectif", id));

        // Empêcher la suppression d'un objectif en cours
        if ("EN_COURS".equals(objectif.getStatut())) {
            throw new IllegalArgumentException("Impossible de supprimer un objectif en cours. Terminez-le d'abord.");
        }

        objectifRepository.delete(objectif);
    }

    // ==================== MÉTHODES UTILITAIRES PRIVÉES ====================

    private Optional<Objectif> getObjectifEnCours(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        List<Objectif> objectifsEnCours = objectifRepository.findByEleveAndStatut(eleve, "EN_COURS");
        return objectifsEnCours.stream().findFirst();
    }

    private boolean hasObjectifEnCours(Long eleveId) {
        return getObjectifEnCours(eleveId).isPresent();
    }

    @Transactional
    protected void mettreAJourProgression(Objectif objectif) {
        int livresLus = compterLivresLus(objectif.getEleve(), objectif.getDateEnvoie(), objectif.getDateFin());
        double progression = calculerProgression(objectif, livresLus);
        String statut = determinerStatut(objectif.getDateFin(), progression);

        // Mettre à jour l'objet seulement si nécessaire
        if (objectif.getProgression() != progression || !objectif.getStatut().equals(statut)) {
            objectif.setProgression(progression);
            objectif.setStatut(statut);
            objectifRepository.save(objectif);
        }
    }

    private double calculerProgression(Objectif objectif, int livresLus) {
        return (objectif.getNbreLivre() > 0) ?
                (double) livresLus / objectif.getNbreLivre() * 100 : 0;
    }

    private int compterLivresLus(Eleve eleve, LocalDate dateDebut, LocalDate dateFin) {
        try {
            List<Progression> progressions = progressionRepository
                    .findByEleveAndPourcentageCompletionAndDateDerniereLectureBetween(
                            eleve,
                            100,
                            dateDebut.atStartOfDay(),
                            dateFin.atTime(23, 59, 59)
                    );

            return progressions.size();
        } catch (Exception e) {
            return 0;
        }
    }

    private LocalDate calculerDateFin(TypeObjectif type, LocalDate dateDebut) {
        return switch (type) {
            case HEBDOMADAIRE -> dateDebut.plusWeeks(1);
            case MENSUEL -> dateDebut.plusMonths(1);
        };
    }

    private long calculerJoursRestants(LocalDate dateFin) {
        long joursRestants = ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
        return Math.max(joursRestants, 0);
    }

    private String determinerStatut(LocalDate dateFin, double progression) {
        LocalDate aujourdhui = LocalDate.now();

        if (aujourdhui.isAfter(dateFin)) {
            return progression >= 100 ? "TERMINE" : "ECHEC";
        } else {
            return "EN_COURS";
        }
    }
}