package com.sadna.sadnamarket.domain.products;

import java.util.Objects;

public class ProductDTO {
    private int productID;
    private String productName;
    private double productPrice;
    private String productCategory;
    private double productRank;
    private boolean isActive;

    public ProductDTO(int productID, String productName, double productPrice, String productCategory,
            double productRank, boolean isActive) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productRank = productRank;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return productID == that.productID && Double.compare(that.productPrice, productPrice) == 0 && Double.compare(that.productRank, productRank) == 0 && isActive == that.isActive && Objects.equals(productName, that.productName) && Objects.equals(productCategory, that.productCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, productName, productPrice, productCategory, productRank, isActive);
    }
}
