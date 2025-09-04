package org.example.controller;

import org.example.model.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private FoodService foodService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    // Get current shop info
    @GetMapping("/profile")
    public ResponseEntity<Shop> getShopProfile(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(shop);
    }
    
    // Update shop profile
    @PutMapping("/profile")
    public ResponseEntity<Shop> updateShopProfile(@RequestBody Shop shop, Authentication authentication) {
        String email = authentication.getName();
        Shop currentShop = shopService.findByEmail(email).orElseThrow();
        shop.setId(currentShop.getId());
        Shop updatedShop = shopService.updateShop(shop);
        return ResponseEntity.ok(updatedShop);
    }
    
    // Dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        
        long totalOrders = orderService.countOrdersByShop(shop);
        BigDecimal totalRevenue = orderService.calculateTotalRevenue(shop);
        List<Order> recentOrders = orderService.getOrdersByShop(shop);
        
        return ResponseEntity.ok(Map.of(
            "totalOrders", totalOrders,
            "totalRevenue", totalRevenue,
            "recentOrdersCount", recentOrders.size(),
            "shopRating", shop.getRating()
        ));
    }
    
    // Food management
    @GetMapping("/foods")
    public ResponseEntity<List<Food>> getShopFoods(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(foodService.getFoodsByShop(shop));
    }
    
    @PostMapping("/foods")
    public ResponseEntity<Food> createFood(@RequestBody Food food, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        food.setShop(shop);
        Food createdFood = foodService.createFood(food);
        return ResponseEntity.ok(createdFood);
    }
    
    @PutMapping("/foods/{foodId}")
    public ResponseEntity<Food> updateFood(@PathVariable Long foodId, @RequestBody Food food, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        Food existingFood = foodService.findById(foodId);
        
        if (!existingFood.getShop().getId().equals(shop.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        food.setId(foodId);
        food.setShop(shop);
        Food updatedFood = foodService.updateFood(food);
        return ResponseEntity.ok(updatedFood);
    }
    
    @DeleteMapping("/foods/{foodId}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long foodId, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        Food food = foodService.findById(foodId);
        
        if (!food.getShop().getId().equals(shop.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        foodService.deleteFood(foodId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/foods/{foodId}/toggle-availability")
    public ResponseEntity<Void> toggleFoodAvailability(@PathVariable Long foodId, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        Food food = foodService.findById(foodId);
        
        if (!food.getShop().getId().equals(shop.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        foodService.toggleFoodAvailability(foodId);
        return ResponseEntity.ok().build();
    }
    
    // Order management
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getShopOrders(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(orderService.getOrdersByShop(shop));
    }
    
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(orderService.getOrdersByStatusAndShop(status, shop));
    }
    
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, OrderStatus> request, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        Order order = orderService.findById(orderId);
        
        if (!order.getShop().getId().equals(shop.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        OrderStatus newStatus = request.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
    
    // Revenue reports
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReport(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        
        BigDecimal totalRevenue = orderService.calculateTotalRevenue(shop);
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        BigDecimal monthlyRevenue = orderService.calculateTotalRevenueByDateRange(shop, startOfMonth, LocalDateTime.now());
        
        return ResponseEntity.ok(Map.of(
            "totalRevenue", totalRevenue,
            "monthlyRevenue", monthlyRevenue
        ));
    }
    
    // Activity logs
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(activityLogService.getActivityLogsByUser(shop));
    }
}
