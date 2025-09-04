package org.example.service;

import org.example.model.ActivityLog;
import org.example.model.User;
import org.example.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ActivityLogService {
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    public void logActivity(String action, String description, User user) {
        ActivityLog activityLog = new ActivityLog(action, description, user);
        activityLogRepository.save(activityLog);
    }
    
    public void logActivity(String action, String description, User user, String entityType, Long entityId) {
        ActivityLog activityLog = new ActivityLog(action, description, entityType, entityId, user);
        activityLogRepository.save(activityLog);
    }
    
    public void logActivity(String action, String description, User user, String entityType, Long entityId, String ipAddress, String userAgent) {
        ActivityLog activityLog = new ActivityLog(action, description, entityType, entityId, user);
        activityLog.setIpAddress(ipAddress);
        activityLog.setUserAgent(userAgent);
        activityLogRepository.save(activityLog);
    }
    
    public List<ActivityLog> getActivityLogsByUser(User user) {
        return activityLogRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<ActivityLog> getActivityLogsByAction(String action) {
        return activityLogRepository.findByAction(action);
    }
    
    public List<ActivityLog> getActivityLogsByEntityType(String entityType) {
        return activityLogRepository.findByEntityType(entityType);
    }
    
    public List<ActivityLog> getActivityLogsByEntity(String entityType, Long entityId) {
        return activityLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
    
    public List<ActivityLog> getActivityLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    public List<ActivityLog> getActivityLogsByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByUserAndCreatedAtBetween(user, startDate, endDate);
    }
    
    public Page<ActivityLog> getAllActivityLogs(Pageable pageable) {
        return activityLogRepository.findAllOrderByCreatedAtDesc(pageable);
    }
    
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll();
    }
}
