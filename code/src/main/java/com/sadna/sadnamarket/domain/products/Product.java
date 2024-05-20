package com.sadna.sadnamarket.domain.products;

import java.text.MessageFormat;

public class Product {
    final private int productID;
    private String productName;
    private int productQuantity;
    private int productPrice;

    public Product(int productID, String productName, int productQuantity, int productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public int getProductID() {
        return productID;
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

    @Override
    public String toString() {
        return MessageFormat.format(
                "Product'{'productID={0}, productName=''{1}'', productQuantity={2}, productPrice={3}'}'", productID,
                productName, productQuantity, productPrice);
    }
}
