package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    Optional <Livre> findByTitre(String titre);

}
