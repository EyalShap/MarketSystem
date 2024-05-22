package com.sadna.sadnamarket.domain.products;

public class ProductDTO {
    final private int productId;
    private String productName;
    private int productPrice;
    private String productCategory;
    private boolean isActive = true;

    public ProductDTO(int productId, String productName, int productPrice, String productCategory) {
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


    public int getProductPrice() {
        return this.productPrice;
    }


    public String getProductCategory() {
        return this.productCategory;
    }


    public boolean isActiveProduct() {
        return this.isActive;
    }


}
