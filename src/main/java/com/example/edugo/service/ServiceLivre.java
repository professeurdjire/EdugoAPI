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
public class ServiceLivre {

    private final LivreRepository livreRepository;
    private final ProgressionRepository progressionRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;

    // ==================== CRUD LIVRES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Livre createLivre(Livre livre) {
        // Vérifier et associer les entités liées
        if (livre.getNiveau() != null) {
            Niveau niveau = niveauRepository.findById(livre.getNiveau().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", livre.getNiveau().getId()));
            livre.setNiveau(niveau);
        }
        
        if (livre.getClasse() != null) {
            Classe classe = classeRepository.findById(livre.getClasse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", livre.getClasse().getId()));
            livre.setClasse(classe);
        }
        
        if (livre.getMatiere() != null) {
            Matiere matiere = matiereRepository.findById(livre.getMatiere().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Matière", livre.getMatiere().getId()));
            livre.setMatiere(matiere);
        }
        
        return livreRepository.save(livre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Livre updateLivre(Long id, Livre livreDetails) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        
        livre.setTitre(livreDetails.getTitre());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setDescription(livreDetails.getDescription());
        livre.setTotalPages(livreDetails.getTotalPages());
        
        // Mettre à jour les relations
        if (livreDetails.getNiveau() != null) {
            Niveau niveau = niveauRepository.findById(livreDetails.getNiveau().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", livreDetails.getNiveau().getId()));
            livre.setNiveau(niveau);
        }
        
        if (livreDetails.getClasse() != null) {
            Classe classe = classeRepository.findById(livreDetails.getClasse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", livreDetails.getClasse().getId()));
            livre.setClasse(classe);
        }
        
        if (livreDetails.getMatiere() != null) {
            Matiere matiere = matiereRepository.findById(livreDetails.getMatiere().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Matière", livreDetails.getMatiere().getId()));
            livre.setMatiere(matiere);
        }
        
        return livreRepository.save(livre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        livreRepository.delete(livre);
    }

    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    public Livre getLivreById(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
    }

    // ==================== LIVRES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresDisponibles(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Retourner les livres adaptés au niveau/classe de l'élève
        if (eleve.getClasse() != null) {
            return livreRepository.findByClasseIdOrNiveauId(
                eleve.getClasse().getId(), 
                eleve.getClasse().getNiveau() != null ? eleve.getClasse().getNiveau().getId() : null
            );
        }
        
        return livreRepository.findAll();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresByMatiere(Long matiereId) {
        return livreRepository.findByMatiereId(matiereId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresByNiveau(Long niveauId) {
        return livreRepository.findByNiveauId(niveauId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresByClasse(Long classeId) {
        return livreRepository.findByClasseId(classeId);
    }

    // ==================== GESTION PROGRESSION DE LECTURE ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Progression updateProgressionLecture(Long eleveId, Long livreId, Integer pageActuelle) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", livreId));
        
        // Calculer le pourcentage de completion
        Integer pourcentageCompletion = (pageActuelle * 100) / livre.getTotalPages();
        
        // Chercher une progression existante ou en créer une nouvelle
        Progression progression = progressionRepository
                .findByEleveIdAndLivreId(eleveId, livreId)
                .orElse(new Progression());
        
        progression.setEleve(eleve);
        progression.setLivre(livre);
        progression.setPageActuelle(pageActuelle);
        progression.setPourcentageCompletion(pourcentageCompletion);
        progression.setDateMiseAJour(java.time.LocalDateTime.now());
        
        return progressionRepository.save(progression);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Progression> getProgressionLecture(Long eleveId) {
        return progressionRepository.findByEleveId(eleveId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public Progression getProgressionLivre(Long eleveId, Long livreId) {
        return progressionRepository.findByEleveIdAndLivreId(eleveId, livreId)
                .orElse(null);
    }

    // ==================== STATISTIQUES LIVRES ====================
    
    public Object getStatistiquesLivre(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", livreId));
        
        List<Progression> progressions = progressionRepository.findByLivreId(livreId);
        
        return new Object() {
            public final Long livreId = livre.getId();
            public final String titre = livre.getTitre();
            public final String auteur = livre.getAuteur();
            public final Integer totalPages = livre.getTotalPages();
            public final Integer nombreLecteurs = progressions.size();
            public final Integer nombreLecteursComplets = (int) progressions.stream()
                    .filter(p -> p.getPourcentageCompletion() >= 100)
                    .count();
            public final Double progressionMoyenne = progressions.isEmpty() ? 0.0 :
                    progressions.stream()
                            .mapToInt(Progression::getPourcentageCompletion)
                            .average()
                            .orElse(0.0);
        };
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Livre> searchLivresByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    public List<Livre> searchLivresByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    public List<Livre> getLivresRecents() {
        return livreRepository.findTop10ByOrderByIdDesc();
    }

    // ==================== RECOMMANDATIONS ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Livre> getLivresRecommandes(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Logique simple de recommandation basée sur la classe/niveau
        if (eleve.getClasse() != null) {
            return livreRepository.findByClasseId(eleve.getClasse().getId());
        }
        
        return livreRepository.findAll();
    }

    // ==================== LIVRES POPULAIRES ====================
    
    public List<Object> getLivresPopulaires() {
        List<Progression> progressions = progressionRepository.findAll();
        
        return progressions.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Progression::getLivre,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(entry -> new Object() {
                    public final Long livreId = entry.getKey().getId();
                    public final String titre = entry.getKey().getTitre();
                    public final String auteur = entry.getKey().getAuteur();
                    public final Long nombreLecteurs = entry.getValue();
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
