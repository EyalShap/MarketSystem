package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyPolicyFacade {
    Map<Integer, BuyPolicy> mapper;

    public BuyPolicyFacade() {
        mapper = new HashMap<Integer, BuyPolicy>();
        List<BuyType> buyTypes = new ArrayList<>();
        buyTypes.add(BuyType.immidiatePurchase);
        mapper.put(0, new DefaultBuyPolicy(buyTypes));
    }

    public int addBuyPolicy(List<BuyType> buyTypes, BuyPolicyType buypolicyType, List<String> args) {
        int newID = Collections.max(mapper.keySet()) + 1;
        if (buypolicyType.equals(BuyPolicyType.Default)) {
            mapper.put(newID, new DefaultBuyPolicy(buyTypes));
        }
        return newID;
    }

    public boolean canBuy(int policyId, List<CartItemDTO> cart, String username) {
        BuyPolicy buypolicy = mapper.get(policyId);
        return buypolicy.canBuy(cart, username);
    }
}
