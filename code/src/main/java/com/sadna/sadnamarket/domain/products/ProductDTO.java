package com.sadna.sadnamarket.domain.products;

public class ProductDTO {
    private int productID;
    private String productName;
    private double productPrice;
    private String productCategory;
    private double productRank;
    private boolean isActive = true;
    private double productWeight;

    public ProductDTO(int productID, String productName, double productPrice, String productCategory,
            double productRank, double productWeight) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productRank = productRank;
        this.productWeight = productWeight;
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

    public double getProductWeight() { return this.productWeight; }

    public void setActiveProduct(boolean active) {
        isActive = active;
    }

}
