package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SimpleBuyPolicy extends BuyPolicy{
    protected PolicySubject policySubject;
    protected List<BuyType> buytypes;

    SimpleBuyPolicy(int id, List<BuyType> buytypes, PolicySubject policySubject) {
        super(id);
        this.buytypes = buytypes;
        this.policySubject = policySubject;
    }

    public SimpleBuyPolicy() {
    }

    public PolicySubject getPolicySubject() {
        return policySubject;
    }

    public void setPolicySubject(PolicySubject policySubject) {
        this.policySubject = policySubject;
    }

    public List<BuyType> getBuytypes() {
        return buytypes;
    }

    public void setBuytypes(List<BuyType> buytypes) {
        this.buytypes = buytypes;
    }

    public Set<Integer> getPolicyProductIds() {
        Set<Integer> ids = new HashSet<>();
        ids.add(policySubject.getProductId());
        return ids;
    }

}
