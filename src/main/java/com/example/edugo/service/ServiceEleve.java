package com.example.edugo.service;

import com.example.edugo.dto.EleveRequest;
import com.example.edugo.dto.EleveResponse;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.repository.ClasseRepository;
import com.example.edugo.repository.EleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceEleve {

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private ClasseRepository classeRepository;

    /**
     *  Créer un élève à partir du DTO EleveRequest
     */
    public EleveResponse createEleve(EleveRequest request) {
        Classe classe = classeRepository.findById(request.getClasseId())
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));

        Eleve eleve = new Eleve();
        eleve.setEmail(request.getEmail());
        eleve.setMotDePasse(request.getPassword());
        eleve.setPrenom(request.getFirstName());
        eleve.setNom(request.getLastName());
        eleve.setDateNaissance(request.getDateNaissance());
        eleve.setClasse(classe);
        eleve.setPointAccumule(request.getPointAccumule() != null ? request.getPointAccumule() : 0);
        eleve.setAvatarUrl(request.getAvatarUrl());

        Eleve savedEleve = eleveRepository.save(eleve);
        return mapToResponse(savedEleve);
    }

    /**
     * Récupérer tous les élèves
     */
    public List<EleveResponse> getAllEleves() {
        return eleveRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un élève par son ID
     */
    public EleveResponse getEleveById(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé avec l'ID : " + id));
        return mapToResponse(eleve);
    }

    /**
     * Mettre à jour un élève
     */
    public EleveResponse updateEleve(Long id, EleveRequest request) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé avec l'ID : " + id));

        Classe classe = classeRepository.findById(request.getClasseId())
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));

        eleve.setEmail(request.getEmail());
        eleve.setMotDePasse(request.getPassword());
        eleve.setPrenom(request.getFirstName());
        eleve.setNom(request.getLastName());
        eleve.setDateNaissance(request.getDateNaissance());
        eleve.setClasse(classe);
        eleve.setPointAccumule(request.getPointAccumule() != null ? request.getPointAccumule() : eleve.getPointAccumule());
        eleve.setAvatarUrl(request.getAvatarUrl());

        Eleve updated = eleveRepository.save(eleve);
        return mapToResponse(updated);
    }

    /**
     * Supprimer un élève
     */
    public void deleteEleve(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé avec l'ID : " + id));
        eleveRepository.delete(eleve);
    }

    /**
     * Méthode utilitaire pour transformer une entité en DTO Response
     */
    private EleveResponse mapToResponse(Eleve eleve) {
        return new EleveResponse(
                eleve.getId(),
                eleve.getEmail(),
                eleve.getPrenom(),
                eleve.getNom(),
                eleve.getDateNaissance(),
                eleve.getClasse() != null ? eleve.getClasse().getNom() : null,
                eleve.getPointAccumule(),
                eleve.getAvatarUrl()
        );
    }
}
