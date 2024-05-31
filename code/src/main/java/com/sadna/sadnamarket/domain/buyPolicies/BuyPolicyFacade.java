package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.discountPolicies.DiscountManager;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicy;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.*;

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

    // for now that function dosent do anything special
    public boolean addBuyPolicy(int storeId, String args) {
        BuyPolicy bp;
        synchronized(mapper){
            if (!mapper.containsKey(storeId)) {
                mapper.put(storeId, new BuyPolicyManager());
            }
        }
        try {
            // create new DiscountPolicy
            bp = new DefaultBuyPolicy(new LinkedList<>(), "");
        } catch (Exception e) {
            return false;
        }
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        buyPolicyManager.addBuyPolicy(bp);
        return true;

    }

    public String canBuy(int storeId, List<CartItemDTO> cart, String username) {
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        return buyPolicyManager.canBuy(cart, username);
    }
}
