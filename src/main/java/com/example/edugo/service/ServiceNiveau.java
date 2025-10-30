package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceNiveau {

    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;

    // ==================== CRUD NIVEAUX ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Niveau createNiveau(Niveau niveau) {
        if (niveauRepository.existsByNom(niveau.getNom())) {
            throw new RuntimeException("Ce niveau existe déjà");
        }
        return niveauRepository.save(niveau);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Niveau updateNiveau(Long id, Niveau niveauDetails) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        
        niveau.setNom(niveauDetails.getNom());
        return niveauRepository.save(niveau);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteNiveau(Long id) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        
        // Vérifier s'il y a des classes associées à ce niveau
        List<Classe> classes = classeRepository.findByNiveauId(id);
        if (!classes.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un niveau contenant des classes");
        }
        
        niveauRepository.delete(niveau);
    }

    public List<Niveau> getAllNiveaux() {
        return niveauRepository.findAll();
    }

    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
    }

    // ==================== GESTION DES CLASSES PAR NIVEAU ====================
    
    public List<Classe> getClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Classe assignerClasseANiveau(Long classeId, Long niveauId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", classeId));
        
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));
        
        classe.setNiveau(niveau);
        return classeRepository.save(classe);
    }

    // ==================== STATISTIQUES NIVEAUX ====================
    
    public Object getStatistiquesNiveau(Long niveauId) {
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));
        
        List<Classe> classes = classeRepository.findByNiveauId(niveauId);
        List<Eleve> eleves = eleveRepository.findByClasseNiveauId(niveauId);
        List<Livre> livres = livreRepository.findByNiveauId(niveauId);
        
        return new Object() {
            public final Long niveauId = niveau.getId();
            public final String nomNiveau = niveau.getNom();
            public final Integer nombreClasses = classes.size();
            public final Integer nombreEleves = eleves.size();
            public final Integer nombreLivres = livres.size();
            public final Integer pointsMoyens = eleves.isEmpty() ? 0 : 
                eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
        };
    }

    // ==================== HIÉRARCHIE DES NIVEAUX ====================
    
    public List<Niveau> getNiveauxParOrdre() {
        return niveauRepository.findAllByOrderByNomAsc();
    }

    public Niveau getNiveauSuivant(Long niveauId) {
        List<Niveau> niveaux = niveauRepository.findAllByOrderByNomAsc();
        int currentIndex = -1;
        
        for (int i = 0; i < niveaux.size(); i++) {
            if (niveaux.get(i).getId().equals(niveauId)) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex >= 0 && currentIndex < niveaux.size() - 1) {
            return niveaux.get(currentIndex + 1);
        }
        
        return null;
    }

    public Niveau getNiveauPrecedent(Long niveauId) {
        List<Niveau> niveaux = niveauRepository.findAllByOrderByNomAsc();
        int currentIndex = -1;
        
        for (int i = 0; i < niveaux.size(); i++) {
            if (niveaux.get(i).getId().equals(niveauId)) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex > 0) {
            return niveaux.get(currentIndex - 1);
        }
        
        return null;
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Niveau> searchNiveauxByName(String nom) {
        return niveauRepository.findByNomContainingIgnoreCase(nom);
    }

    // ==================== VALIDATION NIVEAUX ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    public boolean validerNiveau(Long niveauId) {
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauId));
        
        // Vérifier si le niveau a au moins une classe
        List<Classe> classes = classeRepository.findByNiveauId(niveauId);
        return !classes.isEmpty();
    }

    // ==================== MIGRATION DES ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void migrerElevesVersNiveau(Long niveauIdSource, Long niveauIdDestination) {
        Niveau niveauDestination = niveauRepository.findById(niveauIdDestination)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", niveauIdDestination));
        
        List<Classe> classesSource = classeRepository.findByNiveauId(niveauIdSource);
        
        for (Classe classe : classesSource) {
            classe.setNiveau(niveauDestination);
            classeRepository.save(classe);
        }
    }
}
