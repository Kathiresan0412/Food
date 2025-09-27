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
        List<Food> foods = foodService.getFoodsByShop(shop);
        
        // Calculate today's revenue
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        BigDecimal todayRevenue = orderService.calculateTotalRevenueByDateRange(shop, startOfDay, endOfDay);
        
        return ResponseEntity.ok(Map.of(
            "totalOrders", totalOrders,
            "totalRevenue", totalRevenue,
            "todayRevenue", todayRevenue,
            "totalFoods", foods.size(),
            "averageRating", shop.getRating() != null ? shop.getRating() : BigDecimal.ZERO,
            "recentOrdersCount", recentOrders.size(),
            "shopRating", shop.getRating() != null ? shop.getRating() : BigDecimal.ZERO
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
    
    // Get recent orders
    @GetMapping("/orders/recent")
    public ResponseEntity<List<Order>> getRecentOrders(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        List<Order> orders = orderService.getOrdersByShop(shop);
        // Return only the 10 most recent orders
        return ResponseEntity.ok(orders.stream().limit(10).toList());
    }
    
    // Get order by ID
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        Order order = orderService.findById(orderId);
        
        if (!order.getShop().getId().equals(shop.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(order);
    }
    
    // Analytics endpoint
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        
        // Get top selling foods
        List<Food> foods = foodService.getFoodsByShop(shop);
        List<Map<String, Object>> topSellingItems = foods.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getTotalOrders(), f1.getTotalOrders()))
                .limit(5)
                .map(food -> {
                    Map<String, Object> item = new java.util.HashMap<>();
                    item.put("id", food.getId());
                    item.put("name", food.getName());
                    item.put("category", food.getCategory() != null ? food.getCategory() : "N/A");
                    item.put("totalOrders", food.getTotalOrders());
                    item.put("totalRevenue", food.getPrice().multiply(BigDecimal.valueOf(food.getTotalOrders())));
                    return item;
                })
                .toList();
        
        // Generate revenue data for the last 7 days
        List<String> labels = List.of("6 days ago", "5 days ago", "4 days ago", "3 days ago", "2 days ago", "Yesterday", "Today");
        List<BigDecimal> values = List.of(
            BigDecimal.valueOf(150), BigDecimal.valueOf(200), BigDecimal.valueOf(180),
            BigDecimal.valueOf(250), BigDecimal.valueOf(300), BigDecimal.valueOf(220), BigDecimal.valueOf(280)
        );
        
        Map<String, Object> revenueData = Map.of(
            "labels", labels,
            "values", values
        );
        
        return ResponseEntity.ok(Map.of(
            "topSellingItems", topSellingItems,
            "revenueData", revenueData
        ));
    }
    
    // Get customers who ordered from this shop
    @GetMapping("/customers")
    public ResponseEntity<List<Map<String, Object>>> getShopCustomers(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        
        List<Order> orders = orderService.getOrdersByShop(shop);
        Map<Long, Map<String, Object>> customerMap = new java.util.HashMap<>();
        
        for (Order order : orders) {
            Customer customer = order.getCustomer();
            Long customerId = customer.getId();
            
            if (!customerMap.containsKey(customerId)) {
                customerMap.put(customerId, new java.util.HashMap<>());
                Map<String, Object> customerData = customerMap.get(customerId);
                customerData.put("id", customer.getId());
                customerData.put("name", customer.getName());
                customerData.put("email", customer.getEmail());
                customerData.put("phoneNumber", customer.getPhoneNumber());
                customerData.put("city", customer.getCity());
                customerData.put("totalOrders", 0);
                customerData.put("totalSpent", BigDecimal.ZERO);
                customerData.put("lastOrderDate", null);
            }
            
            Map<String, Object> customerData = customerMap.get(customerId);
            customerData.put("totalOrders", (Integer) customerData.get("totalOrders") + 1);
            customerData.put("totalSpent", ((BigDecimal) customerData.get("totalSpent")).add(order.getTotalAmount()));
            
            if (customerData.get("lastOrderDate") == null || 
                order.getCreatedAt().isAfter((LocalDateTime) customerData.get("lastOrderDate"))) {
                customerData.put("lastOrderDate", order.getCreatedAt());
            }
        }
        
        return ResponseEntity.ok(new java.util.ArrayList<>(customerMap.values()));
    }
    
    // Get reviews for this shop
    @GetMapping("/reviews")
    public ResponseEntity<List<Map<String, Object>>> getShopReviews(Authentication authentication) {
        String email = authentication.getName();
        shopService.findByEmail(email).orElseThrow();
        
        // For now, return mock reviews since we don't have a review system implemented
        List<Map<String, Object>> reviews = List.of(
            Map.of(
                "id", 1L,
                "rating", 5,
                "comment", "Great food and fast delivery!",
                "customer", Map.of("name", "John Doe"),
                "createdAt", LocalDateTime.now().minusDays(2)
            ),
            Map.of(
                "id", 2L,
                "rating", 4,
                "comment", "Good quality food, would order again.",
                "customer", Map.of("name", "Jane Smith"),
                "createdAt", LocalDateTime.now().minusDays(5)
            ),
            Map.of(
                "id", 3L,
                "rating", 5,
                "comment", "Excellent service and delicious food!",
                "customer", Map.of("name", "Mike Johnson"),
                "createdAt", LocalDateTime.now().minusDays(7)
            )
        );
        
        return ResponseEntity.ok(reviews);
    }
    
    // Activity logs
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(activityLogService.getActivityLogsByUser(shop));
    }
}
