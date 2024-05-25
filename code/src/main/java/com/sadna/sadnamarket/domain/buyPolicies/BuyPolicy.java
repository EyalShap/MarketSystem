package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;

public abstract class BuyPolicy {
    protected int id;
    protected List<BuyType> buytypes;
    protected String errorDescription;

    BuyPolicy(List<BuyType> buytypes, String args) {
        this.buytypes = buytypes;
    }

    public abstract boolean canBuy(List<CartItemDTO> cart, String username);

    public String getErrorDescription() {
        return errorDescription;
    }
}
