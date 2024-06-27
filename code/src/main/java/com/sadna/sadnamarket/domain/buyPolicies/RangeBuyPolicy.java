package com.sadna.sadnamarket.domain.buyPolicies;

import javax.persistence.*;
import java.util.List;

@Entity
public abstract class RangeBuyPolicy extends SimpleBuyPolicy{
    @Column(name = "min_value")
    protected int minValue;

    @Column(name = "max_value")
    protected int maxValue;

    public RangeBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int from, int to) {
        super(id, buytypes, subject);
        this.minValue = from;
        this.maxValue = to;
    }

    public RangeBuyPolicy(List<BuyType> buytypes, PolicySubject subject, int from, int to) {
        super(buytypes, subject);
        this.minValue = from;
        this.maxValue = to;
    }
}
