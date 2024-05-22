package com.sadna.sadnamarket.domain.products;

import java.util.List;
import java.util.Set;

public interface IProductRepository {

    public Product getProduct(int productId);

    public Set<Integer> getAllProductIds();

    public void removeProduct(int productId);

    public int addProduct(String productName, int productPrice, String productCategory);

    public boolean isExistProduct(int produtId);

    public List<Product> filterByName(String productName);

    public List<Product> filterByCategory(String category);
}
