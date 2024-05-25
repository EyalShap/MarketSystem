package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.ArrayList;
import java.util.List;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

public class BuyPolicyManager {
    private List<BuyPolicy> buyPolicies;

    public BuyPolicyManager() {
        buyPolicies = new ArrayList<>();
    }

    public void addBuyPolicy(BuyPolicy bp) {
        buyPolicies.add(bp);
    }

    public String canBuy(List<CartItemDTO> cart, String username) {
        String error = "";
        // if one policy says that you cant buy return false;
        for (BuyPolicy policy : buyPolicies) {
            if (!policy.canBuy(cart, username)) {
                error = error + policy.getErrorDescription() + "\n";
            }
        }
        return error;
    }
}
