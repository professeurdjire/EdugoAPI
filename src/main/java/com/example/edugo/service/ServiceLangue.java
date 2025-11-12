package com.example.edugo.service;

import com.example.edugo.entity.Principales.Langue;
import com.example.edugo.repository.LangueRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceLangue {

    private final LangueRepository langueRepository;

    public ServiceLangue(LangueRepository langueRepository) {
        this.langueRepository = langueRepository;
    }

    // --- Lecture ---
    public List<Langue> getAllLangues() {
        return langueRepository.findAllByOrderByLibelleAsc();
    }

    public Optional<Langue> findById(Long id) {
        return langueRepository.findById(id);
    }

    public Optional<Langue> findByCodeIso(String codeIso) {
        return langueRepository.findByCodeIso(codeIso);
    }

    // --- Création ---
    public Langue save(Langue langue) {
        return langueRepository.save(langue);
    }

    // --- Mise à jour ---
    public Langue update(Long id, Langue updatedLangue) {
        return langueRepository.findById(id)
                .map(existing -> {
                    existing.setNom(updatedLangue.getNom());
                    existing.setCodeIso(updatedLangue.getCodeIso());
                    // ajouter ici d'autres champs si nécessaire
                    return langueRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Langue non trouvée avec id : " + id));
    }

    // --- Suppression ---
    public void delete(Long id) {
        if (!langueRepository.existsById(id)) {
            throw new RuntimeException("Langue non trouvée avec id : " + id);
        }
        langueRepository.deleteById(id);
    }
}
