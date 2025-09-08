package org.example.service;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        activityLogService.logActivity("USER_CREATED", "User created with email: " + user.getEmail(), savedUser);
        
        return savedUser;
    }
    
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
        
        existingUser.setName(user.getName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setIsActive(user.getIsActive());
        
        User updatedUser = userRepository.save(existingUser);
        
        activityLogService.logActivity("USER_UPDATED", "User updated: " + user.getEmail(), updatedUser);
        
        return updatedUser;
    }
    
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        activityLogService.logActivity("USER_DEACTIVATED", "User deactivated: " + user.getEmail(), user);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> findByRoleAndIsActive(UserRole role, Boolean isActive) {
        return userRepository.findByRoleAndIsActive(role, isActive);
    }
    
    public long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByIsActive(true);
    }
}
