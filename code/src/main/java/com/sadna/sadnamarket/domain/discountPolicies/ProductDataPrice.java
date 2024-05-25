package com.sadna.sadnamarket.domain.discountPolicies;

public class ProductDataPrice {
    int id;
    int amount;
    int oldPrice;
    int newPrice;

    public ProductDataPrice(int id, int amount, int oldPrice, int newPrice) {
        this.id = id;
        this.amount = amount;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;

    }
}
