package com.sadna.sadnamarket.domain.products;

public class ProductDTO {
    private int productId;
    private String productName;
    private int productPrice;

    public ProductDTO() {

    }

    public int getProductId() {
        // dana added this proxy function for the get store products use case
        return 0;
    }

    public String getProductName() {
        // dana added this proxy function for the get store order history use case
        return "";
    }

    public int getProductPrice() {
        // dana added this proxy function for the get store order history use case
        return 0;
    }
}
