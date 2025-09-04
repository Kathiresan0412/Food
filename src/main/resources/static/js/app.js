// Custom JavaScript for Food Ordering System

// Global variables
let currentUser = null;
let cart = [];

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

// Initialize application
function initializeApp() {
    // Load current user info
    loadCurrentUser();
    
    // Initialize tooltips
    initializeTooltips();
    
    // Initialize form validations
    initializeFormValidations();
    
    // Load cart from localStorage
    loadCartFromStorage();
}

// Load current user information
async function loadCurrentUser() {
    try {
        const response = await fetch('/api/auth/me');
        if (response.ok) {
            currentUser = await response.json();
            updateUserInterface();
        }
    } catch (error) {
        console.error('Error loading current user:', error);
    }
}

// Update user interface based on current user
function updateUserInterface() {
    if (currentUser) {
        // Update navigation
        const userElements = document.querySelectorAll('.user-name');
        userElements.forEach(element => {
            element.textContent = currentUser.name;
        });
        
        // Show/hide elements based on user role
        const roleElements = document.querySelectorAll(`[data-role="${currentUser.role}"]`);
        roleElements.forEach(element => {
            element.style.display = 'block';
        });
    }
}

// Initialize Bootstrap tooltips
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Initialize form validations
function initializeFormValidations() {
    // Email validation
    const emailInputs = document.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('blur', validateEmail);
    });
    
    // Password confirmation
    const passwordInputs = document.querySelectorAll('input[name="password"]');
    const confirmPasswordInputs = document.querySelectorAll('input[name="confirmPassword"]');
    
    passwordInputs.forEach(input => {
        input.addEventListener('input', validatePasswordConfirmation);
    });
    
    confirmPasswordInputs.forEach(input => {
        input.addEventListener('input', validatePasswordConfirmation);
    });
}

// Validate email format
function validateEmail(event) {
    const email = event.target.value;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (email && !emailRegex.test(email)) {
        showFieldError(event.target, 'Please enter a valid email address');
    } else {
        clearFieldError(event.target);
    }
}

// Validate password confirmation
function validatePasswordConfirmation() {
    const passwordInput = document.querySelector('input[name="password"]');
    const confirmPasswordInput = document.querySelector('input[name="confirmPassword"]');
    
    if (passwordInput && confirmPasswordInput) {
        if (confirmPasswordInput.value && passwordInput.value !== confirmPasswordInput.value) {
            showFieldError(confirmPasswordInput, 'Passwords do not match');
        } else {
            clearFieldError(confirmPasswordInput);
        }
    }
}

// Show field error
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.classList.add('is-invalid');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
}

// Clear field error
function clearFieldError(field) {
    field.classList.remove('is-invalid');
    
    const errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
}

// Show success message
function showSuccessMessage(message) {
    showAlert(message, 'success');
}

// Show error message
function showErrorMessage(message) {
    showAlert(message, 'danger');
}

// Show warning message
function showWarningMessage(message) {
    showAlert(message, 'warning');
}

// Show info message
function showInfoMessage(message) {
    showAlert(message, 'info');
}

// Show alert
function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Insert at the top of the main content
    const mainContent = document.querySelector('main') || document.body;
    mainContent.insertBefore(alertDiv, mainContent.firstChild);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// Format date
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Format date only
function formatDateOnly(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Throttle function
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// Cart functions
function addToCart(foodId, quantity = 1) {
    const existingItem = cart.find(item => item.foodId === foodId);
    
    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        cart.push({
            foodId: foodId,
            quantity: quantity,
            addedAt: new Date().toISOString()
        });
    }
    
    saveCartToStorage();
    updateCartUI();
}

function removeFromCart(foodId) {
    cart = cart.filter(item => item.foodId !== foodId);
    saveCartToStorage();
    updateCartUI();
}

function updateCartQuantity(foodId, quantity) {
    const item = cart.find(item => item.foodId === foodId);
    if (item) {
        if (quantity <= 0) {
            removeFromCart(foodId);
        } else {
            item.quantity = quantity;
            saveCartToStorage();
            updateCartUI();
        }
    }
}

function clearCart() {
    cart = [];
    saveCartToStorage();
    updateCartUI();
}

function getCartTotal() {
    return cart.reduce((total, item) => {
        // This would need to be calculated with actual food prices
        return total + (item.quantity * 0); // Placeholder
    }, 0);
}

function getCartItemCount() {
    return cart.reduce((total, item) => total + item.quantity, 0);
}

function saveCartToStorage() {
    localStorage.setItem('foodOrderCart', JSON.stringify(cart));
}

function loadCartFromStorage() {
    const savedCart = localStorage.getItem('foodOrderCart');
    if (savedCart) {
        cart = JSON.parse(savedCart);
        updateCartUI();
    }
}

function updateCartUI() {
    const cartCountElements = document.querySelectorAll('.cart-count');
    const cartTotalElements = document.querySelectorAll('.cart-total');
    
    const itemCount = getCartItemCount();
    const total = getCartTotal();
    
    cartCountElements.forEach(element => {
        element.textContent = itemCount;
        element.style.display = itemCount > 0 ? 'inline' : 'none';
    });
    
    cartTotalElements.forEach(element => {
        element.textContent = formatCurrency(total);
    });
}

// API helper functions
async function apiRequest(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    
    const mergedOptions = { ...defaultOptions, ...options };
    
    try {
        const response = await fetch(url, mergedOptions);
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// Search functionality
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input');
    
    searchInputs.forEach(input => {
        const debouncedSearch = debounce(performSearch, 300);
        input.addEventListener('input', debouncedSearch);
    });
}

function performSearch(event) {
    const query = event.target.value.trim();
    const searchType = event.target.dataset.searchType;
    
    if (query.length >= 2) {
        // Implement search logic based on searchType
        console.log(`Searching for ${query} in ${searchType}`);
    }
}

// Image lazy loading
function initializeLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

// Initialize lazy loading when DOM is ready
document.addEventListener('DOMContentLoaded', initializeLazyLoading);

// Export functions for global use
window.FoodOrderApp = {
    showSuccessMessage,
    showErrorMessage,
    showWarningMessage,
    showInfoMessage,
    formatCurrency,
    formatDate,
    formatDateOnly,
    addToCart,
    removeFromCart,
    updateCartQuantity,
    clearCart,
    getCartTotal,
    getCartItemCount,
    apiRequest
};
