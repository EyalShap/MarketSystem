package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

public class DefaultDiscountPolicy extends DiscountPolicy{

    public DefaultDiscountPolicy(String args) {
        super(args);
    }

    @Override
    public double giveDiscount(ProductDTO productDTO, CartItemDTO cartItemDTO) {
        return productDTO.getProductPrice() * cartItemDTO.getAmount();
    }
}
