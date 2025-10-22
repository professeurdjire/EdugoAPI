package com.example.edugo.repository;



import com.example.edugo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Trouver par email
    Optional<Admin> findByEmail(String email);

    // Trouver les admins actifs
    List<Admin> findByEstActiveTrue();

    // Compter le nombre d'admins
    @Query("SELECT COUNT(a) FROM Admin a")
    Long countAdmins();

    // Trouver les admins avec leurs classes validées
    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.classesValidees WHERE a.id = :adminId")
    Optional<Admin> findByIdWithClassesValidees(@Param("adminId") Long adminId);
}
