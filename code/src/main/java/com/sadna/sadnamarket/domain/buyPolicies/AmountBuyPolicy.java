package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.*;

@Entity
@DiscriminatorValue("AMOUNT_BUY_POLICY")
public class AmountBuyPolicy extends RangeBuyPolicy {
    //private int minValue;
    //private int maxValue; // if this is equal to -1 there is no limit

    AmountBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int from, int to) {
        super(id, buytypes, subject, from, to);
        if((from == -1 && to == -1) || from < -1 || to < -1 || (to != -1 && to < from)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("amount", String.valueOf(from), String.valueOf(to)));
        }
        //this.from = from;
        //this.to = to;
    }

    AmountBuyPolicy(List<BuyType> buytypes, PolicySubject subject, int from, int to) {
        super(buytypes, subject, from, to);
        if((from == -1 && to == -1) || from < -1 || to < -1 || (to != -1 && to < from)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("amount", String.valueOf(from), String.valueOf(to)));
        }
        //this.from = from;
        //this.to = to;
    }

    @Override
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        int amount = policySubject.get(0).subjectAmount(cart, products);
        if(maxValue == -1) {
            if(amount < minValue) {
                error.add(Error.makeAmountBuyPolicyError(policySubject.get(0).getSubject(), minValue, maxValue));
                return error;
            }
        }
        else if(!(amount <= maxValue && amount >= minValue)) {
            error.add(Error.makeAmountBuyPolicyError(policySubject.get(0).getSubject(), minValue, maxValue));
            return error;
        }
        return error;
    }

    public int getFrom() {
        return minValue;
    }

    public void setFrom(int from) {
        this.minValue = from;
    }

    public int getTo() {
        return maxValue;
    }

    public void setTo(int to) {
        this.maxValue = to;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        if(maxValue == -1)
            return String.format("More than %d units of %s must be bought.", minValue, policySubject.get(0).getDesc());
        if(minValue == -1)
            return String.format("Less than %d units of %s must be bought.", maxValue, policySubject.get(0).getDesc());
        return String.format("%d - %d units of %s must be bought.", minValue, maxValue, policySubject.get(0).getDesc());
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o))
            return false;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountBuyPolicy that = (AmountBuyPolicy) o;
        return minValue == that.minValue && maxValue == that.maxValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minValue, maxValue);
    }
}
