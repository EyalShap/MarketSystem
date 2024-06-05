package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

public class BuyPolicyManager {
    private List<BuyPolicy> buyPolicies;

    public BuyPolicyManager() {
        buyPolicies = new ArrayList<>();
    }

    public void addBuyPolicy(BuyPolicy bp) {
        synchronized (buyPolicies) {
            buyPolicies.add(bp);
        }
    }

    public String canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        String error = "";
        // if one policy says that you cant buy return false;
        for (BuyPolicy policy : buyPolicies) {
            if (!policy.canBuy(cart, products, user)) {
                error = error + policy.getErrorDescription() + "\n";
            }
        }
        return error;
    }
}
