package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyFacade {

    private Map<Integer, DiscountManager> mapper;
    private ProductFacade productFacade;
    private static DiscountPolicyFacade instance;

    public DiscountPolicyFacade(ProductFacade productFacade) {
        mapper = new HashMap<Integer, DiscountManager>();
        this.productFacade = productFacade;
    }

    public static DiscountPolicyFacade getInstance() {
        if (instance == null) {
            instance = new DiscountPolicyFacade(new ProductFacade());
        }
        return instance;
    }

    // for now that function dosent do anything special
    public boolean addDiscountPolicy(int storeId, String args) {
        DiscountPolicy dp;
        synchronized(mapper){
            if (mapper.get(storeId) == null) {
                mapper.put(storeId, new DiscountManager());
            }
        }
        try {
            // create new DiscountPolicy
            dp = null;
        } catch (Exception e) {
            return false;
        }
        DiscountManager discountManager = mapper.get(storeId);
        discountManager.addDiscountPolicy(dp);
        return true;
    }

    public List<ProductDataPrice> calculatePrice(int storeId, List<CartItemDTO> cart) {
        DiscountManager discountManager = mapper.get(storeId);
        Map<Integer, ProductDTO> productDTOMap = new HashMap<>();
        for(CartItemDTO cartItemDTO : cart){
            productDTOMap.put(cartItemDTO.getProductId(), productFacade.getProductDTO(cartItemDTO.getProductId()));
        }
        return discountManager.giveDiscount(cart, productDTOMap);
    }
}
