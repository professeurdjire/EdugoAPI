package com.example.edugo.service;

import com.example.edugo.dto.MatiereRequest;
import com.example.edugo.dto.MatiereResponse;
import com.example.edugo.entity.Principales.Matiere;
import com.example.edugo.repository.MatiereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceMatiere {

    private final MatiereRepository matiereRepository;

    // -------------------- CREATE --------------------
    public MatiereResponse createMatiere(MatiereRequest request) {
        Matiere matiere = new Matiere();
        matiere.setNom(request.getNom());

        Matiere savedMatiere = matiereRepository.save(matiere);
        return mapToResponse(savedMatiere);
    }

    // -------------------- READ --------------------
    public List<MatiereResponse> getAllMatieres() {
        return matiereRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MatiereResponse getMatiereById(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'id: " + id));
        return mapToResponse(matiere);
    }

    // -------------------- UPDATE --------------------
    public MatiereResponse updateMatiere(Long id, MatiereRequest request) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'id: " + id));

        matiere.setNom(request.getNom());

        Matiere updatedMatiere = matiereRepository.save(matiere);
        return mapToResponse(updatedMatiere);
    }

    // -------------------- DELETE --------------------
    public void deleteMatiere(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'id: " + id));
        matiereRepository.delete(matiere);
    }

    // -------------------- MAPPER --------------------
    private MatiereResponse mapToResponse(Matiere matiere) {
        return new MatiereResponse(
                matiere.getId(),
                matiere.getNom()
        );
    }
}
