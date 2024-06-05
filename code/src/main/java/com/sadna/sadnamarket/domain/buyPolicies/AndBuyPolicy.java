package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;

public class AndBuyPolicy extends CompositeBuyPolicy{

    public AndBuyPolicy(int id, BuyPolicy policy1, BuyPolicy policy2) {
        super(id, policy1, policy2);
    }

    public AndBuyPolicy() {
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        return policy1.canBuy(cart, products, user) && policy2.canBuy(cart, products, user);
    }
}
