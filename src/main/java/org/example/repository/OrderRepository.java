package org.example.repository;

import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.PaymentStatus;
import org.example.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByCustomer(Customer customer);
    
    List<Order> findByShop(Shop shop);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
    
    @Query("SELECT o FROM Order o WHERE o.customer = :customer ORDER BY o.createdAt DESC")
    List<Order> findByCustomerOrderByCreatedAtDesc(@Param("customer") Customer customer);
    
    @Query("SELECT o FROM Order o WHERE o.shop = :shop ORDER BY o.createdAt DESC")
    List<Order> findByShopOrderByCreatedAtDesc(@Param("shop") Shop shop);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.shop = :shop")
    List<Order> findByStatusAndShop(@Param("status") OrderStatus status, @Param("shop") Shop shop);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.shop = :shop AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByShopAndCreatedAtBetween(@Param("shop") Shop shop, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCustomerAndCreatedAtBetween(@Param("customer") Customer customer, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.shop = :shop")
    long countByShop(@Param("shop") Shop shop);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer = :customer")
    long countByCustomer(@Param("customer") Customer customer);
}
