package com.sadna.sadnamarket.domain.discountPolicies;

public class ProductDataPrice {
    int id;
    int storeId;
    String name;
    int amount;
    double oldPrice;
    double newPrice;

    public ProductDataPrice(int id,int storeId, String name, int amount, double oldPrice, double newPrice) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for amount
    public int getAmount() {
        return amount;
    }

    // Getter for oldPrice
    public double getOldPrice() {
        return oldPrice;
    }

    // Getter for newPrice
    public double getNewPrice() {
        return newPrice;
    }
    public int getStoreId() {
        return storeId;
    }

    public void setNewPrice(double newPrice) {
        if(newPrice < 0){
            this.newPrice = 0;
        }
        else{
            this.newPrice = newPrice;
        }
    }

    public ProductDataPrice deepCopy(){
        return new ProductDataPrice(this.id,this.storeId, this.name, this.amount,this.oldPrice,this.newPrice);
    }
}
