package com.example.edugo.service;

import com.example.edugo.dto.EleveSimpleResponse;
import com.example.edugo.dto.SuggestionRequest;
import com.example.edugo.dto.SuggestionResponse;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Suggestion;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.SuggestionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceSuggestion {

    private final SuggestionRepository suggestionRepository;
    private final EleveRepository eleveRepository;

    // ====== MAPPING ENTITÉ <-> DTO ======
    private SuggestionResponse toResponse(Suggestion suggestion) {
        if (suggestion == null) return null;

        return new SuggestionResponse(
                suggestion.getId(),
                suggestion.getContenu(),
                suggestion.getDateEnvoie(),
                new EleveSimpleResponse(
                        suggestion.getEleve().getId(),
                        suggestion.getEleve().getNom(),
                        suggestion.getEleve().getPrenom(),
                        suggestion.getEleve().getEmail()
                )
        );
    }

    private Suggestion toEntity(SuggestionRequest dto, Eleve eleve) {
        if (dto == null) return null;

        return new Suggestion(
                dto.getContenu(),
                eleve
        );
    }

    // ====== MÉTHODES POUR ÉLÈVES ======

    /**
     * Permet à un élève d'ajouter une suggestion
     */
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public SuggestionResponse ajouterSuggestion(SuggestionRequest suggestionRequest) {
        // Récupérer l'élève connecté depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Eleve eleve = eleveRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec email: " + email));

        // Créer et sauvegarder la suggestion
        Suggestion suggestion = toEntity(suggestionRequest, eleve);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        return toResponse(savedSuggestion);
    }

    /**
     * Permet à un élève de voir ses propres suggestions
     */
    @PreAuthorize("hasRole('ELEVE')")
    public List<SuggestionResponse> getSuggestionsParEleve() {
        // Récupérer l'élève connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Eleve eleve = eleveRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec email: " + email));

        List<Suggestion> suggestions = suggestionRepository.findByEleveIdOrderByDateEnvoieDesc(eleve.getId());

        return suggestions.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Permet à un élève de voir une de ses suggestions spécifique
     */
    @PreAuthorize("hasRole('ELEVE')")
    public SuggestionResponse getSuggestionParEleve(Long suggestionId) {
        // Récupérer l'élève connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Eleve eleve = eleveRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec email: " + email));

        Suggestion suggestion = suggestionRepository.findByIdAndEleveId(suggestionId, eleve.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion non trouvée avec id: " + suggestionId));

        return toResponse(suggestion);
    }

    // ====== MÉTHODES POUR ADMIN ======

    /**
     * Permet à un admin de voir toutes les suggestions
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<SuggestionResponse> getAllSuggestions() {
        List<Suggestion> suggestions = suggestionRepository.findAllByOrderByDateEnvoieDesc();

        return suggestions.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Permet à un admin de voir les suggestions d'un élève spécifique
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<SuggestionResponse> getSuggestionsParEleveId(Long eleveId) {
        // Vérifier que l'élève existe
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec id: " + eleveId));

        List<Suggestion> suggestions = suggestionRepository.findByEleveIdOrderByDateEnvoieDesc(eleveId);

        return suggestions.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Permet à un admin de voir une suggestion spécifique
     */
    @PreAuthorize("hasRole('ADMIN')")
    public SuggestionResponse getSuggestionById(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion non trouvée avec id: " + suggestionId));

        return toResponse(suggestion);
    }

    /**
     * Permet à un admin de supprimer une suggestion
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteSuggestion(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion non trouvée avec id: " + suggestionId));

        suggestionRepository.delete(suggestion);
    }

    /**
     * Statistiques des suggestions (pour admin)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public SuggestionStats getStatistiquesSuggestions() {
        long totalSuggestions = suggestionRepository.count();
        long suggestionsCeMois = suggestionRepository.countSuggestionsCeMois();
        long suggestionsCetteSemaine = suggestionRepository.countSuggestionsCetteSemaine();
        long suggestionsAujourdhui = suggestionRepository.countSuggestionsAujourdhui();

        return new SuggestionStats(totalSuggestions, suggestionsCeMois, suggestionsCetteSemaine, suggestionsAujourdhui);
    }

    // ====== CLASSE INTERNE POUR LES STATISTIQUES ======
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuggestionStats {
        private long totalSuggestions;
        private long suggestionsCeMois;
        private long suggestionsCetteSemaine;
        private long suggestionsAujourdhui;
    }
}