package com.sadna.sadnamarket.domain.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFacade {
    private static ProductFacade instance;
    private IProductRepository productRepository;
    private Map<Integer, List<Product>> products; // storeId -> list of products

    public ProductFacade() {
        productRepository = new MemoryProductRepository();
        products = new HashMap<>();
    }

    public static ProductFacade getInstance() {
        if (instance == null) {
            instance = new ProductFacade();
        }
        return instance;
    }

    public int addProduct(int storeId, String productName, int productPrice, String productCategory) {
        if (storeId < 0) {
            throw new IllegalArgumentException(String.format("Store Id %d is invalid.", storeId));
        }

        checkProductAttributes(productName, productPrice, productCategory);

        int productId = productRepository.addProduct(productName, productPrice, productCategory);

        Product createdProduct = productRepository.getProduct(productId);

        List<Product> storeProducts = products.get(storeId);
        if (storeProducts == null) {
            storeProducts = new ArrayList<>();
        }
        storeProducts.add(createdProduct);
        products.put(storeId, storeProducts);

        return productId;
    }

    public void removeProduct(int storeId, int productId) {
        if (!isStoreExist(storeId))
            throw new IllegalArgumentException(String.format("Store Id %d does not exist.", storeId));

        if (isProductExistInStore(storeId, productId)) {
            productRepository.removeProduct(productId);
        } else
            throw new IllegalArgumentException(
                    String.format("Product Id %d does not exist in store Id %d.", productId, storeId));
    }

    public void updateProduct(int storeId, int productId, String newProductName,
            int newPrice, String newCategory) {
        if (!isStoreExist(storeId))
            throw new IllegalArgumentException(String.format("Store Id %d does not exist.", storeId));

        if (!isProductExistInStore(storeId, productId))
            throw new IllegalArgumentException(
                    String.format("Product Id %d does not exist in store Id %d.", productId, storeId));

        checkProductAttributes(newProductName, newPrice, newCategory);

        Product productToUpdate = productRepository.getProduct(productId);

        if (!productToUpdate.isActiveProduct())
            throw new IllegalArgumentException(
                    String.format("Product Id %d was deleted from store Id %d.", productId, storeId));

        productToUpdate.setProductName(newProductName);
        productToUpdate.setProductPrice(newPrice);
        productToUpdate.setProductCategory(newCategory);
    }

    public List<ProductDTO> getAllProductsByName(String productName) {
        return ProductMapper.toProductDTOList(productRepository.filterByName(productName));

    }

    public List<ProductDTO> getAllProductsByCategory(String category) {
        return ProductMapper.toProductDTOList(productRepository.filterByCategory(category));
    }

    // private Product getProduct(int storeId, int productId) {
    // List<Product> storeProducts = products.get(storeId);
    // Product result = null;
    // for (Product product : storeProducts) {
    // if (product.getProductId() == productId) {
    // result = product;
    // break;
    // }
    // }
    // return result;
    // }

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
                || storeProducts.stream().noneMatch(product -> product.getProductId() == productId));
    }

    public ProductDTO getProductDTO(int productId) {
        Product result = productRepository.getProduct(productId);

        return result.getProductDTO();
    }

    // public Product getProductIfExist(int productId) {
    // Product result = null;

    // for (Map.Entry<Integer, List<Product>> entry : products.entrySet()) {
    // // Integer storeId = entry.getKey();
    // if (result != null)
    // break;
    // List<Product> storeProducts = entry.getValue();
    // for (Product product : storeProducts) {
    // if (product.getProductId() == productId) {
    // result = product;
    // break;
    // }
    // }
    // }
    // return result;
    // }

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
            result.append("Store Id: ").append(storeId).append("\n");
            for (Product product : storeProducts) {
                result.append("  ").append(product).append("\n");
            }
        }
        return result.toString();
    }
    //
    // public static void main(String[] args) {
    // ProductFacade facade = ProductFacade.getInstance();
    // try {
    // facade.addProduct(1, "product1", 10, "cat1");
    // facade.addProduct(1, "product2", 5, "cat1");
    // facade.addProduct(2, "product3", 5, "cat2");
    //
    // System.out.println("Initial state:");
    // System.out.println(facade);
    //
    // facade.removeProduct(1, 0);
    //
    // System.out.println("After removal:");
    // System.out.println(facade);
    //
    // facade.updateProduct(1, 1, "updatedProduct2", 20, "cat3");
    //
    // System.out.println("After update:");
    // System.out.println(facade);
    // } catch (IllegalArgumentException e) {
    // System.err.println("Error: " + e.getMessage());
    // }
    // }

}