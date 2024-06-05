package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;

public class AndBuyPolicy extends CompositeBuyPolicy{

    public AndBuyPolicy(List<BuyType> buytypes, BuyPolicy policy1, BuyPolicy policy2) {
        super(buytypes, policy1, policy2);
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        return policy1.canBuy(cart, products, user) && policy2.canBuy(cart, products, user);
    }
}
