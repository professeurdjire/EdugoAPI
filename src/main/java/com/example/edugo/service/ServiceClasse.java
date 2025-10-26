package com.example.edugo.service;

import com.example.edugo.dto.ClasseRequest;
import com.example.edugo.dto.ClasseResponse;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Niveau;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.ClasseRepository;
import com.example.edugo.repository.NiveauRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceClasse {

    private final ClasseRepository classeRepository;
    private final NiveauRepository niveauRepository;

    public ServiceClasse(ClasseRepository classeRepository, NiveauRepository niveauRepository) {
        this.classeRepository = classeRepository;
        this.niveauRepository = niveauRepository;
    }

    // Ajouter une nouvelle classe
    public ClasseResponse createClasse(ClasseRequest request) {
        Niveau niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new RuntimeException("Niveau non trouvé"));

        Classe classe = new Classe();
        classe.setNom(request.getNom());
        classe.setNiveau(niveau);

        Classe saved = classeRepository.save(classe);

        // Retourner la réponse sans l'id
        return new ClasseResponse(null, saved.getNom(), saved.getNiveau().getNom());
    }

    // Afficher toutes les classes
    public List<ClasseResponse> getAllClasses() {
        return classeRepository.findAll().stream()
                .map(classe -> new ClasseResponse(classe.getId(), classe.getNom(), classe.getNiveau().getNom()))
                .collect(Collectors.toList());
    }
    // Récupérer une classe par son id
    public ClasseResponse getClasseById(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée avec id = " + id));

        return new ClasseResponse(classe.getId(), classe.getNom(), classe.getNiveau().getNom());
    }

    // Modifier une classe existante
    public ClasseResponse updateClasse(Long id, ClasseRequest request) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée avec id = " + id));

        // Mise à jour du nom
        classe.setNom(request.getNom());

        // Mise à jour du niveau (si différent)
        Niveau niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec id = " + request.getNiveauId()));

        classe.setNiveau(niveau);

        Classe updated = classeRepository.save(classe);

        // Retourner la réponse sans l'id
        return new ClasseResponse(classe.getId(), updated.getNom(), updated.getNiveau().getNom());
    }

    // Supprimer une classe (simple)
    public void deleteClasse(Long id) {
        if (!classeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Classe non trouvée avec id = " + id);
        }
        classeRepository.deleteById(id);
    }

    // Supprimer une classe et retourner la représentation supprimée
    public ClasseResponse removeClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée avec id = " + id));

        ClasseResponse response = new ClasseResponse(null, classe.getNom(), classe.getNiveau().getNom());
        classeRepository.delete(classe);
        return response;
    }
}
