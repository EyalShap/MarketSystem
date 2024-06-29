package com.sadna.sadnamarket.domain.buyPolicies;


import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BuyPolicyDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "policy_id")
    Integer policyId;

    public abstract Query getUniqueQuery(Session session);
    public abstract BuyPolicy toBuyPolicy();
    public abstract boolean isComposite();
    public abstract int getId1();
    public abstract int getId2();
    public abstract BuyPolicy toBuyPolicy(BuyPolicy policy1, BuyPolicy policy2);
}
