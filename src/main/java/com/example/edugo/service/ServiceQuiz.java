package com.example.edugo.service;

import com.example.edugo.dto.QuizResponse;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Quiz;
import com.example.edugo.entity.StatutQuiz;
import com.example.edugo.exception.ResourceNotFoundException;
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

    // ====== MAPPING ENTITE <-> DTO ======
    private QuizResponse toResponse(Quiz quiz) {
        if (quiz == null) return null;
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitre(quiz.getTitre());
        response.setNombreQuestions(quiz.getNombreQuestions());
        // Note: Quiz est lié à Livre, donc titre/description viennent du livre
        if (quiz.getLivre() != null) {
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
        return quizRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public QuizResponse getQuizByIdDto(Long id) {
        Quiz quiz = quizRepository.findById(id)
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
        Quiz saved = quizRepository.save(quiz);
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
        if (statut == StatutQuiz.ACTIF) {
            return quizRepository.findByStatutActif().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        } else {
            return quizRepository.findByStatutInactif().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }
    }
}
