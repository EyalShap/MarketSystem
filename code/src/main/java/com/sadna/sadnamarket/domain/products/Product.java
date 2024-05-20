package com.sadna.sadnamarket.domain.products;

import java.text.MessageFormat;

public class Product {
    final private int productID;
    private String productName;
    private int productPrice;
    private String productCategory;
    private boolean isActive = true;

    public Product(int productID, String productName, int productPrice, String productCategory) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public boolean isActiveProduct() {
        return this.isActive;
    }

    public void disableProduct() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "Product'{'productID={0}, productName=''{1}'', productPrice={2}, productCategory=''{3}'', isActive={4}}'",
                productID,
                productName, productPrice, productCategory, isActive);
    }
}
