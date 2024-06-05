package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;
import java.util.Map;

public class CategorySubject extends PolicySubject{
    private String category;

    public CategorySubject(String category) {
        this.category = category;
    }

    @Override
    public int subjectAmount(List<CartItemDTO> cart, Map<Integer, ProductDTO> products) {
        int categoryAmount = 0;
        for(CartItemDTO item : cart) {
            int productId = item.getProductId();
            ProductDTO product = products.get(productId);
            if(isSubject(product))
                categoryAmount += item.getAmount();
        }
        return categoryAmount;
    }

    @Override
    public boolean isSubject(ProductDTO product) {
        return this.category.equals(product.getProductCategory());
    }

}
