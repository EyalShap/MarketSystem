package com.sadna.sadnamarket.api;

import org.springframework.web.bind.annotation.RequestParam;

public class ProductRequest {
    String productName;
    double minProductPrice;
    double maxProductPrice;
    String productCategory;
    double minProductRank;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMinProductPrice() {
        return minProductPrice;
    }

    public void setMinProductPrice(double minProductPrice) {
        this.minProductPrice = minProductPrice;
    }

    public double getMaxProductPrice() {
        return maxProductPrice;
    }

    public void setMaxProductPrice(double maxProductPrice) {
        this.maxProductPrice = maxProductPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getMinProductRank() {
        return minProductRank;
    }

    public void setMinProductRank(double minProductRank) {
        this.minProductRank = minProductRank;
    }
}
