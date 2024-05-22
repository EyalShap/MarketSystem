package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;

public class BuyPolicyFacade {

    public int addBuyPolicy() {
        //proxy function
        // assuming it returns the id of the new policy
        return 0;
    }

    public boolean canBuy(int policyId, List<CartItemDTO> cart, String username) {
        return true;
    }
}
