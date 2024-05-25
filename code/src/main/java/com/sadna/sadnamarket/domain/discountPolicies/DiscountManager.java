package com.sadna.sadnamarket.domain.discountPolicies;

import java.util.ArrayList;
import java.util.List;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

public class DiscountManager {
    List<DiscountPolicy> discounts;

    public DiscountManager() {
        discounts = new ArrayList<>();
    }

    public void addDiscountPolicy(DiscountPolicy discountPolicy) {
        discounts.add(discountPolicy);
    }

    // for now that function dosent do anything special
    public List<ProductDataPrice> giveDiscount(List<CartItemDTO> cart, ProductFacade productFacade) {
        List<ProductDataPrice> listProductDataPrice = new ArrayList<>();
        for (CartItemDTO cartItemDTO : cart) {
            ProductDTO pDTO = productFacade.getProductDTO(cartItemDTO.getProductId());
            ProductDataPrice ProductDataPrice = new ProductDataPrice(cartItemDTO.getProductId(),
                    cartItemDTO.getAmount(),
                    pDTO.getProductPrice(), pDTO.getProductPrice());
            listProductDataPrice.add(ProductDataPrice);
        }
        return listProductDataPrice;
    }

}
