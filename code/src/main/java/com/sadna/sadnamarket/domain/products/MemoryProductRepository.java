package com.sadna.sadnamarket.domain.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoryProductRepository implements IProductRepository {
    private int nextProductId;
    private Map<Integer, Product> products;

    public MemoryProductRepository() {
        this.nextProductId = 0;
        this.products = new HashMap<>();
    }

    @Override
    public int addProduct(String productName, int productPrice,
            String productCategory) {
        Product createdProduct = new Product(nextProductId, productName, productPrice,
                productCategory);

        products.put(nextProductId, createdProduct);
        nextProductId++;

        return nextProductId - 1;
    }

    @Override
    public Product getProduct(int productId) {
        if (!isExistProduct(productId))
            throw new IllegalArgumentException(String.format("Product Id %d does not exist.", productId));

        return products.get(productId);
    }

    @Override
    public Set<Integer> getAllProductIds() {
        return products.keySet();
    }

    @Override
    public void removeProduct(int productId) {
        if (!isExistProduct(productId))
            throw new IllegalArgumentException(String.format("Product Id %d does not exist.", productId));

        Product product = getProduct(productId);
        if (!product.isActiveProduct())
            throw new IllegalArgumentException(String.format("Product Id %d was already removed.", productId));

        product.disableProduct();

    }

    @Override
    public boolean isExistProduct(int productId) {
        return products.containsKey(productId);
    }

    @Override
    public List<Product> filterByName(String productName) {
        return products.values().stream()
                .filter(product -> product.getProductName().equalsIgnoreCase(productName) && product.isActiveProduct())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByCategory(String category) {
        return products.values().stream()
                .filter(product -> product.getProductCategory().equalsIgnoreCase(category) && product.isActiveProduct())
                .collect(Collectors.toList());
    }
}
