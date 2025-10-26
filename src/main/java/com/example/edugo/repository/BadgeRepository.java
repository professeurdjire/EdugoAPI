package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
