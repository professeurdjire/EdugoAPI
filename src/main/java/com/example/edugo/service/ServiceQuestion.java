package com.example.edugo.service;

import com.example.edugo.dto.QuestionRequest;
import com.example.edugo.dto.QuestionResponse;
import com.example.edugo.dto.ReponsePossibleRequest;
import com.example.edugo.dto.ReponsePossibleResponse;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ServiceQuestion {

    private final QuestionRepository questionRepository;
    private final TypeQuestionRepository typeQuestionRepository;
    private final QuizRepository quizRepository;
    private final ExerciceRepository exerciceRepository;
    private final ChallengeRepository challengeRepository;
    private final DefiRepository defiRepository;

    public ServiceQuestion(QuestionRepository questionRepository,
                           TypeQuestionRepository typeQuestionRepository,
                           QuizRepository quizRepository,
                           ExerciceRepository exerciceRepository,
                           ChallengeRepository challengeRepository,
                           DefiRepository defiRepository) {
        this.questionRepository = questionRepository;
        this.typeQuestionRepository = typeQuestionRepository;
        this.quizRepository = quizRepository;
        this.exerciceRepository = exerciceRepository;
        this.challengeRepository = challengeRepository;
        this.defiRepository = defiRepository;
    }

    @Transactional
    public QuestionResponse create(QuestionRequest req) {
        validateOwner(req);
        validateByType(req);

        Question q = new Question();
        q.setEnonce(req.getEnonce());
        q.setPoints(req.getPoints() != null ? req.getPoints() : 1);
        q.setDateCreation(LocalDateTime.now());
        q.setDateModification(LocalDateTime.now());

        // Resolve type
        String libelleType = req.getType();
        TypeQuestion type = typeQuestionRepository.findByLibelleTypeIgnoreCase(libelleType)
                .orElseGet(() -> typeQuestionRepository.save(new TypeQuestion(libelleType)));
        q.setType(type);

        // Resolve owner
        if (req.getQuizId() != null) {
            Quiz quiz = quizRepository.findById(req.getQuizId())
                    .orElseThrow(() -> new ResourceNotFoundException("Quiz", req.getQuizId()));
            q.setQuiz(quiz);
        } else if (req.getExerciceId() != null) {
            Exercice ex = exerciceRepository.findById(req.getExerciceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercice", req.getExerciceId()));
            q.setExercice(ex);
        } else if (req.getChallengeId() != null) {
            Challenge ch = challengeRepository.findById(req.getChallengeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challenge", req.getChallengeId()));
            q.setChallenge(ch);
        } else if (req.getDefiId() != null) {
            Defi df = defiRepository.findById(req.getDefiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Défi", req.getDefiId()));
            q.setDefi(df);
        }

        // Map responses for MCQ types
        if (req.getReponses() != null) {
            List<ReponsePossible> options = req.getReponses().stream().map(r -> {
                ReponsePossible rp = new ReponsePossible();
                rp.setLibelleReponse(r.getLibelle());
                rp.setEstCorrecte(Boolean.TRUE.equals(r.getEstCorrecte()));
                rp.setQuestion(q);
                return rp;
            }).collect(Collectors.toList());
            q.setReponsesPossibles(options);
        }

        Question saved = questionRepository.save(q);
        return toResponse(saved);
    }

    private void validateOwner(QuestionRequest req) {
        int owners = 0;
        if (req.getQuizId() != null) owners++;
        if (req.getExerciceId() != null) owners++;
        if (req.getChallengeId() != null) owners++;
        if (req.getDefiId() != null) owners++;
        if (owners != 1) {
            throw new IllegalArgumentException("Exactly one owner must be set among quizId, exerciceId, challengeId, defiId");
        }
    }

    private void validateByType(QuestionRequest req) {
        String t = Objects.requireNonNullElse(req.getType(), "").toUpperCase();
        List<ReponsePossibleRequest> reps = req.getReponses();
        if ("QCU".equals(t)) {
            long correct = reps == null ? 0 : reps.stream().filter(r -> Boolean.TRUE.equals(r.getEstCorrecte())).count();
            if (correct != 1) throw new IllegalArgumentException("QCU requires exactly one correct answer");
            if (reps == null || reps.size() < 2) throw new IllegalArgumentException("QCU requires at least 2 options");
        } else if ("QCM".equals(t)) {
            if (reps == null || reps.size() < 2) throw new IllegalArgumentException("QCM requires at least 2 options");
            long correct = reps.stream().filter(r -> Boolean.TRUE.equals(r.getEstCorrecte())).count();
            if (correct < 1) throw new IllegalArgumentException("QCM requires at least one correct answer");
        } else if ("VRAI_FAUX".equals(t)) {
            if (reps == null || reps.size() != 2) throw new IllegalArgumentException("VRAI_FAUX requires exactly 2 options");
            long correct = reps.stream().filter(r -> Boolean.TRUE.equals(r.getEstCorrecte())).count();
            if (correct != 1) throw new IllegalArgumentException("VRAI_FAUX requires exactly one correct option");
        } else if ("APPARIEMENT".equals(t)) {
            // Appariement: pas de schéma strict pour l'instant, exiger au moins 2 options
            if (reps == null || reps.size() < 2) throw new IllegalArgumentException("APPARIEMENT requires at least 2 options");
        } else {
            throw new IllegalArgumentException("Unsupported type: " + t);
        }
    }

    private QuestionResponse toResponse(Question q) {
        QuestionResponse dto = new QuestionResponse();
        dto.setId(q.getId());
        dto.setIntitule(q.getEnonce());
        dto.setType(q.getType() != null ? q.getType().getLibelleType() : null);
        dto.setReponsesPossibles(q.getReponsesPossibles() == null ? List.of() : q.getReponsesPossibles().stream()
                .map(r -> new ReponsePossibleResponse(r.getId(), r.getLibelleReponse(), r.isEstCorrecte()))
                .collect(Collectors.toList()));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> listByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> listByExercice(Long exerciceId) {
        return questionRepository.findByExerciceId(exerciceId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> listByChallenge(Long challengeId) {
        return questionRepository.findByChallengeId(challengeId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> listByDefi(Long defiId) {
        return questionRepository.findByDefiId(defiId).stream().map(this::toResponse).collect(Collectors.toList());
    }
}

