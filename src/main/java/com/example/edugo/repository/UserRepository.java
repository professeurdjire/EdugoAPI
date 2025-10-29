package com.example.edugo.repository;

import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver par email
    Optional<User> findByEmail(String email);

    // Vérifier l'existence par email
    Boolean existsByEmail(String email);

    // Trouver par rôle
    List<User> findByRole(Role role);

    // Trouver les utilisateurs actifs
    List<User> findByEstActiveTrue();

    // Recherche par nom ou prénom
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchByNomOrPrenom(@Param("searchTerm") String searchTerm);

    // Compter les utilisateurs par rôle
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") String role);

    // Trouver les utilisateurs avec pagination
    @Query("SELECT u FROM User u ORDER BY u.dateCreation DESC")
    List<User> findAllOrderByDateCreationDesc();
}