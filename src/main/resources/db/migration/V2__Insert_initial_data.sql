-- Insert initial admin user
-- Password: admin123 (BCrypt encoded)
INSERT INTO users (email, password, name, phone_number, is_active, role, created_at, updated_at) 
VALUES ('admin@foodsystem.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'System Administrator', '+1234567890', TRUE, 'ADMIN', NOW(), NOW());

-- Insert admin record
INSERT INTO admins (id) VALUES (1);

-- Insert sample shops (pending approval)
INSERT INTO users (email, password, name, phone_number, is_active, role, created_at, updated_at) 
VALUES 
('pizza@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John Pizza', '+1111111111', TRUE, 'SHOP', NOW(), NOW()),
('burger@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane Burger', '+2222222222', TRUE, 'SHOP', NOW(), NOW()),
('sushi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Mike Sushi', '+3333333333', TRUE, 'SHOP', NOW(), NOW());

INSERT INTO shops (id, shop_name, description, address, city, postal_code, is_approved, rating, total_orders) 
VALUES 
(2, 'Pizza Palace', 'Best pizza in town with fresh ingredients', '123 Main St', 'New York', '10001', TRUE, 4.5, 150),
(3, 'Burger King', 'Delicious burgers and fries', '456 Oak Ave', 'New York', '10002', TRUE, 4.2, 200),
(4, 'Sushi Master', 'Authentic Japanese sushi', '789 Pine St', 'New York', '10003', FALSE, 0.0, 0);

-- Insert sample customers
INSERT INTO users (email, password, name, phone_number, is_active, role, created_at, updated_at) 
VALUES 
('customer1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Alice Johnson', '+4444444444', TRUE, 'CUSTOMER', NOW(), NOW()),
('customer2@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Bob Smith', '+5555555555', TRUE, 'CUSTOMER', NOW(), NOW()),
('customer3@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Carol Davis', '+6666666666', TRUE, 'CUSTOMER', NOW(), NOW());

INSERT INTO customers (id, address, city, postal_code, date_of_birth, preferred_payment_method, total_orders, total_spent) 
VALUES 
(5, '100 Customer St', 'New York', '10004', '1990-01-15', 'Credit Card', 25, 450.00),
(6, '200 Customer Ave', 'New York', '10005', '1985-05-20', 'PayPal', 15, 280.00),
(7, '300 Customer Blvd', 'New York', '10006', '1992-12-10', 'Credit Card', 8, 120.00);

-- Insert sample foods
INSERT INTO foods (name, description, price, image_url, category, is_available, preparation_time, rating, total_orders, shop_id, created_at, updated_at) 
VALUES 
-- Pizza Palace foods
('Margherita Pizza', 'Classic pizza with tomato sauce, mozzarella, and basil', 12.99, '/images/margherita.jpg', 'Pizza', TRUE, 15, 4.5, 50, 2, NOW(), NOW()),
('Pepperoni Pizza', 'Pizza topped with pepperoni and mozzarella cheese', 14.99, '/images/pepperoni.jpg', 'Pizza', TRUE, 15, 4.3, 75, 2, NOW(), NOW()),
('Vegetarian Pizza', 'Pizza with bell peppers, mushrooms, onions, and olives', 13.99, '/images/vegetarian.jpg', 'Pizza', TRUE, 15, 4.4, 25, 2, NOW(), NOW()),

-- Burger King foods
('Classic Burger', 'Beef patty with lettuce, tomato, onion, and special sauce', 8.99, '/images/classic-burger.jpg', 'Burger', TRUE, 10, 4.2, 100, 3, NOW(), NOW()),
('Cheese Burger', 'Beef patty with cheese, lettuce, tomato, and pickles', 9.99, '/images/cheese-burger.jpg', 'Burger', TRUE, 10, 4.1, 80, 3, NOW(), NOW()),
('Chicken Burger', 'Grilled chicken breast with lettuce, tomato, and mayo', 7.99, '/images/chicken-burger.jpg', 'Burger', TRUE, 12, 4.0, 60, 3, NOW(), NOW()),

-- Sushi Master foods
('California Roll', 'Crab, avocado, and cucumber roll', 6.99, '/images/california-roll.jpg', 'Sushi', TRUE, 8, 4.6, 40, 4, NOW(), NOW()),
('Salmon Roll', 'Fresh salmon with rice and seaweed', 8.99, '/images/salmon-roll.jpg', 'Sushi', TRUE, 8, 4.7, 35, 4, NOW(), NOW()),
('Dragon Roll', 'Eel, cucumber, and avocado with eel sauce', 12.99, '/images/dragon-roll.jpg', 'Sushi', TRUE, 10, 4.8, 20, 4, NOW(), NOW());

-- Insert sample orders
INSERT INTO orders (order_number, total_amount, status, payment_status, delivery_address, customer_id, shop_id, created_at, updated_at) 
VALUES 
('ORD-1703123456789', 25.98, 'DELIVERED', 'PAID', '100 Customer St, New York, 10004', 5, 2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('ORD-1703123456790', 18.98, 'DELIVERED', 'PAID', '200 Customer Ave, New York, 10005', 6, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('ORD-1703123456791', 15.98, 'PREPARING', 'PAID', '300 Customer Blvd, New York, 10006', 7, 4, NOW(), NOW());

-- Insert sample order items
INSERT INTO order_items (quantity, unit_price, total_price, order_id, food_id) 
VALUES 
(1, 12.99, 12.99, 1, 1),
(1, 12.99, 12.99, 1, 2),
(1, 8.99, 8.99, 2, 4),
(1, 9.99, 9.99, 2, 5),
(1, 6.99, 6.99, 3, 7),
(1, 8.99, 8.99, 3, 8);

-- Insert sample activity logs
INSERT INTO activity_logs (action, description, entity_type, entity_id, user_id, created_at) 
VALUES 
('USER_CREATED', 'User created with email: admin@foodsystem.com', 'User', 1, 1, NOW()),
('SHOP_REGISTERED', 'Shop registered: Pizza Palace', 'Shop', 2, 2, NOW()),
('SHOP_APPROVED', 'Shop approved: Pizza Palace', 'Shop', 2, 1, NOW()),
('FOOD_CREATED', 'Food created: Margherita Pizza', 'Food', 1, 2, NOW()),
('ORDER_CREATED', 'Order created: ORD-1703123456789', 'Order', 1, 5, NOW()),
('ORDER_STATUS_UPDATED', 'Order status changed from PENDING to DELIVERED for order: ORD-1703123456789', 'Order', 1, 2, NOW());
