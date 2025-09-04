package org.example.repository;

import org.example.model.Food;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder(Order order);
    
    List<OrderItem> findByFood(Food food);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order = :order ORDER BY oi.id")
    List<OrderItem> findByOrderOrderById(@Param("order") Order order);
    
    @Query("SELECT oi.food, SUM(oi.quantity) as totalQuantity FROM OrderItem oi WHERE oi.food = :food GROUP BY oi.food")
    List<Object[]> findTotalQuantityByFood(@Param("food") Food food);
}
