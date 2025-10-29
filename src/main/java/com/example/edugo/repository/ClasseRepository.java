package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Trouver par nom
    Optional<Classe> findByNom(String nom);

}
