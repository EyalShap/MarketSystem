package com.sadna.sadnamarket.domain.products;

import java.text.MessageFormat;

public class Product {
    final private int productId;
    private String productName;
    private double productPrice;
    private String productCategory;
    private double productRank;
    private boolean isActive = true;
    private double productWeight;

    public Product(int productId, String productName, double productPrice, String productCategory, double productRank, double productWeight) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productRank = productRank;
        this.productWeight = productWeight;
    }

    public int getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return this.productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getProductRank() {
        return this.productRank;
    }

    public void setProductRank(double productRank) {
        this.productRank = productRank;
    }

    public double getProductWeight() {
        return this.productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public boolean isActiveProduct() {
        return this.isActive;
    }

    public void disableProduct() {
        this.isActive = false;
    }

    public ProductDTO getProductDTO() {
        return ProductMapper.toProductDTO(this);
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "Product'{'productId={0}, productName=''{1}'', productPrice={2}, productCategory=''{3}'', productRank={4}, isActive={5}'}'",
                productId,
                productName, productPrice, productCategory, productRank, isActive);
    }


}
