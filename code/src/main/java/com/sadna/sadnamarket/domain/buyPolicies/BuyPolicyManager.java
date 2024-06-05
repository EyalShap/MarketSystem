package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

public class BuyPolicyManager {
    private List<Integer> buyPolicyIds;
    private BuyPolicyFacade facade;

    public BuyPolicyManager(BuyPolicyFacade facade) {
        buyPolicyIds = new ArrayList<>();
        this.facade = facade;
    }

    public void addBuyPolicy(int buyPolicyId) throws Exception {
        synchronized (buyPolicyIds) {
            if(buyPolicyIds.contains(buyPolicyId))
                throw new Exception();
            buyPolicyIds.add(buyPolicyId);
        }
    }

    public String canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) throws Exception {
        String error = "";
        // if one policy says that you cant buy return false;
        for (Integer policyId : buyPolicyIds) {
            BuyPolicy policy = facade.getBuyPolicy(policyId);
            if (!policy.canBuy(cart, products, user)) {
                error = error + policy.getErrorDescription() + "\n";
            }
        }
        return error;
    }
}
