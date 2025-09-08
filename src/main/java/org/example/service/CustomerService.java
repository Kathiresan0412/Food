package org.example.service;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
        }
        
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setIsActive(true); // Customers are active immediately
        Customer savedCustomer = customerRepository.save(customer);
        
        activityLogService.logActivity("CUSTOMER_REGISTERED", "Customer registered: " + customer.getName(), savedCustomer);
        
        return savedCustomer;
    }
    
    public Customer updateCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer.getId()));
        
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setPostalCode(customer.getPostalCode());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());
        existingCustomer.setPreferredPaymentMethod(customer.getPreferredPaymentMethod());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        
        activityLogService.logActivity("CUSTOMER_UPDATED", "Customer updated: " + customer.getName(), updatedCustomer);
        
        return updatedCustomer;
    }
    
    public void deactivateCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        customer.setIsActive(false);
        customerRepository.save(customer);
        
        activityLogService.logActivity("CUSTOMER_DEACTIVATED", "Customer deactivated: " + customer.getName(), customer);
    }
    
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
    
    public List<Customer> getActiveCustomers() {
        return customerRepository.findByIsActive(true);
    }
    
    public List<Customer> getCustomersByCity(String city) {
        return customerRepository.findByCityAndActive(city);
    }
    
    public List<Customer> getTopCustomersBySpending(BigDecimal minAmount) {
        return customerRepository.findTopCustomersBySpending(minAmount);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public void updateCustomerStats(Long customerId, Double orderAmount) {
        Customer customer = findById(customerId);
        customer.setTotalOrders(customer.getTotalOrders() + 1);
        BigDecimal updatedTotal = customer.getTotalSpent().add(BigDecimal.valueOf(orderAmount));
        customer.setTotalSpent(updatedTotal);
        customerRepository.save(customer);
    }
}
