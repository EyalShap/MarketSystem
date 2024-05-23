package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;

public class Basket {
    private int storeId;
    private HashMap<Integer,Integer> products;
    public Basket(int storeId){
        this.storeId=storeId;
        products=new HashMap<>();
    }
    public int getStoreId(){
        return storeId;
    }
    public void addProduct(int productId,int amount){
        if(hasProduct(productId))
            throw new IllegalArgumentException("user already exits in cart");
        products.put(productId, amount);
    }
    public void removeProduct(int productId){
        if(!hasProduct(productId))
            throw new IllegalArgumentException("user doesnt exits in cart");
        products.remove(productId);
    }
    private boolean hasProduct(int productId){
        return products.containsKey(productId);
    }
    public void changeQuantity(int productId,int quantity) {
        if(!hasProduct(productId))
            throw new IllegalArgumentException("user doesnt exits in cart");
        products.replace(productId,quantity);
    }

    public void purchase(){
        /*TODO: 1.update new amount of product in the store
                2.make order object to memorize for the member and for the store
        */
    }


}
