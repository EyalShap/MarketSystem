package com.sadna.sadnamarket.domain.users;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class IUser {

    protected Cart cart;
    private static final Logger logger = LogManager.getLogger(IUser.class);
    public IUser(){
        cart=new Cart();
    }
    
    // Abstract method (does not have a body)
    public abstract boolean isLoggedIn();
    
    // Regular method
    public void addProductToCart(int storeId,int productId, int amount) {
        logger.info("add amount of: {} of product id:{} of storeId: {}",amount,productId,storeId);
        cart.addProduct(storeId,productId, amount);
        logger.info("finish add amount of: {} of product id:{} of storeId",amount,productId,storeId);
    }
    public void removeProductFromCart(int storeId,int productId) {
        logger.info("remove product id: {} of storeId: {}",productId,storeId);
        cart.removeProduct(storeId, productId);
        logger.info("done remove product id: {} of storeId: {}",productId,storeId);

    }
    public void changeQuantityCart(int storeId,int productId, int amount) {
        logger.info("change amount of: {} of product id:{} of storeId: {}",amount,productId,storeId);
        cart.changeQuantity(storeId,productId, amount);
        logger.info("done change amount of: {} of product id:{} of storeId: {}",amount,productId,storeId);

    }
    public Cart getCart(){
        logger.info("get cart: {}",cart);
        return this.cart;
    }
    public List<CartItemDTO> getCartItems() {
        logger.info("Entering get Cart Items");
        List<CartItemDTO> cartItems=this.cart.getCartItems();
        logger.info("exiting get Cart Items: {}",cartItems);
        return cartItems;
    }

    
    
}