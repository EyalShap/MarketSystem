package com.sadna.sadnamarket.domain.users;


abstract class IUser {

    protected Cart cart;

    
    // Abstract method (does not have a body)
    public abstract boolean isLoggedIn();
    
    // Regular method
    public void addToCart(int productId, int amount) {
        cart.addProduct(productId, amount);
    }
    public Cart getCart(){
        return this.cart;
    }
    
    
}