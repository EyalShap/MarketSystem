package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.Member;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;

public abstract class BuyPolicy {
    protected int id;
    protected String errorDescription;

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
}
