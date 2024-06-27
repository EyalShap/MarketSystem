package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonFilter("idFilter")
@Entity
@Table(name = "range_buy_policy")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "policy_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BuyPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "policy_id")
    private Integer id;

    BuyPolicy(int id) {
        this.id = id;
    }

    BuyPolicy() {
        this.id = null;
    }

    public abstract Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected abstract boolean dependsOnUser();

    public abstract String getPolicyDesc();

    public abstract Set<Integer> getPolicyProductIds();

    public abstract boolean equals(Object other);
}
