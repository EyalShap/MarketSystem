package com.sadna.sadnamarket.domain.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemoryProductRepository implements IProductRepository {
    private int nextProductId;
    private Map<Integer, Product> products;
    private static final Logger logger = LogManager.getLogger(MemoryProductRepository.class);

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
        logger.info("product " + createdProduct + " succesfully added");
        return nextProductId - 1;
    }

    @Override
    public Product getProduct(int productId) {
        if (!isExistProduct(productId)) {
            logger.error(String.format("Product Id %d does not exist.", productId));
            throw new IllegalArgumentException(String.format("Product Id %d does not exist.", productId));
        }
        return products.get(productId);
    }

    @Override
    public Set<Integer> getAllProductIds() {
        return products.keySet();
    }

    @Override
    public void removeProduct(int productId) {
        if (!isExistProduct(productId)) {
            logger.error(String.format("Product Id %d does not exist.", productId));
            throw new IllegalArgumentException(String.format("Product Id %d does not exist.", productId));
        }

        Product product = getProduct(productId);
        if (!product.isActiveProduct()) {
            logger.error(String.format("Product Id %d was already removed.", productId));
            throw new IllegalArgumentException(String.format("Product Id %d was already removed.", productId));
        }
        product.disableProduct();
        logger.error(String.format("Product Id %d was succesully removed.", productId));
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
