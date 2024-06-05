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

    public CompositeBuyPolicy() {
    }

    public BuyPolicy getPolicy1() {
        return policy1;
    }

    public void setPolicy1(BuyPolicy policy1) {
        this.policy1 = policy1;
    }

    public BuyPolicy getPolicy2() {
        return policy2;
    }

    public void setPolicy2(BuyPolicy policy2) {
        this.policy2 = policy2;
    }
}
