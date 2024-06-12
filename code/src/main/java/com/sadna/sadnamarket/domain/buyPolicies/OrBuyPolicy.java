package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.util.List;
import java.util.Map;

public class OrBuyPolicy extends CompositeBuyPolicy{

    public OrBuyPolicy(int id, BuyPolicy policy1, BuyPolicy policy2) {
        super(id, policy1, policy2);
        setErrorDescription(Error.makeOrBuyPolicyError(policy1.getErrorDescription(), policy2.getErrorDescription()));
    }

    public OrBuyPolicy() {
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        return policy1.canBuy(cart, products, user) || policy2.canBuy(cart, products, user);
    }

    @Override
    protected boolean dependsOnUser() {
        return policy1.dependsOnUser() && policy2.dependsOnUser();
    }
}
