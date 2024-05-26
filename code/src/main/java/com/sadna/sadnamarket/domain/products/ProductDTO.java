package com.sadna.sadnamarket.domain.products;

public class ProductDTO {
    private int productID;
    private String productName;
    private double productPrice;
    private String productCategory;
    private boolean isActive = true;

    public ProductDTO(int productID, String productName, double productPrice, String productCategory) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
    }

    public ProductDTO(){
    }

    public int getProductID() {
        return productID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public boolean isActiveProduct() {
        return isActive;
    }

    public String getProductName() {
        // dana added this proxy function for the get store order history use case
        return this.productName;
    }

    public double getProductPrice() {
        // dana added this proxy function for the get store order history use case
        return this.productPrice;
    }

    public void setActiveProduct(boolean active) {
        isActive = active;
    }

}
