package com.example.edugo.service;

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
    public Classe createClasse(Classe classe) {
        if (classe.getNiveau() != null) {
            Niveau niveau = niveauRepository.findById(classe.getNiveau().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", classe.getNiveau().getId()));
            classe.setNiveau(niveau);
        }
        return classeRepository.save(classe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Classe updateClasse(Long id, Classe classeDetails) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        
        classe.setNom(classeDetails.getNom());
        
        if (classeDetails.getNiveau() != null) {
            Niveau niveau = niveauRepository.findById(classeDetails.getNiveau().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", classeDetails.getNiveau().getId()));
            classe.setNiveau(niveau);
        }
        
        return classeRepository.save(classe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        
        // Vérifier s'il y a des élèves dans cette classe
        List<Eleve> eleves = eleveRepository.findByClasseId(id);
        if (!eleves.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une classe contenant des élèves");
        }
        
        classeRepository.delete(classe);
    }

    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    public Classe getClasseById(Long id) {
        return classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
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
            public final Long classeId = classe.getId();
            public final String nomClasse = classe.getNom();
            public final String niveau = classe.getNiveau() != null ? classe.getNiveau().getNom() : "Non défini";
            public final Integer nombreEleves = eleves.size();
            public final Integer pointsMoyens = eleves.isEmpty() ? 0 : 
                eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
        };
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Classe> getClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId);
    }

    public List<Classe> searchClassesByName(String nom) {
        return classeRepository.findByNomContainingIgnoreCase(nom);
    }
}
