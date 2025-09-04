package org.example.service;

import org.example.model.*;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ShopService shopService;
    
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        
        activityLogService.logActivity("ORDER_CREATED", "Order created: " + savedOrder.getOrderNumber(), 
                savedOrder.getCustomer(), "Order", savedOrder.getId());
        
        return savedOrder;
    }
    
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        
        // Update estimated delivery time based on status
        if (newStatus == OrderStatus.CONFIRMED) {
            order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30)); // Default 30 minutes
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setActualDeliveryTime(LocalDateTime.now());
        }
        
        Order updatedOrder = orderRepository.save(order);
        
        activityLogService.logActivity("ORDER_STATUS_UPDATED", 
                "Order status changed from " + oldStatus + " to " + newStatus + " for order: " + order.getOrderNumber(), 
                updatedOrder.getCustomer(), "Order", updatedOrder.getId());
        
        return updatedOrder;
    }
    
    public Order updatePaymentStatus(Long orderId, PaymentStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        PaymentStatus oldStatus = order.getPaymentStatus();
        order.setPaymentStatus(newStatus);
        
        Order updatedOrder = orderRepository.save(order);
        
        activityLogService.logActivity("PAYMENT_STATUS_UPDATED", 
                "Payment status changed from " + oldStatus + " to " + newStatus + " for order: " + order.getOrderNumber(), 
                updatedOrder.getCustomer(), "Order", updatedOrder.getId());
        
        return updatedOrder;
    }
    
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        
        activityLogService.logActivity("ORDER_CANCELLED", "Order cancelled: " + order.getOrderNumber(), 
                cancelledOrder.getCustomer(), "Order", cancelledOrder.getId());
        
        return cancelledOrder;
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }
    
    public List<Order> getOrdersByShop(Shop shop) {
        return orderRepository.findByShopOrderByCreatedAtDesc(shop);
    }
    
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> getOrdersByStatusAndShop(OrderStatus status, Shop shop) {
        return orderRepository.findByStatusAndShop(status, shop);
    }
    
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    public List<Order> getOrdersByShopAndDateRange(Shop shop, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByShopAndCreatedAtBetween(shop, startDate, endDate);
    }
    
    public List<Order> getOrdersByCustomerAndDateRange(Customer customer, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerAndCreatedAtBetween(customer, startDate, endDate);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public long countOrdersByShop(Shop shop) {
        return orderRepository.countByShop(shop);
    }
    
    public long countOrdersByCustomer(Customer customer) {
        return orderRepository.countByCustomer(customer);
    }
    
    public BigDecimal calculateTotalRevenue(Shop shop) {
        List<Order> orders = orderRepository.findByShop(shop);
        return orders.stream()
                .filter(order -> order.getPaymentStatus() == PaymentStatus.PAID)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calculateTotalRevenueByDateRange(Shop shop, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByShopAndCreatedAtBetween(shop, startDate, endDate);
        return orders.stream()
                .filter(order -> order.getPaymentStatus() == PaymentStatus.PAID)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
