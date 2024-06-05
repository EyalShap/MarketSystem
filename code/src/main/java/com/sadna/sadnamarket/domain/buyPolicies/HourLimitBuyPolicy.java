package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class HourLimitBuyPolicy extends SimpleBuyPolicy{
    private LocalTime from;
    private LocalTime to; // null if no limit

    HourLimitBuyPolicy(List<BuyType> buytypes, PolicySubject subject, LocalTime from, LocalTime to) throws Exception {
        super(buytypes, subject);
        if(to.isBefore(from)) {
            throw new Exception();
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            return isTimeInLimit();
        }
        return true;
    }

    private boolean isTimeInLimit() {
        LocalTime now = LocalTime.now();
        if(to == null) {
            return now.isAfter(from);
        }
        return now.isBefore(to) && now.isAfter(from);
    }
}
