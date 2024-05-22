package com.sadna.sadnamarket.domain.products;

public class ProductFacade {

    // there should be all the product fields as parameters
    public int addProduct(int storeId, String productName, int productQuantity, int productPrice) {
        // dana added this proxy function for stock management use case
        // assuming it returns the product id of the new product
        //nextProductId++;
        //return nextProductId - 1;
        return 0;
    }

    public void removeProduct(int storeId, int productId) {
        // dana added this proxy function for stock management use case
    }

    public void updateProduct(int storeId, int productId, String newProductName, Integer newQuantity, Integer newPrice) {
        // dana added this proxy function for stock management use case
    }

    public ProductDTO getProductDTO(int productId) {
        // dana added this proxy function for get products use case
        return new ProductDTO();
    }
}
