package com.example.edugo.service;

import com.example.edugo.dto.*;
import com.example.edugo.entity.ChatMessage;
import com.example.edugo.entity.ChatSession;
import com.example.edugo.entity.RessourceIA;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IAService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RessourceIARepository ressourceIARepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final MatiereRepository matiereRepository;
    private final String geminiApiKey;
    private final String openrouterApiKey;
    private final String openrouterModel;

    public IAService(ChatSessionRepository chatSessionRepository,
                     ChatMessageRepository chatMessageRepository,
                     RessourceIARepository ressourceIARepository,
                     EleveRepository eleveRepository,
                     LivreRepository livreRepository,
                     MatiereRepository matiereRepository,
                     @Value("${gemini.apiKey:}") String geminiApiKey,
                     @Value("${openrouter.apiKey:}") String openrouterApiKey,
                     @Value("${openrouter.model:openrouter/auto}") String openrouterModel) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.ressourceIARepository = ressourceIARepository;
        this.eleveRepository = eleveRepository;
        this.livreRepository = livreRepository;
        this.matiereRepository = matiereRepository;
        this.geminiApiKey = geminiApiKey;
        this.openrouterApiKey = openrouterApiKey;
        this.openrouterModel = openrouterModel;
    }

    @Transactional
    public ChatResponse sendMessage(ChatRequest req) {
        ChatSession session;
        if (req.getSessionId() != null) {
            session = chatSessionRepository.findById(req.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("ChatSession", req.getSessionId()));
        } else {
            if (req.getEleveId() == null) throw new IllegalArgumentException("eleveId requis pour créer une session");
            session = new ChatSession();
            session.setEleve(eleveRepository.findById(req.getEleveId())
                    .orElseThrow(() -> new ResourceNotFoundException("Élève", req.getEleveId())));
            if (req.getLivreId() != null) session.setLivre(livreRepository.findById(req.getLivreId()).orElse(null));
            if (req.getMatiereId() != null) session.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
            session.setTitre("Discussion IA");
            session = chatSessionRepository.save(session);
        }

        // store user message
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSession(session);
        userMsg.setRole(ChatMessage.Role.USER);
        userMsg.setContent(req.getMessage());
        chatMessageRepository.save(userMsg);

        // --- Intégration OpenRouter (Chat Completions compatible OpenAI) ---
        RestTemplate restTemplate = new RestTemplate();

        String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

        // Construire l'historique des messages pour le modèle, avec un message système
        List<ChatMessage> hist = chatMessageRepository.findBySessionId(session.getId());

        List<Map<String, String>> messages = new ArrayList<>();

        // Message système: contexte de l'éducation malienne
        StringBuilder systemContent = new StringBuilder();
        systemContent.append("Tu es un tuteur pédagogique numérique pour des élèves du système éducatif malien. ");
        systemContent.append("Explique les notions de façon simple, structurée et progressive, en français clair, ");
        systemContent.append("en tenant compte des programmes officiels du Mali (école fondamentale, collège, lycée). ");
        systemContent.append("Quand c'est pertinent, donne des exemples concrets liés au contexte malien (vie quotidienne, économie locale, culture). ");
        systemContent.append("Adapte le niveau de difficulté au niveau scolaire de l'élève et encourage-le avec un ton bienveillant. ");
        systemContent.append("Si la question porte sur un livre ou une matière précise, concentre-toi sur ce contenu pour aider à la compréhension et à la révision.");

        messages.add(Map.of(
                "role", "system",
                "content", systemContent.toString()
        ));

        // Ajouter ensuite l'historique utilisateur/assistant
        messages.addAll(
                hist.stream()
                        .map(m -> {
                            String role = (m.getRole() == ChatMessage.Role.USER)
                                    ? "user"
                                    : (m.getRole() == ChatMessage.Role.ASSISTANT ? "assistant" : "system");
                            String content = m.getContent() != null ? m.getContent() : "";
                            return Map.<String, String>of(
                                    "role", role,
                                    "content", content
                            );
                        })
                        .collect(Collectors.toList())
        );

        String modelToUse = (openrouterModel != null && !openrouterModel.isBlank()) ? openrouterModel : "openrouter/auto";

        Map<String, Object> body = Map.of(
                "model", modelToUse,
                "messages", messages
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (openrouterApiKey == null || openrouterApiKey.isBlank()) {
            // fallback explicite d'erreur si clé manquante
            ChatMessage assistant = new ChatMessage();
            assistant.setSession(session);
            assistant.setRole(ChatMessage.Role.ASSISTANT);
            assistant.setContent("[Erreur IA] openrouter.apiKey manquant côté serveur");
            chatMessageRepository.save(assistant);
        } else {
            headers.set("Authorization", "Bearer " + openrouterApiKey);
            // Optionnel mais recommandé par OpenRouter: référent et titre
            headers.set("HTTP-Referer", "http://localhost:8080");
            headers.set("X-Title", "Edugo Chat");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(OPENROUTER_URL, request, Map.class);
                Map<String, Object> respBody = response.getBody();
                String reponseIA = "";
                if (respBody != null && respBody.get("choices") instanceof List) {
                    List choices = (List) respBody.get("choices");
                    if (!choices.isEmpty()) {
                        Object first = choices.get(0);
                        if (first instanceof Map) {
                            Map firstMap = (Map) first;
                            Object message = firstMap.get("message");
                            if (message instanceof Map) {
                                Object content = ((Map) message).get("content");
                                if (content != null) reponseIA = content.toString();
                            }
                        }
                    }
                }

                if (reponseIA == null || reponseIA.isBlank()) {
                    reponseIA = "[IA] Réponse vide";
                }

                ChatMessage assistant = new ChatMessage();
                assistant.setSession(session);
                assistant.setRole(ChatMessage.Role.ASSISTANT);
                assistant.setContent(reponseIA);
                chatMessageRepository.save(assistant);
            } catch (Exception e) {
                ChatMessage assistant = new ChatMessage();
                assistant.setSession(session);
                assistant.setRole(ChatMessage.Role.ASSISTANT);
                assistant.setContent("[Erreur IA] OpenRouter: " + e.getMessage());
                chatMessageRepository.save(assistant);
            }
        }
        session.setUpdatedAt(LocalDateTime.now());
        chatSessionRepository.save(session);

        List<ChatMessage> history = chatMessageRepository.findBySessionId(session.getId());
        ChatResponse resp = new ChatResponse();
        resp.setSessionId(session.getId());
        resp.setMessages(history.stream().map(m -> {
            ChatResponse.Message mm = new ChatResponse.Message();
            mm.setRole(m.getRole().name());
            mm.setContent(m.getContent());
            mm.setCreatedAt(m.getCreatedAt());
            return mm;
        }).collect(Collectors.toList()));
        return resp;
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> listSessions(Long eleveId) {
        return chatSessionRepository.findByEleveId(eleveId).stream().map(this::toSessionDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatResponse getSession(Long sessionId) {
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatSession", sessionId));
        List<ChatMessage> history = chatMessageRepository.findBySessionId(sessionId);
        ChatResponse resp = new ChatResponse();
        resp.setSessionId(sessionId);
        resp.setMessages(history.stream().map(m -> {
            ChatResponse.Message mm = new ChatResponse.Message();
            mm.setRole(m.getRole().name());
            mm.setContent(m.getContent());
            mm.setCreatedAt(m.getCreatedAt());
            return mm;
        }).collect(Collectors.toList()));
        return resp;
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        ChatSession s = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatSession", sessionId));
        // delete messages first (cascade not defined)
        chatMessageRepository.findBySessionId(sessionId).forEach(m -> chatMessageRepository.delete(m));
        chatSessionRepository.delete(s);
    }

    @Transactional
    public RessourceIAResponse generateRessource(RessourceIARequest req) {
        if (req.getEleveId() == null) throw new IllegalArgumentException("eleveId requis");
        RessourceIA r = new RessourceIA();
        r.setEleve(eleveRepository.findById(req.getEleveId()).orElse(null));
        if (req.getLivreId() != null) r.setLivre(livreRepository.findById(req.getLivreId()).orElse(null));
        if (req.getMatiereId() != null) r.setMatiere(matiereRepository.findById(req.getMatiereId()).orElse(null));
        r.setTitre(req.getTitre() != null ? req.getTitre() : "Ressource IA");
        try {
            r.setType(RessourceIA.Type.valueOf(req.getType() != null ? req.getType() : "RESUME"));
        } catch (Exception e) {
            r.setType(RessourceIA.Type.RESUME);
        }
        // stub generation
        r.setContenu("[IA] Contenu généré (stub) pour: " + (req.getPrompt() != null ? req.getPrompt() : r.getTitre()));
        r = ressourceIARepository.save(r);
        return toRessourceDto(r);
    }

    @Transactional(readOnly = true)
    public List<RessourceIAResponse> listRessources(Long eleveId, Long livreId, String type) {
        RessourceIA.Type t = null;
        try { if (type != null) t = RessourceIA.Type.valueOf(type); } catch (Exception ignored) {}
        return ressourceIARepository.search(eleveId, livreId, t).stream().map(this::toRessourceDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RessourceIAResponse getRessource(Long id) {
        RessourceIA r = ressourceIARepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RessourceIA", id));
        return toRessourceDto(r);
    }

    private SessionResponse toSessionDto(ChatSession s) {
        SessionResponse dto = new SessionResponse();
        dto.setId(s.getId());
        dto.setTitre(s.getTitre());
        dto.setEleveId(s.getEleve() != null ? s.getEleve().getId() : null);
        dto.setLivreId(s.getLivre() != null ? s.getLivre().getId() : null);
        dto.setMatiereId(s.getMatiere() != null ? s.getMatiere().getId() : null);
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }

    private RessourceIAResponse toRessourceDto(RessourceIA r) {
        RessourceIAResponse dto = new RessourceIAResponse();
        dto.setId(r.getId());
        dto.setTitre(r.getTitre());
        dto.setType(r.getType() != null ? r.getType().name() : null);
        dto.setContenu(r.getContenu());
        dto.setFichierPath(r.getFichierPath());
        dto.setEleveId(r.getEleve() != null ? r.getEleve().getId() : null);
        dto.setLivreId(r.getLivre() != null ? r.getLivre().getId() : null);
        dto.setMatiereId(r.getMatiere() != null ? r.getMatiere().getId() : null);
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
