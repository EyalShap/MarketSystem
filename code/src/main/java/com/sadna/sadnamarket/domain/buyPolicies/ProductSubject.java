package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;
import java.util.Map;

public class ProductSubject extends PolicySubject{
    private int productId;

    public ProductSubject(int productId) {
        this.productId = productId;
    }

    public ProductSubject() {
    }

    @Override
    public int subjectAmount(List<CartItemDTO> cart, Map<Integer, ProductDTO> products) {
        int productAmount = 0;
        for(CartItemDTO item : cart) {
            if(isSubject(products.get(item.getProductId())))
                productAmount += item.getAmount();
        }
        return productAmount;
    }

    @Override
    public boolean isSubject(ProductDTO product) {
        return product.getProductID() == this.productId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
