package com.sadna.sadnamarket.domain.products;

public class ProductDTO {
    private int productID;
    private String productName;
    private double productPrice;
    private String productCategory;
    private double productRank;
    private boolean isActive = true;

    public ProductDTO(int productID, String productName, double productPrice, String productCategory,
            double productRank) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productRank = productRank;
    }

    public ProductDTO() {
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return this.productName;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public double getProductRank() {
        return this.productRank;
    }

    public boolean isActiveProduct() {
        return isActive;
    }

    public void setActiveProduct(boolean active) {
        isActive = active;
    }

}
