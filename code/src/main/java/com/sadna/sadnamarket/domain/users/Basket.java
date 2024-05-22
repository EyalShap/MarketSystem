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
    public void changeQuantity(int productId,int quantity) {
        products.put(productId,quantity);
    }

    public void purchase(){
        /*TODO: 1.update new amount of product in the store
                2.make order object to memorize for the member and for the store
        */
    }


}
