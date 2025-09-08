package org.example.repository;

import org.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Customer> findByIsActive(Boolean isActive);
    
    @Query("SELECT c FROM Customer c WHERE c.city = :city AND c.isActive = true")
    List<Customer> findByCityAndActive(@Param("city") String city);
    
    @Query("SELECT c FROM Customer c WHERE c.totalSpent >= :minAmount AND c.isActive = true ORDER BY c.totalSpent DESC")
    List<Customer> findTopCustomersBySpending(@Param("minAmount") BigDecimal minAmount);
}
