package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    @Query("SELECT n FROM Notification n WHERE n.idEleve = :eleveId")
    List<Notification> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT n FROM Notification n WHERE n.idEleve = :eleveId AND (n.estVu IS NULL OR n.estVu = false)")
    List<Notification> findUnreadByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.idEleve = :eleveId AND (n.estVu IS NULL OR n.estVu = false)")
    Long countUnreadByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT n FROM Notification n WHERE n.idEleve = :eleveId ORDER BY n.dateExplication DESC")
    List<Notification> findByEleveIdOrderByDateDesc(@Param("eleveId") Long eleveId);
    
    @Query("SELECT n FROM Notification n WHERE (n.estVu IS NULL OR n.estVu = false) ORDER BY n.dateExplication DESC")
    List<Notification> findAllUnread();
}

