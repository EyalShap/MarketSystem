package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.util.List;
import java.util.Map;

public class ConditioningBuyPolicy extends CompositeBuyPolicy{
    // policy1 -> policy2 = !policy1 || policy2
    public ConditioningBuyPolicy(int id, BuyPolicy policy1, BuyPolicy policy2) {
        super(id, policy1, policy2);
        setErrorDescription(Error.makeConditioningBuyPolicyError(policy1.getErrorDescription(), policy2.getErrorDescription()));
    }

    public ConditioningBuyPolicy() {
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        boolean res1 = policy1.canBuy(cart, products, user);
        boolean res2 = policy2.canBuy(cart, products, user);
        if(user == null && policy1.dependsOnUser())
            return res2;
        return !res1 || res2;
    }

    @Override
    protected boolean dependsOnUser() {
        return policy1.dependsOnUser() || policy2.dependsOnUser();
    }
}
