package org.example.repository;

import org.example.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    
    Optional<Shop> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Shop> findByIsApproved(Boolean isApproved);
    
    List<Shop> findByIsActive(Boolean isActive);
    
    @Query("SELECT s FROM Shop s WHERE s.isApproved = :isApproved AND s.isActive = :isActive")
    List<Shop> findByIsApprovedAndIsActive(@Param("isApproved") Boolean isApproved, @Param("isActive") Boolean isActive);
    
    @Query("SELECT s FROM Shop s WHERE s.city = :city AND s.isApproved = true AND s.isActive = true")
    List<Shop> findByCityAndApproved(@Param("city") String city);
    
    @Query("SELECT s FROM Shop s WHERE s.shopName LIKE %:name% AND s.isApproved = true AND s.isActive = true")
    List<Shop> findByShopNameContainingAndApproved(@Param("name") String name);
}
