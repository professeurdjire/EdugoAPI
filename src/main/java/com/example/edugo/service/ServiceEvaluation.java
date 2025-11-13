package com.example.edugo.service;

import com.example.edugo.dto.SubmitRequest;
import com.example.edugo.dto.SubmitResultResponse;
import com.example.edugo.entity.Principales.Question;
import com.example.edugo.entity.Principales.ReponsePossible;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.QuestionRepository;
import com.example.edugo.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceEvaluation {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public ServiceEvaluation(QuizRepository quizRepository,
                             QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public SubmitResultResponse submitQuiz(Long quizId, SubmitRequest req) {
        // ensure quiz exists
        quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", quizId));

        // load questions of the quiz
        List<Question> questions = questionRepository.findByQuizId(quizId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        int score = 0;
        List<SubmitResultResponse.Detail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                // load with options
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultResponse.Detail d = new SubmitResultResponse.Detail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(quizId);
        res.setOwnerType("QUIZ");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }

    public SubmitResultResponse submitChallenge(Long challengeId, SubmitRequest req) {
        // challenge existence is implicitly checked by having questions; could add a repo if needed
        List<Question> questions = questionRepository.findByChallengeId(challengeId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        int score = 0;
        List<SubmitResultResponse.Detail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultResponse.Detail d = new SubmitResultResponse.Detail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(challengeId);
        res.setOwnerType("CHALLENGE");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }

    public SubmitResultResponse submitExercice(Long exerciceId, SubmitRequest req) {
        List<Question> questions = questionRepository.findByExerciceId(exerciceId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        int score = 0;
        List<SubmitResultResponse.Detail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultResponse.Detail d = new SubmitResultResponse.Detail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(exerciceId);
        res.setOwnerType("EXERCICE");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }
}
