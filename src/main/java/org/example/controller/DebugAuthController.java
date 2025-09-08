package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user-exists")
    public Map<String, Object> userExists(@RequestParam String email) {
        Map<String, Object> resp = new HashMap<>();
        var opt = userService.findByEmail(email);
        resp.put("email", email);
        resp.put("exists", opt.isPresent());
        opt.ifPresent(user -> {
            resp.put("id", user.getId());
            resp.put("role", user.getRole());
            resp.put("active", user.getIsActive());
        });
        return resp;
    }

    @GetMapping("/check-password")
    public Map<String, Object> checkPassword(@RequestParam String email, @RequestParam String rawPassword) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("email", email);
        var opt = userService.findByEmail(email);
        if (opt.isEmpty()) {
            resp.put("exists", false);
            resp.put("passwordMatches", false);
            return resp;
        }
        User user = opt.get();
        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        resp.put("exists", true);
        resp.put("passwordMatches", matches);
        resp.put("role", user.getRole());
        resp.put("active", user.getIsActive());
        return resp;
    }
}


