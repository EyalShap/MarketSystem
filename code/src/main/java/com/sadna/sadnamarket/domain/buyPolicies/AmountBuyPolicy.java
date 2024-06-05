package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.List;
import java.util.Map;

public class AmountBuyPolicy extends SimpleBuyPolicy {
    private int from;
    private int to; // if this is equal to -1 there is no limit

    AmountBuyPolicy(List<BuyType> buytypes, PolicySubject subject, int from, int to) throws Exception {
        super(buytypes, subject);
        if(to != -1 && to < from) {
            throw new Exception();
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        int amount = policySubject.subjectAmount(cart, products);
        if(to == -1)
            return amount >= from;
        return amount <= to && amount >= from;
    }
}
