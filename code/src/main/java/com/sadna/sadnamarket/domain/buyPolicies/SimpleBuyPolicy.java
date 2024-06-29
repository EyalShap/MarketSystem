package com.sadna.sadnamarket.domain.buyPolicies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public abstract class SimpleBuyPolicy extends BuyPolicy{
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "policy_id")
    protected List<PolicySubject> policySubject;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "buy_policy_types", joinColumns = @JoinColumn(name = "policy_id"))
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "store_id", referencedColumnName = "store_id"),
            @MapKeyJoinColumn(name = "buy_type", referencedColumnName = "buy_type")
    })
    protected List<BuyType> buytypes;

    SimpleBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id);
        this.buytypes = buytypes;
        this.policySubject = new ArrayList<>();
        policySubject.add(subject);
    }

    SimpleBuyPolicy(List<BuyType> buytypes, PolicySubject subject) {
        super();
        this.buytypes = buytypes;
        this.policySubject = new ArrayList<>();
        policySubject.add(subject);
    }

    public SimpleBuyPolicy() {
    }

    public PolicySubject getPolicySubject() {
        return policySubject.get(0);
    }

    public void setPolicySubject(PolicySubject policySubject) {
        this.policySubject = List.of(policySubject);
    }

    public List<BuyType> getBuytypes() {
        return buytypes;
    }

    public void setBuytypes(List<BuyType> buytypes) {
        this.buytypes = buytypes;
    }

    public Set<Integer> getPolicyProductIds() {
        Set<Integer> ids = new HashSet<>();
        ids.add(policySubject.get(0).getProductId());
        return ids;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleBuyPolicy)) return false;
        SimpleBuyPolicy that = (SimpleBuyPolicy) o;
        return buytypes.equals(that.buytypes) && policySubject.equals(that.policySubject);
    }

}
