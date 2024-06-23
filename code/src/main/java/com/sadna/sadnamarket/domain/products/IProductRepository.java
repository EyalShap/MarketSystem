package com.sadna.sadnamarket.domain.products;

import java.util.List;
import java.util.Set;

public interface IProductRepository {

    public Product getProduct(int productId);

    public Set<Integer> getAllProductIds();

    public List<Product> getAllProducts();

    List<Product> getProducts(List<Integer> productIds);

    public void removeProduct(int productId);

    public int addProduct(String productName, double productPrice, String productCategory, double productRank, double productWeight, int storeId);

    public boolean isExistProduct(int productId);

    public List<Product> filterByName(String productName);

    public List<Product> filterByCategory(String category);
    public List <Product> getTopProducts();
}
