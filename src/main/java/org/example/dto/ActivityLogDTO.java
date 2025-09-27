package org.example.dto;

import java.time.LocalDateTime;

public class ActivityLogDTO {
    private Long id;
    private String action;
    private String description;
    private String entityType;
    private Long entityId;
    private String ipAddress;
    private String userAgent;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;

    // Constructors
    public ActivityLogDTO() {}

    public ActivityLogDTO(Long id, String action, String description, String entityType, 
                         Long entityId, String ipAddress, String userAgent, 
                         String userName, String userEmail, LocalDateTime createdAt) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.entityType = entityType;
        this.entityId = entityId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.userName = userName;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
