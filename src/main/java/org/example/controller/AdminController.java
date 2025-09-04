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
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    // Dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        long totalUsers = userService.getAllUsers().size();
        long totalShops = userService.countByRole(UserRole.SHOP);
        long totalCustomers = userService.countByRole(UserRole.CUSTOMER);
        long pendingShops = shopService.getPendingApprovalShops().size();
        
        return ResponseEntity.ok(Map.of(
            "totalUsers", totalUsers,
            "totalShops", totalShops,
            "totalCustomers", totalCustomers,
            "pendingShops", pendingShops
        ));
    }
    
    // Shop management
    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }
    
    @GetMapping("/shops/pending")
    public ResponseEntity<List<Shop>> getPendingShops() {
        return ResponseEntity.ok(shopService.getPendingApprovalShops());
    }
    
    @PostMapping("/shops/{shopId}/approve")
    public ResponseEntity<Shop> approveShop(@PathVariable Long shopId) {
        Shop approvedShop = shopService.approveShop(shopId);
        return ResponseEntity.ok(approvedShop);
    }
    
    @PostMapping("/shops/{shopId}/reject")
    public ResponseEntity<Shop> rejectShop(@PathVariable Long shopId) {
        Shop rejectedShop = shopService.rejectShop(shopId);
        return ResponseEntity.ok(rejectedShop);
    }
    
    @PutMapping("/shops/{shopId}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long shopId, @RequestBody Shop shop) {
        shop.setId(shopId);
        Shop updatedShop = shopService.updateShop(shop);
        return ResponseEntity.ok(updatedShop);
    }
    
    // Customer management
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    
    @GetMapping("/customers/active")
    public ResponseEntity<List<Customer>> getActiveCustomers() {
        return ResponseEntity.ok(customerService.getActiveCustomers());
    }
    
    @PostMapping("/customers/{customerId}/deactivate")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long customerId) {
        customerService.deactivateCustomer(customerId);
        return ResponseEntity.ok().build();
    }
    
    // Order management
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }
    
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, OrderStatus> request) {
        OrderStatus newStatus = request.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
    
    // Activity logs
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs() {
        return ResponseEntity.ok(activityLogService.getAllActivityLogs());
    }
    
    @GetMapping("/activity-logs/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(activityLogService.getActivityLogsByUser(user));
    }
    
    // User management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }
    
    @PostMapping("/users/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
