package com.sadna.sadnamarket.domain.users;


abstract class IUser {

    protected Cart cart;

    
    // Abstract method (does not have a body)
    public abstract boolean isLoggedIn();
    
    // Regular method
    public void addToCart(Product product) {
        
    }

    
}