package com.sadna.sadnamarket.domain.discountPolicies;

public class ProductDataPrice {
    int id;
    int amount;
    double oldPrice;
    double newPrice;

    public ProductDataPrice(int id, int amount, double oldPrice, double newPrice) {
        this.id = id;
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

    public void setNewPrice(double newPrice) {
        if(newPrice < 0){
            this.newPrice = 0;
        }
        else{
            this.newPrice = newPrice;
        }
    }

    public ProductDataPrice deepCopy(){
        return new ProductDataPrice(this.id,this.amount,this.oldPrice,this.newPrice);
    }
}
