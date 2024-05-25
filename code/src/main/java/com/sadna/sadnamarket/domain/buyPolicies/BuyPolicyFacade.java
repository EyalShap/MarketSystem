package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.discountPolicies.DiscountManager;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicy;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyPolicyFacade {
    private Map<Integer, BuyPolicyManager> mapper;
    private static BuyPolicyFacade instance;

    public BuyPolicyFacade() {
        mapper = new HashMap<Integer, BuyPolicyManager>();
    }

    public static BuyPolicyFacade getInstance() {
        if (instance == null) {
            instance = new BuyPolicyFacade();
        }
        return instance;
    }

    public boolean addBuyPolicy(int storeId, String args) {
        BuyPolicy bp;
        if (mapper.get(storeId) == null) {
            mapper.put(storeId, new BuyPolicyManager());
        }
        try {
            // create new DiscountPolicy
            dp = null;
        } catch (Exception e) {
            return false;
        }
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        buyPolicyManager.addDiscountPolicy(bp);
        return true;
    }

    public boolean canBuy(int storeId, List<CartItemDTO> cart, String username) {
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        return buyPolicyManager.canBuy(cart, username);
    }
}
