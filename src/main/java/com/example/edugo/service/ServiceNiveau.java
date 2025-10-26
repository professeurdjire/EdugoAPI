package com.example.edugo.service;

import com.example.edugo.dto.NiveauRequest;
import com.example.edugo.dto.NiveauResponse;
import com.example.edugo.entity.Principales.Niveau;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.NiveauRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceNiveau {

    private final NiveauRepository niveauRepository;

    public ServiceNiveau(NiveauRepository niveauRepository) {
        this.niveauRepository = niveauRepository;
    }

    //  Créer un nouveau niveau
    public NiveauResponse createNiveau(NiveauRequest request) {
        Niveau niveau = new Niveau();
        niveau.setNom(request.getNom());

        Niveau saved = niveauRepository.save(niveau);

        return new NiveauResponse(saved.getId(), saved.getNom());
    }

    // Récupérer tous les niveaux
    public List<NiveauResponse> getAllNiveaux() {
        return niveauRepository.findAll().stream()
                .map(niveau -> new NiveauResponse(niveau.getId(), niveau.getNom()))
                .collect(Collectors.toList());
    }

    // Récupérer un niveau par ID
    public NiveauResponse getNiveauById(Long id) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec id = " + id));

        return new NiveauResponse(niveau.getId(), niveau.getNom());
    }

    // Modifier un niveau existant
    public NiveauResponse updateNiveau(Long id, NiveauRequest request) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec id = " + id));

        niveau.setNom(request.getNom());

        Niveau updated = niveauRepository.save(niveau);

        return new NiveauResponse(updated.getId(), updated.getNom());
    }

    // Supprimer un niveau (simple)
    public void deleteNiveau(Long id) {
        if (!niveauRepository.existsById(id)) {
            throw new ResourceNotFoundException("Niveau non trouvé avec id = " + id);
        }
        niveauRepository.deleteById(id);
    }

    // Supprimer un niveau et retourner la représentation supprimée
    public NiveauResponse removeNiveau(Long id) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau non trouvé avec id = " + id));

        NiveauResponse response = new NiveauResponse(niveau.getId(), niveau.getNom());
        niveauRepository.delete(niveau);
        return response;
    }
}
