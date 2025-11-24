package com.example.edugo.service;

import com.example.edugo.dto.QuizResponse;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Quiz;
import com.example.edugo.entity.StatutQuiz;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.LivreRepository;
import com.example.edugo.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceQuiz {

    private final QuizRepository quizRepository;
    private final LivreRepository livreRepository;
    private final EleveRepository eleveRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private QuizResponse toResponse(Quiz quiz) {
        if (quiz == null) return null;
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitre(quiz.getTitre());
        response.setNombreQuestions(quiz.getNombreQuestions());
        // Note: Quiz est lié à Livre, donc titre/description viennent du livre
        if (quiz.getLivre() != null) {
            response.setLivreId(quiz.getLivre().getId());
            response.setTitreLivre(quiz.getLivre().getTitre());
            response.setDescription(quiz.getLivre().getDescription());
            response.setAuteur(quiz.getLivre().getAuteur());
            if (quiz.getLivre().getMatiere() != null) {
                response.setMatiereId(quiz.getLivre().getMatiere().getId());
                response.setMatiereNom(quiz.getLivre().getMatiere().getNom());
            }
            if (quiz.getLivre().getNiveau() != null) {
                response.setNiveauId(quiz.getLivre().getNiveau().getId());
                response.setNiveauNom(quiz.getLivre().getNiveau().getNom());
            }
        }
        return response;
    }

    // ====== CRUD DTO ======
    public List<QuizResponse> getAllQuizzesDto() {
        return quizRepository.findAllWithRelations().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public QuizResponse getQuizByIdDto(Long id) {
        Quiz quiz = quizRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        return toResponse(quiz);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public QuizResponse createQuiz(com.example.edugo.dto.QuizCreateRequest dto) {
        Livre livre = livreRepository.findById(dto.getLivreId())
                .orElseThrow(() -> new ResourceNotFoundException("Livre", dto.getLivreId()));
        Quiz quiz = new Quiz(livre);
        if (dto.getTitre() != null && !dto.getTitre().isBlank()) {
            quiz.setTitre(dto.getTitre());
        }
        // Lier explicitement les deux côtés de la relation OneToOne
        livre.setQuiz(quiz);
        quiz.setLivre(livre);
        // Sauvegarder d'abord le quiz pour obtenir son ID
        Quiz saved = quizRepository.save(quiz);
        // Puis sauvegarder le livre pour mettre à jour quiz_id dans la table livres
        // (Livre est le propriétaire de la relation avec @JoinColumn)
        livreRepository.save(livre);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public QuizResponse updateQuiz(Long id, Quiz quizDetails) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        quiz.setStatut(quizDetails.getStatut());
        Quiz saved = quizRepository.save(quiz);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        quizRepository.delete(quiz);
    }

    public List<QuizResponse> getQuizzesByStatut(StatutQuiz statut) {
        // Utiliser findAllWithRelations et filtrer par statut pour éviter les problèmes de lazy loading
        List<Quiz> allQuizzes = quizRepository.findAllWithRelations();
        List<Quiz> filtered = allQuizzes.stream()
                .filter(q -> statut == StatutQuiz.ACTIF ? q.getStatut() == StatutQuiz.ACTIF : q.getStatut() != StatutQuiz.ACTIF)
                .collect(Collectors.toList());
        return filtered.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ==================== QUIZZES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public List<QuizResponse> getQuizzesDisponibles(Long eleveId) {
        Eleve eleve = eleveRepository.findByIdWithRelations(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Récupérer tous les quizzes actifs avec leurs relations
        List<Quiz> allQuizzes = quizRepository.findAllWithRelations();
        
        // Filtrer les quizzes actifs qui correspondent à la classe ou au niveau de l'élève
        return allQuizzes.stream()
                .filter(quiz -> quiz.getStatut() == StatutQuiz.ACTIF)
                .filter(quiz -> {
                    if (quiz.getLivre() == null) return false;
                    Livre livre = quiz.getLivre();
                    
                    // Récupérer les IDs de classe et niveau de l'élève
                    Long classeId = eleve.getClasse() != null ? eleve.getClasse().getId() : null;
                    Long niveauIdClasse = (eleve.getClasse() != null && eleve.getClasse().getNiveau() != null) 
                            ? eleve.getClasse().getNiveau().getId() : null;
                    Long niveauIdDirect = eleve.getNiveau() != null ? eleve.getNiveau().getId() : null;
                    
                    // Vérifier si le livre correspond à la classe de l'élève
                    if (classeId != null && livre.getClasse() != null 
                            && livre.getClasse().getId().equals(classeId)) {
                        return true;
                    }
                    
                    // Vérifier si le livre correspond au niveau (via classe ou directement)
                    Long niveauIdFinal = niveauIdDirect != null ? niveauIdDirect : niveauIdClasse;
                    if (niveauIdFinal != null && livre.getNiveau() != null 
                            && livre.getNiveau().getId().equals(niveauIdFinal)) {
                        return true;
                    }
                    
                    // Si l'élève n'a ni classe ni niveau, retourner tous les quizzes actifs
                    return classeId == null && niveauIdFinal == null;
                })
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
