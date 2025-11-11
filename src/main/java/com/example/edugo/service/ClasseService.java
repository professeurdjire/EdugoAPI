package com.example.edugo.service;

import com.example.edugo.dto.ClasseRequest;
import com.example.edugo.dto.ClasseResponse;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Niveau;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.ClasseRepository;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.NiveauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClasseService {

    private final ClasseRepository classeRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;

    // ==================== CRUD CLASSES ====================

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ClasseResponse createClasse(ClasseRequest request) {
        Niveau niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", request.getNiveauId()));

        Classe classe = new Classe();
        classe.setNom(request.getNom());
        classe.setNiveau(niveau);

        Classe saved = classeRepository.save(classe);
        return mapToResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ClasseResponse updateClasse(Long id, ClasseRequest request) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));

        classe.setNom(request.getNom());

        if (request.getNiveauId() != null) {
            Niveau niveau = niveauRepository.findById(request.getNiveauId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", request.getNiveauId()));
            classe.setNiveau(niveau);
        }

        Classe updated = classeRepository.save(classe);
        return mapToResponse(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));

        List<Eleve> eleves = eleveRepository.findByClasseId(id);
        if (!eleves.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une classe contenant des élèves");
        }

        classeRepository.delete(classe);
    }

    public List<ClasseResponse> getAllClasses() {
        return classeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClasseResponse getClasseById(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        return mapToResponse(classe);
    }

    // ==================== GESTION ÉLÈVES DANS LES CLASSES ====================

    @PreAuthorize("hasRole('ADMIN')")
    public List<Eleve> getElevesByClasse(Long classeId) {
        return eleveRepository.findByClasseId(classeId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Eleve assignerEleveAClasse(Long eleveId, Long classeId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", classeId));

        eleve.setClasse(classe);
        return eleveRepository.save(eleve);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void retirerEleveDeClasse(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        eleve.setClasse(null);
        eleveRepository.save(eleve);
    }

    // ==================== STATISTIQUES CLASSE ====================

    public Object getStatistiquesClasse(Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", classeId));

        List<Eleve> eleves = eleveRepository.findByClasseId(classeId);

        return new Object() {
            public final Long classeIdField = classe.getId();
            public final String nomClasse = classe.getNom();
            public final String niveau = classe.getNiveau() != null ? classe.getNiveau().getNom() : "Non défini";
            public final Integer nombreEleves = eleves.size();
            public final Integer pointsMoyens = eleves.isEmpty() ? 0 :
                    eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
        };
    }

    // ==================== RECHERCHE ET FILTRAGE ====================

    public List<ClasseResponse> getClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ClasseResponse> searchClassesByName(String nom) {
        return classeRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==================== MÉTHODE UTILITAIRE ====================

    private ClasseResponse mapToResponse(Classe classe) {
        ClasseResponse response = new ClasseResponse();
        response.setId(classe.getId());
        response.setNom(classe.getNom());

        if (classe.getNiveau() != null) {
            response.setNiveauId(classe.getNiveau().getId());
            response.setNiveauNom(classe.getNiveau().getNom());
        }

        return response;
    }
}
