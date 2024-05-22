package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;

public class Cart {

    private HashMap<Integer,Basket> baskets;
    
    public Cart(){
        baskets=new HashMap<>();
    }
    public void addProduct(int productId,int amount){

    }
    public void removeProduct(int productId){

    }
    public boolean hasProduct(int productId){
        return baskets.containsKey(productId);
    }

    public boolean isEmpty(){
        return baskets.isEmpty();
    }

    public void changeQuantity(Basket basket,int productId ,int quntity){
        basket.changeQuantity(productId,quntity);
    }

    public void purchase(){
        for(Basket basket: baskets.values()){
            basket.purchase();
        }
    }
}
