package com.sadna.sadnamarket.domain.products;

import java.text.MessageFormat;

public class Product {
    final private int productId;
    private String productName;
    private int productPrice;
    private String productCategory;
    private boolean isActive = true;

    public Product(int productId, String productName, int productPrice, String productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
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

    public int getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return this.productCategory;
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

    public ProductDTO getProductDTO() {
        return ProductMapper.toProductDTO(this);
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "Product'{'productId={0}, productName=''{1}'', productPrice={2}, productCategory=''{3}'', isActive={4}'}'",
                productId,
                productName, productPrice, productCategory, isActive);
    }
}
