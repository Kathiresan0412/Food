package org.example.service;

import org.example.model.Food;
import org.example.model.Shop;
import org.example.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodService {
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    public Food createFood(Food food) {
        Food savedFood = foodRepository.save(food);
        
        activityLogService.logActivity("FOOD_CREATED", "Food created: " + food.getName(), 
                food.getShop(), "Food", savedFood.getId());
        
        return savedFood;
    }
    
    public Food updateFood(Food food) {
        Food existingFood = foodRepository.findById(food.getId())
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + food.getId()));
        
        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setPrice(food.getPrice());
        existingFood.setImageUrl(food.getImageUrl());
        existingFood.setCategory(food.getCategory());
        existingFood.setIsAvailable(food.getIsAvailable());
        existingFood.setPreparationTime(food.getPreparationTime());
        
        Food updatedFood = foodRepository.save(existingFood);
        
        activityLogService.logActivity("FOOD_UPDATED", "Food updated: " + food.getName(), 
                food.getShop(), "Food", updatedFood.getId());
        
        return updatedFood;
    }
    
    public void deleteFood(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
        
        foodRepository.delete(food);
        
        activityLogService.logActivity("FOOD_DELETED", "Food deleted: " + food.getName(), 
                food.getShop(), "Food", foodId);
    }
    
    public Food findById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }
    
    public List<Food> getFoodsByShop(Shop shop) {
        return foodRepository.findByShop(shop);
    }
    
    public List<Food> getAvailableFoodsByShop(Shop shop) {
        return foodRepository.findAvailableByShop(shop);
    }
    
    public List<Food> getFoodsByCategory(String category) {
        return foodRepository.findAvailableByCategoryOrderByRating(category);
    }
    
    public List<Food> searchFoodsByName(String name) {
        return foodRepository.findByNameContainingAndAvailable(name);
    }
    
    public List<Food> getFoodsByPriceRange(Double minPrice, Double maxPrice) {
        return foodRepository.findByPriceRangeAndAvailable(minPrice, maxPrice);
    }
    
    public List<Food> getAllAvailableFoods() {
        return foodRepository.findAllAvailableFromApprovedShops();
    }
    
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    
    public void updateFoodRating(Long foodId, Double newRating) {
        Food food = findById(foodId);
        food.setRating(BigDecimal.valueOf(newRating));
        foodRepository.save(food);
    }
    
    public void incrementFoodOrders(Long foodId) {
        Food food = findById(foodId);
        food.setTotalOrders(food.getTotalOrders() + 1);
        foodRepository.save(food);
    }
    
    public void toggleFoodAvailability(Long foodId) {
        Food food = findById(foodId);
        food.setIsAvailable(!food.getIsAvailable());
        foodRepository.save(food);
        
        activityLogService.logActivity("FOOD_AVAILABILITY_TOGGLED", 
                "Food availability toggled: " + food.getName() + " - " + (food.getIsAvailable() ? "Available" : "Unavailable"), 
                food.getShop(), "Food", foodId);
    }
}
