package org.example.dto;

import java.time.LocalDateTime;

public class ShopDTO {
    private Long id;
    private String shopName;
    private String description;
    private String address;
    private String city;
    private String postalCode;
    private Boolean isApproved;
    private java.math.BigDecimal rating;
    private Integer totalOrders;
    private String ownerName;
    private String ownerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ShopDTO() {}

    public ShopDTO(Long id, String shopName, String description, String address, 
                   String city, String postalCode, Boolean isApproved, java.math.BigDecimal rating, 
                   Integer totalOrders, String ownerName, String ownerEmail, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.shopName = shopName;
        this.description = description;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.isApproved = isApproved;
        this.rating = rating;
        this.totalOrders = totalOrders;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public java.math.BigDecimal getRating() {
        return rating;
    }

    public void setRating(java.math.BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
