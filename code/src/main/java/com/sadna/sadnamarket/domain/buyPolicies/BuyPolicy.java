package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;

public abstract class BuyPolicy {
    int id;
    List<BuyType> buytypes;

    BuyPolicy(List<BuyType> buytypes) {
        this.buytypes = buytypes;
    }

    public abstract boolean canBuy(List<CartItemDTO> cart, String username);
}
