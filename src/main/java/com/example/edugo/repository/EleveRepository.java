package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EleveRepository extends JpaRepository<Eleve, Long> {
}
