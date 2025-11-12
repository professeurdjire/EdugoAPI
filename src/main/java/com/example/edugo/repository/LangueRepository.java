package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Long> {
    List<Langue> findAllByOrderByLibelleAsc();
    Optional<Langue> findByCodeIso(String codeIso);
}