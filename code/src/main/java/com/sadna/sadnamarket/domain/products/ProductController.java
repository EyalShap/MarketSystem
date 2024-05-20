package com.sadna.sadnamarket.domain.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    private static ProductController instance;
    private Map<Integer, List<Product>> products; // storeId -> list of products
    private int nextProductId = 0;

    private ProductController() {
        products = new HashMap<>();
    }

    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    public int addProduct(int storeId, String productName, int productQuantity, int productPrice) {
        if (storeId < 0) {
            throw new IllegalArgumentException(String.format("Store ID %d is invalid.", storeId));
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (productQuantity < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative.");
        }
        if (productPrice < 0) {
            throw new IllegalArgumentException("Product price cannot be negative.");
        }

        Product createdProduct = new Product(nextProductId, productName, productQuantity, productPrice);

        List<Product> storeProducts = products.get(storeId);
        if (storeProducts == null) {
            storeProducts = new ArrayList<>();
        }
        storeProducts.add(createdProduct);
        products.put(storeId, storeProducts);

        nextProductId++;
        return createdProduct.getProductID();
    }

    public void removeProduct(int storeId, int productId) {
        if (!products.containsKey(storeId)) {
            throw new IllegalArgumentException(String.format("Store ID %d does not exist.", storeId));
        }

        List<Product> storeProducts = products.get(storeId);
        if (storeProducts == null || storeProducts.stream().noneMatch(product -> product.getProductID() == productId)) {
            throw new IllegalArgumentException(
                    String.format("Product ID %d does not exist in store ID %d.", productId, storeId));
        }

        storeProducts.removeIf(product -> product.getProductID() == productId);
    }

    public void updateProduct(int storeId, int productId, String newProductName, Integer newQuantity,
            Integer newPrice) {
        if (!products.containsKey(storeId)) {
            throw new IllegalArgumentException(String.format("Store ID %d does not exist.", storeId));
        }

        List<Product> storeProducts = products.get(storeId);
        Product productToUpdate = null;
        for (Product product : storeProducts) {
            if (product.getProductID() == productId) {
                productToUpdate = product;
                break;
            }
        }

        if (productToUpdate == null) {
            throw new IllegalArgumentException(
                    String.format("Product ID %d does not exist in store ID %d.", productId, storeId));
        }

        if (newProductName != null && !newProductName.trim().isEmpty()) {
            productToUpdate.setProductName(newProductName);
        }
        if (newQuantity != null) {
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Product quantity cannot be negative.");
            }
            productToUpdate.setProductAmount(newQuantity);
        }
        if (newPrice != null) {
            if (newPrice < 0) {
                throw new IllegalArgumentException("Product price cannot be negative.");
            }
            productToUpdate.setProductPrice(newPrice);
        }
    }

    // package-private for tests
    Map<Integer, List<Product>> getProducts() {
        return products;
    }

    // package-private for tests
    void resetProducts() {
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, List<Product>> entry : products.entrySet()) {
            int storeId = entry.getKey();
            List<Product> storeProducts = entry.getValue();
            result.append("Store ID: ").append(storeId).append("\n");
            for (Product product : storeProducts) {
                result.append("  ").append(product).append("\n");
            }
        }
        return result.toString();
    }

    // public static void main(String[] args) {
    // ProductController controller = ProductController.getInstance();
    // try {
    // controller.addProduct(1, "product1", 10, 100);
    // controller.addProduct(1, "product2", 5, 98780);
    // controller.addProduct(2, "product3", 5, 98780);
    //
    // System.out.println("Initial state:");
    // System.out.println(controller);
    //
    // controller.removeProduct(1, 0);
    //
    // System.out.println("After removal:");
    // System.out.println(controller);
    //
    // controller.updateProduct(1, 1, "updatedProduct2", 20, 20000);
    //
    // System.out.println("After update:");
    // System.out.println(controller);
    // } catch (IllegalArgumentException e) {
    // System.err.println("Error: " + e.getMessage());
    // }
    // }

}
