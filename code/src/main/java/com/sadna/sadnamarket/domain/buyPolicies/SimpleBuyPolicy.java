package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.List;

public abstract class SimpleBuyPolicy extends BuyPolicy{
    protected PolicySubject policySubject;
    protected List<BuyType> buytypes;

    SimpleBuyPolicy(int id, List<BuyType> buytypes, PolicySubject policySubject) {
        super(id);
        this.buytypes = buytypes;
        this.policySubject = policySubject;
    }
}
