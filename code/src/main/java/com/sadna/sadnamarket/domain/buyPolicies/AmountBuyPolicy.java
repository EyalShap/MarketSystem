package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AmountBuyPolicy extends SimpleBuyPolicy {
    private int from;
    private int to; // if this is equal to -1 there is no limit

    AmountBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int from, int to) {
        super(id, buytypes, subject);
        if((from == -1 && to == -1) || from < -1 || to < -1 || (to != -1 && to < from)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("amount", String.valueOf(from), String.valueOf(to)));
        }
        this.from = from;
        this.to = to;
        this.setErrorDescription(Error.makeAmountBuyPolicyError(subject.getSubject(), from, to));
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        int amount = policySubject.subjectAmount(cart, products);
        if(to == -1)
            return amount >= from;
        return amount <= to && amount >= from;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        if(to == -1)
            return String.format("More than %d units of %s must be bought.", from, policySubject.getDesc());
        if(from == -1)
            return String.format("Less than %d units of %s must be bought.", to, policySubject.getDesc());
        return String.format("%d - %d units of %s must be bought.", from, to, policySubject.getDesc());
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o))
            return false;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountBuyPolicy that = (AmountBuyPolicy) o;
        return from == that.from && to == that.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
