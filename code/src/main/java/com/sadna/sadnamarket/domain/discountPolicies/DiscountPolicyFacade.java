package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyFacade {

    private Map<Integer, DiscountPolicy> mapper;
    private ProductFacade productFacade;
    private static DiscountPolicyFacade instance;

    public DiscountPolicyFacade() {
        mapper = new HashMap<Integer, DiscountPolicy>();
        productFacade = ProductFacade.getInstance();
    }

    public static DiscountPolicyFacade getInstance() {
        if (instance == null) {
            instance = new DiscountPolicyFacade();
        }
        return instance;
    }

    public int addDiscountPolicy() {
        // proxy function
        // assuming it returns the id of the new policy
        return 0;
    }

    public List<ProductDataPrice> calculatePrice(List<Integer> discountPolicyIds, List<CartItemDTO> cart) {
        List<ProductDataPrice> recipe = new ArrayList<ProductDataPrice>();
        for (CartItemDTO cartItem : cart) {
            for (Integer dpid : discountPolicyIds) {
                DiscountPolicy discountPolicy = mapper.get(dpid);
                ProductDTO productDTO = productFacade.getProductDTO(cartItem.getProductId());
                recipe.add(new ProductDataPrice(cartItem.getProductId(),
                        cartItem.getAmount(),
                        productDTO.getProductPrice(),
                        discountPolicy.giveDiscount(productDTO, cartItem)));

            }
        }
        return recipe;
    }
}
