package com.sadna.sadnamarket.domain.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = ProductController.getInstance();
        productController.resetProducts();
    }

    @Test
    void Given_ValidInput_When_AddProduct_Then_ProductIsAdded() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        int productId = productController.addProduct(storeId, productName, productQuantity, productPrice);

        Map<Integer, List<Product>> products = productController.getProducts();
        assertTrue(products.containsKey(storeId));
        List<Product> storeProducts = products.get(storeId);
        assertNotNull(storeProducts);
        assertFalse(storeProducts.isEmpty());

        Product addedProduct = storeProducts.get(0);
        assertEquals(productId, addedProduct.getProductID());
        assertEquals(productName, addedProduct.getProductName());
        assertEquals(productQuantity, addedProduct.getProductAmount());
        assertEquals(productPrice, addedProduct.getProductPrice());
    }

    @Test
    void Given_NegativeStoreId_When_AddProduct_Then_ThrowsException() {
        int storeId = -1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.addProduct(storeId, productName, productQuantity, productPrice));

        String expectedMessage = String.format("Store ID %d is invalid.", storeId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_NullProductName_When_AddProduct_Then_ThrowsException() {
        int storeId = 1;
        String productName = null;
        int productQuantity = 10;
        int productPrice = 100;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.addProduct(storeId, productName, productQuantity, productPrice));

        String expectedMessage = "Product name cannot be null or empty.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_NegativeProductQuantity_When_AddProduct_Then_ThrowsException() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = -10;
        int productPrice = 100;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.addProduct(storeId, productName, productQuantity, productPrice));

        String expectedMessage = "Product quantity cannot be negative.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_NegativeProductPrice_When_AddProduct_Then_ThrowsException() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = -100;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.addProduct(storeId, productName, productQuantity, productPrice));

        String expectedMessage = "Product price cannot be negative.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_ExistingProduct_When_RemoveProduct_Then_ProductIsRemoved() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        int productId = productController.addProduct(storeId, productName, productQuantity, productPrice);
        productController.removeProduct(storeId, productId);

        List<Product> storeProducts = productController.getProducts().get(storeId);
        assertNotNull(storeProducts);
        assertTrue(storeProducts.isEmpty());
    }

    @Test
    void Given_NonExistentStoreId_When_RemoveProduct_Then_ThrowsException() {
        int storeId = 999;
        int productId = 0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.removeProduct(storeId, productId));

        String expectedMessage = String.format("Store ID %d does not exist.", storeId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_NonExistentProductId_When_RemoveProduct_Then_ThrowsException() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        productController.addProduct(storeId, productName, productQuantity, productPrice);
        int nonExistentProductId = 999;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.removeProduct(storeId, nonExistentProductId));

        String expectedMessage = String.format("Product ID %d does not exist in store ID %d.", nonExistentProductId,
                storeId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_ValidInput_When_UpdateProduct_Then_ProductIsUpdated() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        int productId = productController.addProduct(storeId, productName, productQuantity, productPrice);

        String newProductName = "updatedProduct1";
        int newQuantity = 20;
        int newPrice = 200;

        productController.updateProduct(storeId, productId, newProductName, newQuantity, newPrice);

        Product updatedProduct = productController.getProducts().get(storeId).get(0);
        assertEquals(newProductName, updatedProduct.getProductName());
        assertEquals(newQuantity, updatedProduct.getProductAmount());
        assertEquals(newPrice, updatedProduct.getProductPrice());
    }

    @Test
    void Given_NonExistentStoreId_When_UpdateProduct_Then_ThrowsException() {
        int storeId = 999;
        int productId = 0;
        String newProductName = "updatedProduct";
        Integer newQuantity = 10;
        Integer newPrice = 100;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.updateProduct(storeId, productId, newProductName, newQuantity, newPrice));

        String expectedMessage = String.format("Store ID %d does not exist.", storeId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void Given_NonExistentProductId_When_UpdateProduct_Then_ThrowsException() {
        int storeId = 1;
        String productName = "product1";
        int productQuantity = 10;
        int productPrice = 100;

        productController.addProduct(storeId, productName, productQuantity, productPrice);
        int nonExistentProductId = 999;
        String newProductName = "updatedProduct";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productController.updateProduct(storeId, nonExistentProductId, newProductName, null, null));

        String expectedMessage = String.format("Product ID %d does not exist in store ID %d.", nonExistentProductId,
                storeId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
