package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.List;

public abstract class SimpleBuyPolicy extends BuyPolicy{
    protected PolicySubject policySubject;

    SimpleBuyPolicy(int id, List<BuyType> buytypes, PolicySubject policySubject) {
        super(id, buytypes);
        this.policySubject = policySubject;
    }
}
