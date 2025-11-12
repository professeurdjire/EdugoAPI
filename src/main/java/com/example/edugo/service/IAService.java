package com.example.edugo.service;

import com.example.edugo.dto.*;
import com.example.edugo.entity.ChatMessage;
import com.example.edugo.entity.ChatSession;
import com.example.edugo.entity.RessourceIA;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IAService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RessourceIARepository ressourceIARepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final MatiereRepository matiereRepository;

    public IAService(ChatSessionRepository chatSessionRepository,
                     ChatMessageRepository chatMessageRepository,
                     RessourceIARepository ressourceIARepository,
                     EleveRepository eleveRepository,
                     LivreRepository livreRepository,
                     MatiereRepository matiereRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.ressourceIARepository = ressourceIARepository;
        this.eleveRepository = eleveRepository;
        this.livreRepository = livreRepository;
        this.matiereRepository = matiereRepository;
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

        // stub LLM response
        ChatMessage assistant = new ChatMessage();
        assistant.setSession(session);
        assistant.setRole(ChatMessage.Role.ASSISTANT);
        assistant.setContent("[IA] Réponse générée (stub) à: " + req.getMessage());
        chatMessageRepository.save(assistant);

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
