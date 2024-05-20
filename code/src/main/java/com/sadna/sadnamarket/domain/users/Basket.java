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

    }
    public boolean hasProduct(int productId){
        return products.containsKey(productId);
    }
}
