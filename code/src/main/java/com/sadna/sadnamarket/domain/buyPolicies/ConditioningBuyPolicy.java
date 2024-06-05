package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;

public class ConditioningBuyPolicy extends CompositeBuyPolicy{
    // policy1 -> policy2
    public ConditioningBuyPolicy(int id, List<BuyType> buytypes, BuyPolicy policy1, BuyPolicy policy2) {
        super(id, buytypes, policy1, policy2);
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policy1.canBuy(cart, products, user)) {
            return policy2.canBuy(cart, products, user);
        }
        return false;
    }
}
