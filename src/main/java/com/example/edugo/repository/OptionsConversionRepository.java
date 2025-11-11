package com.example.edugo.repository;

import com.example.edugo.entity.Principales.OptionsConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsConversionRepository extends JpaRepository<OptionsConversion, Long> {
    List<OptionsConversion> findByEtatTrue();
}

