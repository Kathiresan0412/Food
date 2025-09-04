package org.example.controller;

import org.example.model.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private FoodService foodService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    // Get current customer info
    @GetMapping("/profile")
    public ResponseEntity<Customer> getCustomerProfile(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(customer);
    }
    
    // Update customer profile
    @PutMapping("/profile")
    public ResponseEntity<Customer> updateCustomerProfile(@RequestBody Customer customer, Authentication authentication) {
        String email = authentication.getName();
        Customer currentCustomer = customerService.findByEmail(email).orElseThrow();
        customer.setId(currentCustomer.getId());
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    // Dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        
        long totalOrders = orderService.countOrdersByCustomer(customer);
        List<Order> recentOrders = orderService.getOrdersByCustomer(customer);
        
        return ResponseEntity.ok(Map.of(
            "totalOrders", totalOrders,
            "totalSpent", customer.getTotalSpent(),
            "recentOrdersCount", recentOrders.size()
        ));
    }
    
    // Browse shops
    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getApprovedShops() {
        return ResponseEntity.ok(shopService.getApprovedShops());
    }
    
    @GetMapping("/shops/search")
    public ResponseEntity<List<Shop>> searchShops(@RequestParam String name) {
        return ResponseEntity.ok(shopService.searchShopsByName(name));
    }
    
    @GetMapping("/shops/city/{city}")
    public ResponseEntity<List<Shop>> getShopsByCity(@PathVariable String city) {
        return ResponseEntity.ok(shopService.getShopsByCity(city));
    }
    
    // Browse foods
    @GetMapping("/foods")
    public ResponseEntity<List<Food>> getAllAvailableFoods() {
        return ResponseEntity.ok(foodService.getAllAvailableFoods());
    }
    
    @GetMapping("/foods/shop/{shopId}")
    public ResponseEntity<List<Food>> getFoodsByShop(@PathVariable Long shopId) {
        Shop shop = shopService.findById(shopId);
        return ResponseEntity.ok(foodService.getAvailableFoodsByShop(shop));
    }
    
    @GetMapping("/foods/category/{category}")
    public ResponseEntity<List<Food>> getFoodsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(foodService.getFoodsByCategory(category));
    }
    
    @GetMapping("/foods/search")
    public ResponseEntity<List<Food>> searchFoods(@RequestParam String name) {
        return ResponseEntity.ok(foodService.searchFoodsByName(name));
    }
    
    @GetMapping("/foods/price-range")
    public ResponseEntity<List<Food>> getFoodsByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return ResponseEntity.ok(foodService.getFoodsByPriceRange(minPrice, maxPrice));
    }
    
    // Order management
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getCustomerOrders(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customer));
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        Order order = orderService.findById(orderId);
        
        if (!order.getCustomer().getId().equals(customer.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        Order order = orderService.findById(orderId);
        
        if (!order.getCustomer().getId().equals(customer.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        Order cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(cancelledOrder);
    }
    
    // Activity logs
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(activityLogService.getActivityLogsByUser(customer));
    }
}
