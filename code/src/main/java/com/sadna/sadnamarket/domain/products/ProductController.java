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

    public int addProduct(int storeId, String productName, int productPrice, String productCategory) {
        if (storeId < 0) {
            throw new IllegalArgumentException(String.format("Store ID %d is invalid.", storeId));
        }

        checkProductAttributes(productName, productPrice, productCategory);

        Product createdProduct = new Product(nextProductId, productName, productPrice, productCategory);

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
        if (!isStoreExist(storeId))
            throw new IllegalArgumentException(String.format("Store ID %d does not exist.", storeId));

        List<Product> storeProducts = products.get(storeId);

        if (isProductExistInStore(storeId, productId)) {
            Product productToRemove = getProduct(storeId, productId);
            productToRemove.disableProduct();// turn off his active flag
        } else
            throw new IllegalArgumentException(
                    String.format("Product ID %d does not exist in store ID %d.", productId, storeId));
    }

    public void updateProduct(int storeId, int productId, String newProductName,
            int newPrice, String newCategory) {
        if (!isStoreExist(storeId))
            throw new IllegalArgumentException(String.format("Store ID %d does not exist.", storeId));

        if (!isProductExistInStore(storeId, productId))
            throw new IllegalArgumentException(
                    String.format("Product ID %d does not exist in store ID %d.", productId, storeId));

        checkProductAttributes(newProductName, newPrice, newCategory);

        Product productToUpdate = getProduct(storeId, productId);

        if (!productToUpdate.isActiveProduct())
            throw new IllegalArgumentException(
                    String.format("Product ID %d was deleted from store ID %d.", productId, storeId));

        productToUpdate.setProductName(newProductName);
        productToUpdate.setProductPrice(newPrice);
        productToUpdate.setProductCategory(newCategory);
    }

    private Product getProduct(int storeId, int productId) {
        List<Product> storeProducts = products.get(storeId);
        Product result = null;
        for (Product product : storeProducts) {
            if (product.getProductID() == productId) {
                result = product;
                break;
            }
        }
        return result;
    }

    private void checkProductAttributes(String newProductName, int newPrice, String newCategory) {
        String isValidProductName = checkProductName(newProductName);
        String isValidProductPrice = checkProductPrice(newPrice);
        String isValidProductCategory = checkProductCategory(newCategory);

        boolean isAllValid = isValidProductName.isEmpty() && isValidProductPrice.isEmpty()
                && isValidProductCategory.isEmpty();

        if (!isAllValid) {
            StringBuilder result = new StringBuilder("Product can be updated because: ");
            result.append(isValidProductName).append(isValidProductPrice).append(isValidProductCategory);
            throw new IllegalArgumentException(String.valueOf(result));
        }
    }

    private String checkProductName(String productName) {
        return (productName == null || productName.trim().isEmpty()) ? "Product name cannot be null or empty. " : "";
    }

    private String checkProductPrice(Integer productPrice) {
        return productPrice < 0 ? "Product price cannot be negative. " : "";
    }

    private String checkProductCategory(String productCategory) {
        return (productCategory == null || productCategory.trim().isEmpty())
                ? "Product category cannot be null or empty. "
                : "";
    }

    private boolean isStoreExist(int storeId) {
        return products.containsKey(storeId);
    }

    private boolean isProductExistInStore(int storeId, int productId) {
        List<Product> storeProducts = products.get(storeId);
        return !(storeProducts == null
                || storeProducts.stream().noneMatch(product -> product.getProductID() == productId));
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
    //
    // public static void main(String[] args) {
    // ProductController controller = ProductController.getInstance();
    // try {
    // controller.addProduct(1, "product1", 10, "cat1");
    // controller.addProduct(1, "product2", 5, "cat1");
    // controller.addProduct(2, "product3", 5, "cat2");
    //
    // System.out.println("Initial state:");
    // System.out.println(controller);
    //
    // controller.removeProduct(1, 0);
    //
    // System.out.println("After removal:");
    // System.out.println(controller);
    //
    // controller.updateProduct(1, 1, "updatedProduct2", 20, "cat3");
    //
    // System.out.println("After update:");
    // System.out.println(controller);
    // } catch (IllegalArgumentException e) {
    // System.err.println("Error: " + e.getMessage());
    // }
    // }

}
