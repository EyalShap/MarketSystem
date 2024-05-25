package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.List;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

public class DefaultBuyPolicy extends BuyPolicy {

    DefaultBuyPolicy(List<BuyType> buytypes, String args) {
        super(buytypes, args);
        this.id = 0;
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, String username) {
        return true;
    }
}
