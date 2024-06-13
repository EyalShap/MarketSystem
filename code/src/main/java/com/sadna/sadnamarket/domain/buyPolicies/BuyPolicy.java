package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonFilter("idFilter")
public abstract class BuyPolicy {
    private int id;
    private String errorDescription;

    BuyPolicy(int id) {
        this.id = id;
        this.errorDescription = null;
    }

    public BuyPolicy() {
    }

    public abstract boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user);

    public int getId() {
        return id;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    protected abstract boolean dependsOnUser();

    public abstract String getPolicyDesc();

    public abstract Set<Integer> getPolicyProductIds();
}
