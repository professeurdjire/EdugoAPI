package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    /**
     * Trouver un device par son OneSignal Player ID
     */
    Optional<Device> findByOneSignalPlayerId(String oneSignalPlayerId);
    
    /**
     * Trouver tous les devices d'un utilisateur
     */
    @Query("SELECT d FROM Device d WHERE d.userId = :userId AND d.isActive = true")
    List<Device> findByUserIdAndActive(@Param("userId") Long userId);
    
    /**
     * Trouver tous les devices d'un utilisateur par rôle
     */
    @Query("SELECT d FROM Device d WHERE d.userId = :userId AND d.userRole = :role AND d.isActive = true")
    List<Device> findByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);
    
    /**
     * Trouver tous les devices actifs d'un rôle spécifique (pour notifications globales)
     */
    @Query("SELECT d FROM Device d WHERE d.userRole = :role AND d.isActive = true")
    List<Device> findByRoleAndActive(@Param("role") String role);
    
    /**
     * Trouver tous les devices actifs
     */
    @Query("SELECT d FROM Device d WHERE d.isActive = true")
    List<Device> findAllActive();
    
    /**
     * Désactiver un device par OneSignal Player ID
     */
    @Query("UPDATE Device d SET d.isActive = false WHERE d.oneSignalPlayerId = :playerId")
    void deactivateByPlayerId(@Param("playerId") String playerId);
}

