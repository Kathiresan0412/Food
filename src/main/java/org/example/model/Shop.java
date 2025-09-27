package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shops")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop extends User {
    
    @NotBlank(message = "Shop name is required")
    @Column(name = "shop_name", nullable = false)
    private String shopName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "is_approved")
    private Boolean isApproved = false;
    
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(0.00);
    
    @Column(name = "total_orders")
    private Integer totalOrders = 0;
    
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Food> foods = new ArrayList<>();
    
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();
    
    public Shop() {
        super();
        setRole(UserRole.SHOP);
    }
    
    public Shop(String email, String password, String name, String shopName, String address) {
        super(email, password, name, UserRole.SHOP);
        this.shopName = shopName;
        this.address = address;
    }
    
    // Getters and Setters
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
    
    public BigDecimal getRating() {
        return rating;
    }
    
    public void setRating(BigDecimal newRating) {
        this.rating = newRating;
    }
    
    public Integer getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public List<Food> getFoods() {
        return foods;
    }
    
    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
