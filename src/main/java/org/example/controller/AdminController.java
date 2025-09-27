package org.example.controller;

import org.example.dto.ActivityLogDTO;
import org.example.dto.OrderDTO;
import org.example.dto.ShopDTO;
import org.example.dto.UserDTO;
import org.example.model.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShopService shopService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    // Dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        long totalUsers = userService.getAllUsers().size();
        long totalShops = userService.countByRole(UserRole.SHOP);
        long pendingShops = shopService.getPendingApprovalShops().size();
        
        return ResponseEntity.ok(Map.of(
            "totalUsers", totalUsers,
            "totalShops", totalShops,
            "pendingShops", pendingShops
        ));
    }
    
    // Shop management
    @GetMapping("/shops")
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        List<Shop> shops = shopService.getAllShops();
        List<ShopDTO> shopDTOs = shops.stream()
                .map(this::convertToShopDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(shopDTOs);
    }
    
    @GetMapping("/shops/pending")
    public ResponseEntity<List<ShopDTO>> getPendingShops() {
        List<Shop> shops = shopService.getPendingApprovalShops();
        List<ShopDTO> shopDTOs = shops.stream()
                .map(this::convertToShopDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(shopDTOs);
    }
    
    @PostMapping("/shops/{shopId}/approve")
    public ResponseEntity<ShopDTO> approveShop(@PathVariable Long shopId) {
        Shop approvedShop = shopService.approveShop(shopId);
        return ResponseEntity.ok(convertToShopDTO(approvedShop));
    }
    
    @PostMapping("/shops/{shopId}/reject")
    public ResponseEntity<ShopDTO> rejectShop(@PathVariable Long shopId) {
        Shop rejectedShop = shopService.rejectShop(shopId);
        return ResponseEntity.ok(convertToShopDTO(rejectedShop));
    }
    
    @PutMapping("/shops/{shopId}")
    public ResponseEntity<ShopDTO> updateShop(@PathVariable Long shopId, @RequestBody Shop shop) {
        shop.setId(shopId);
        Shop updatedShop = shopService.updateShop(shop);
        return ResponseEntity.ok(convertToShopDTO(updatedShop));
    }
    
    
    // Activity logs
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLogDTO>> getActivityLogs() {
        List<ActivityLog> activityLogs = activityLogService.getAllActivityLogs();
        List<ActivityLogDTO> activityLogDTOs = activityLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityLogDTOs);
    }
    
    @GetMapping("/activity-logs/user/{userId}")
    public ResponseEntity<List<ActivityLogDTO>> getActivityLogsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<ActivityLog> activityLogs = activityLogService.getActivityLogsByUser(user);
        List<ActivityLogDTO> activityLogDTOs = activityLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityLogDTOs);
    }
    
    // Orders management
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);
        return ResponseEntity.ok(convertToOrderDTO(order));
    }
    
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }
    
    // Customers management
    @GetMapping("/customers")
    public ResponseEntity<List<UserDTO>> getAllCustomers() {
        List<User> customers = userService.findByRole(UserRole.CUSTOMER);
        List<UserDTO> customerDTOs = customers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }
    
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<UserDTO> getCustomerById(@PathVariable Long customerId) {
        User customer = userService.findById(customerId);
        if (customer.getRole() != UserRole.CUSTOMER) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToUserDTO(customer));
    }
    
    // Helper method to convert ActivityLog to ActivityLogDTO
    private ActivityLogDTO convertToDTO(ActivityLog activityLog) {
        ActivityLogDTO dto = new ActivityLogDTO();
        dto.setId(activityLog.getId());
        dto.setAction(activityLog.getAction());
        dto.setDescription(activityLog.getDescription());
        dto.setEntityType(activityLog.getEntityType());
        dto.setEntityId(activityLog.getEntityId());
        dto.setIpAddress(activityLog.getIpAddress());
        dto.setUserAgent(activityLog.getUserAgent());
        dto.setCreatedAt(activityLog.getCreatedAt());
        
        // Handle user information safely
        if (activityLog.getUser() != null) {
            dto.setUserName(activityLog.getUser().getName());
            dto.setUserEmail(activityLog.getUser().getEmail());
        }
        
        return dto;
    }
    
    // Helper method to convert Shop to ShopDTO
    private ShopDTO convertToShopDTO(Shop shop) {
        ShopDTO dto = new ShopDTO();
        dto.setId(shop.getId());
        dto.setShopName(shop.getShopName());
        dto.setDescription(shop.getDescription());
        dto.setAddress(shop.getAddress());
        dto.setCity(shop.getCity());
        dto.setPostalCode(shop.getPostalCode());
        dto.setIsApproved(shop.getIsApproved());
        dto.setRating(shop.getRating());
        dto.setTotalOrders(shop.getTotalOrders());
        dto.setCreatedAt(shop.getCreatedAt());
        dto.setUpdatedAt(shop.getUpdatedAt());
        
        // Shop extends User, so we can access user properties directly
        dto.setOwnerName(shop.getName());
        dto.setOwnerEmail(shop.getEmail());
        
        return dto;
    }
    
    // Helper method to convert User to UserDTO
    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setIsActive(user.getIsActive());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }
    
    // Helper method to convert Order to OrderDTO
    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setDeliveryInstructions(order.getDeliveryInstructions());
        dto.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        dto.setActualDeliveryTime(order.getActualDeliveryTime());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // Handle customer information safely
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getName());
            dto.setCustomerEmail(order.getCustomer().getEmail());
        }
        
        // Handle shop information safely
        if (order.getShop() != null) {
            dto.setShopId(order.getShop().getId());
            dto.setShopName(order.getShop().getShopName());
            dto.setShopAddress(order.getShop().getAddress());
        }
        
        return dto;
    }
    
    // User management
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable UserRole role) {
        List<User> users = userService.findByRole(role);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    
    @PostMapping("/users/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
