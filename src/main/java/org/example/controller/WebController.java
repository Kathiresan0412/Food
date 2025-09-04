package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/register/shop")
    public String registerShop() {
        return "register-shop";
    }
    
    @GetMapping("/register/customer")
    public String registerCustomer() {
        return "register-customer";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            
            if (user != null) {
                model.addAttribute("user", user);
                
                switch (user.getRole()) {
                    case ADMIN:
                        return "admin/dashboard";
                    case SHOP:
                        return "shop/dashboard";
                    case CUSTOMER:
                        return "customer/dashboard";
                    default:
                        return "login";
                }
            }
        }
        return "login";
    }
    
    @GetMapping("/admin/**")
    public String adminDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            
            if (user != null && user.getRole() == org.example.model.UserRole.ADMIN) {
                model.addAttribute("user", user);
                return "admin/dashboard";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("/shop/**")
    public String shopDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            
            if (user != null && user.getRole() == org.example.model.UserRole.SHOP) {
                model.addAttribute("user", user);
                return "shop/dashboard";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("/customer/**")
    public String customerDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            
            if (user != null && user.getRole() == org.example.model.UserRole.CUSTOMER) {
                model.addAttribute("user", user);
                return "customer/dashboard";
            }
        }
        return "redirect:/login";
    }
}
