package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyFacade {

    private Map<Integer, DiscountManager> mapper;
    private ProductFacade productFacade;
    private static DiscountPolicyFacade instance;

    public DiscountPolicyFacade() {
        mapper = new HashMap<Integer, DiscountManager>();
        productFacade = ProductFacade.getInstance();
    }

    public static DiscountPolicyFacade getInstance() {
        if (instance == null) {
            instance = new DiscountPolicyFacade();
        }
        return instance;
    }

    public boolean addDiscountPolicy(int storeId, String args) {
        DiscountPolicy dp;
        if (mapper.get(storeId) == null) {
            mapper.put(storeId, new DiscountManager());
        }
        try {
            dp = new DiscountPolicy(args);
        } catch (Exception e) {
            return false;
        }
        DiscountManager discountManager = mapper.get(storeId);
        discountManager.addDiscountPolicy(dp);
        return true;
    }

    public List<ProductDataPrice> calculatePrice(int storeId, List<CartItemDTO> cart) {
        DiscountManager discountManager = mapper.get(storeId);
        return discountManager.giveDiscount(cart, productFacade);
    }
}
