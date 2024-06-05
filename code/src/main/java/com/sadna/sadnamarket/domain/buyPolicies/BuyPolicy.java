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

    public abstract boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user);

    public String getErrorDescription() {
        return errorDescription;
    }

}
