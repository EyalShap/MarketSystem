package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;

public abstract class CompositeBuyPolicy extends BuyPolicy{
    protected BuyPolicy policy1;
    protected BuyPolicy policy2;

    public CompositeBuyPolicy(int id, BuyPolicy policy1, BuyPolicy policy2) {
        super(id);
        this.policy1 = policy1;
        this.policy2 = policy2;
    }
}
