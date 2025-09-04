package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }
    
    public Admin(String email, String password, String name) {
        super(email, password, name, UserRole.ADMIN);
    }
}
