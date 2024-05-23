package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;

public class Cart {

    private HashMap<Integer,Basket> baskets;
    
    public Cart(){
        baskets=new HashMap<>();
    }
    public void addProduct(int storeId,int productId,int amount){
        if(hasStore(storeId)){
            baskets.get(storeId).addProduct(productId, amount);;
        }
        else{
            Basket basket=new Basket(storeId);
            basket.addProduct(productId, amount);
            baskets.put(storeId, basket);
        }
    }
    public void removeProduct(int storeId,int productId){
        if(!hasStore(storeId)){
            throw new IllegalArgumentException("product doesnt exist in cart");
        }
        baskets.get(storeId).removeProduct(productId);
    }
    private boolean hasStore(int storeId){
        return baskets.containsKey(storeId);
    }

    public boolean isEmpty(){
        return baskets.isEmpty();
    }

    public void changeQuantity(int storeId,int productId ,int quntity){
        if(!hasStore(storeId)){
            throw new IllegalArgumentException("product doesnt exist in cart");
        }
        baskets.get(storeId).changeQuantity(productId,quntity);
    }

    public void purchase(){
        for(Basket basket: baskets.values()){
            basket.purchase();
        }
    }
}
