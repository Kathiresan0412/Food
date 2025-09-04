package org.example.repository;

import org.example.model.ActivityLog;
import org.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    List<ActivityLog> findByUser(User user);
    
    List<ActivityLog> findByAction(String action);
    
    List<ActivityLog> findByEntityType(String entityType);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.user = :user ORDER BY al.createdAt DESC")
    List<ActivityLog> findByUserOrderByCreatedAtDesc(@Param("user") User user);
    
    @Query("SELECT al FROM ActivityLog al ORDER BY al.createdAt DESC")
    Page<ActivityLog> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<ActivityLog> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.user = :user AND al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<ActivityLog> findByUserAndCreatedAtBetween(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.entityType = :entityType AND al.entityId = :entityId ORDER BY al.createdAt DESC")
    List<ActivityLog> findByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Long entityId);
}
