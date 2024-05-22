package com.sadna.sadnamarket.domain.users;


abstract class IUser {

    protected Cart cart;

    
    // Abstract method (does not have a body)
    public abstract boolean isLoggedIn();
    
    // Regular method
    public void addProductToCart(int storeId,int productId, int amount) {
        cart.addProduct(storeId,productId, amount);
    }
    public void removeProductFromCart(int storeId,int productId) {
        cart.removeProduct(storeId, productId);;
    }
    public void changeQuantityCart(int storeId,int productId, int amount) {
        cart.changeQuantity(storeId,productId, amount);
    }
    public Cart getCart(){
        return this.cart;
    }
    
    
}