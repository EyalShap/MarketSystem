package com.sadna.sadnamarket.domain.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    private ProductController productController;

    @BeforeEach
    void setUp() {
        productController = ProductController.getInstance();
        productController.resetProducts();
    }

    @Test
    void givenValidStoreIdAndProductAttributes_whenAddingProduct_thenProductIsAdded() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        int productId = productController.addProduct(storeId, productName, productPrice, productCategory);

        Map<Integer, List<Product>> products = productController.getProducts();
        assertTrue(products.containsKey(storeId));
        assertEquals(1, products.get(storeId).size());
        Product addedProduct = products.get(storeId).get(0);
        assertEquals(productId, addedProduct.getProductID());
        assertEquals(productName, addedProduct.getProductName());
        assertEquals(productPrice, addedProduct.getProductPrice());
        assertEquals(productCategory, addedProduct.getProductCategory());
    }

    @Test
    void givenInvalidStoreId_whenAddingProduct_thenThrowsException() {
        int storeId = -1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.addProduct(storeId, productName, productPrice, productCategory);
        });

        assertEquals("Store ID -1 is invalid.", exception.getMessage());
    }

    @Test
    void givenValidProductIdAndStoreId_whenRemovingProduct_thenProductIsRemoved() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        int productId = productController.addProduct(storeId, productName, productPrice, productCategory);
        productController.removeProduct(storeId, productId);

        Map<Integer, List<Product>> products = productController.getProducts();
        assertTrue(products.containsKey(storeId));
        Product removedProduct = products.get(storeId).get(0);
        assertFalse(removedProduct.isActiveProduct());
    }

    @Test
    void givenInvalidStoreId_whenRemovingProduct_thenThrowsException() {
        int storeId = 1;
        int productId = 0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.removeProduct(storeId, productId);
        });

        assertEquals("Store ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void givenInvalidProductId_whenRemovingProduct_thenThrowsException() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        productController.addProduct(storeId, productName, productPrice, productCategory);

        int invalidProductId = 999;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.removeProduct(storeId, invalidProductId);
        });

        assertEquals("Product ID 999 does not exist in store ID 1.", exception.getMessage());
    }

    @Test
    void givenValidProductIdAndStoreId_whenUpdatingProduct_thenProductIsUpdated() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        int productId = productController.addProduct(storeId, productName, productPrice, productCategory);
        String newProductName = "Updated Product";
        int newProductPrice = 200;
        String newProductCategory = "Category2";

        productController.updateProduct(storeId, productId, newProductName, newProductPrice, newProductCategory);

        Map<Integer, List<Product>> products = productController.getProducts();
        assertTrue(products.containsKey(storeId));
        Product updatedProduct = products.get(storeId).get(0);
        assertEquals(productId, updatedProduct.getProductID());
        assertEquals(newProductName, updatedProduct.getProductName());
        assertEquals(newProductPrice, updatedProduct.getProductPrice());
        assertEquals(newProductCategory, updatedProduct.getProductCategory());
    }

    @Test
    void givenInvalidStoreId_whenUpdatingProduct_thenThrowsException() {
        int storeId = 1;
        int productId = 0;
        String newProductName = "Updated Product";
        int newProductPrice = 200;
        String newProductCategory = "Category2";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.updateProduct(storeId, productId, newProductName, newProductPrice, newProductCategory);
        });

        assertEquals("Store ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void givenInvalidProductId_whenUpdatingProduct_thenThrowsException() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        productController.addProduct(storeId, productName, productPrice, productCategory);

        int invalidProductId = 999;
        String newProductName = "Updated Product";
        int newProductPrice = 200;
        String newProductCategory = "Category2";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.updateProduct(storeId, invalidProductId, newProductName, newProductPrice, newProductCategory);
        });

        assertEquals("Product ID 999 does not exist in store ID 1.", exception.getMessage());
    }

    @Test
    void givenDeletedProduct_whenUpdatingProduct_thenThrowsException() {
        int storeId = 1;
        String productName = "Test Product";
        int productPrice = 100;
        String productCategory = "Category1";

        int productId = productController.addProduct(storeId, productName, productPrice, productCategory);
        productController.removeProduct(storeId, productId);

        String newProductName = "Updated Product";
        int newProductPrice = 200;
        String newProductCategory = "Category2";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.updateProduct(storeId, productId, newProductName, newProductPrice, newProductCategory);
        });

        assertEquals("Product ID " + productId + " was deleted from store ID " + storeId + ".", exception.getMessage());
    }
}
