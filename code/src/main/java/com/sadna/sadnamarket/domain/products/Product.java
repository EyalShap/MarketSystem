package com.sadna.sadnamarket.domain.products;

public class Product {
    private String productName;
    private int productQuantity ;
    private int productPrice;
    public Product(String productName,int productQuantity,int productPrice){
        this.productName=productName;
        this.productQuantity=productQuantity;
        this.productPrice=productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductAmount() {
        return productQuantity;
    }

    public void setProductAmount(int productAmount) {
        this.productQuantity = productAmount;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
