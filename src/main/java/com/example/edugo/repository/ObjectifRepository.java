package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Objectif;
import com.example.edugo.entity.Principales.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Long> {

    List<Objectif> findByEleveOrderByDateEnvoieDesc(Eleve eleve);
    List<Objectif> findByEleveAndStatut(Eleve eleve, String statut);
    List<Objectif> findByEleveAndStatutOrderByDateEnvoieDesc(Eleve eleve, String statut);
    Optional<Objectif> findByIdObjectifAndEleve(Long idObjectif, Eleve eleve);
}