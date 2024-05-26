package com.sadna.sadnamarket.domain.products;

import java.util.*;
import java.util.stream.Collectors;

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

    public int addProduct(int storeId, String productName, double productPrice, String productCategory,
            double productRank) {
        if (storeId < 0) {
            throw new IllegalArgumentException(String.format("Store Id %d is invalid.", storeId));
        }

        checkProductAttributes(productName, productPrice, productCategory, productRank);

        // other checks ??
        // already created in this store ?
        int productId = productRepository.addProduct(productName, productPrice, productCategory, productRank);

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
            double newPrice, String newCategory, double newRank) {
        if (!isStoreExist(storeId))
            throw new IllegalArgumentException(String.format("Store Id %d does not exist.", storeId));

        if (!isProductExistInStore(storeId, productId))
            throw new IllegalArgumentException(
                    String.format("Product Id %d does not exist in store Id %d.", productId, storeId));

        checkProductAttributes(newProductName, newPrice, newCategory, newRank);

        Product productToUpdate = productRepository.getProduct(productId);

        // other checks ??
        // merge products with same properties ?
        if (!productToUpdate.isActiveProduct())
            throw new IllegalArgumentException(
                    String.format("Product Id %d was deleted from store Id %d.", productId, storeId));

        productToUpdate.setProductName(newProductName);
        productToUpdate.setProductPrice(newPrice);
        productToUpdate.setProductCategory(newCategory);
        productToUpdate.setProductRank(newRank);
    }

    public List<ProductDTO> getFilteredProducts(List<Integer> storeProductIds, String productName,
            double maxProductPrice, String productCategory,
            double minProductRank) {

        List<Product> storeProducts = productRepository.getProducts(storeProductIds);

        if (productName != null && isValidProductName(productName)) {
            storeProducts = storeProducts.stream()
                    .filter(product -> product.getProductCategory().equals(productName))
                    .collect(Collectors.toList());
        }

        if (productCategory != null && isValidProductCategory(productCategory)) {
            storeProducts = storeProducts.stream()
                    .filter(product -> product.getProductCategory().equals(productCategory))
                    .collect(Collectors.toList());
        }

        // if (minProductPrice != null && isValidProductPrice(minProductPrice)) {
        // storeProducts = storeProducts.stream()
        // .filter(product -> product.getProductPrice() >= minProductPrice)
        // .collect(Collectors.toList());
        // }

        if (maxProductPrice != -1 && isValidProductPrice(maxProductPrice)) {
            storeProducts = storeProducts.stream()
                    .filter(product -> product.getProductPrice() <= maxProductPrice)
                    .collect(Collectors.toList());
        }

        if (minProductRank != -1 && isValidProductRank(minProductRank)) {
            storeProducts = storeProducts.stream()
                    .filter(product -> product.getProductPrice() >= minProductRank)
                    .collect(Collectors.toList());
        }

        // if (maxProductRank != null && isValidProductRank(maxProductRank)) {
        // storeProducts = storeProducts.stream()
        // .filter(product -> product.getProductRank() <= maxProductRank)
        // .collect(Collectors.toList());
        // }

        return ProductMapper.toProductDTOList(storeProducts);
    }

    public List<ProductDTO> getAllProducts() {
        return ProductMapper.toProductDTOList(productRepository.getAllProducts());
    }

    public List<ProductDTO> getAllProductsByName(String productName) {
        return ProductMapper.toProductDTOList(productRepository.filterByName(productName));
    }

    public List<ProductDTO> getAllProductsByCategory(String category) {
        return ProductMapper.toProductDTOList(productRepository.filterByCategory(category));
    }

    private boolean isValidProductName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidProductRank(double productRank) {
        return productRank >= 0 && productRank <= 5;
    }

    private boolean isValidProductPrice(double productPrice) {
        return productPrice >= 0;
    }

    private boolean isValidProductCategory(String category) {
        return category != null && !category.trim().isEmpty();
    }

    private void checkProductAttributes(String newProductName, double newPrice, String newCategory, double newRank) {
        boolean isValidProductName = isValidProductName(newProductName);
        boolean isValidProductPrice = isValidProductPrice(newPrice);
        boolean isValidProductCategory = isValidProductCategory(newCategory);
        boolean isValidProductRank = isValidProductRank(newRank);

        boolean isAllValid = isValidProductName && isValidProductPrice && isValidProductCategory && isValidProductRank;

        if (!isAllValid) {
            StringBuilder result = new StringBuilder("Product information's are wrong: ");

            if (!isValidProductName)
                result.append("Product name cannot be null or empty. ");
            if (!isValidProductPrice)
                result.append("Product price cannot be negative. ");
            if (!isValidProductCategory)
                result.append("Product category cannot be null or empty. ");
            if (!isValidProductRank)
                result.append("Product rank have to be between 0 and 5. ");

            throw new IllegalArgumentException(String.valueOf(result));
        }
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

    // package-private for tests
    Map<Integer, List<Product>> getProducts() {
        return products;
    }

    // package-private for tests
    void resetProducts() {
        products = new HashMap<>();
    }

    // }

}