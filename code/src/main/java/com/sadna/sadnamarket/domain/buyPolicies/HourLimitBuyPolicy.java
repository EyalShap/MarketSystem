package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.Clock;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HourLimitBuyPolicy extends SimpleBuyPolicy{
    private LocalTime from;
    private LocalTime to; // null if no limit

    HourLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, LocalTime from, LocalTime to) {
        super(id, buytypes, subject);
        if(to == null)
            to = LocalTime.of(23, 59, 59);
        if(from == null)
            from = LocalTime.of(0, 0);
        if(to.isBefore(from)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("hour limit", from.toString(), to.toString()));
        }
        this.from = from;
        this.to = to;
        setErrorDescription(Error.makeHourLimitBuyPolicyError(subject.getSubject(), from, to));
    }

    public HourLimitBuyPolicy() {
    }


    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            return isTimeInLimit();
        }
        return true;
    }

    public static LocalTime getCurrTime() {
        return LocalTime.now();
    }

    private boolean isTimeInLimit() {
        LocalTime now = getCurrTime();
        return now.isBefore(to) && now.isAfter(from);
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        if(from == null)
            return String.format("%s can only be bought before %s.", policySubject.getDesc(), to.toString());
        if(to == null)
            return String.format("%s can only be bought after %s.", policySubject.getDesc(), from.toString());
        return String.format("%s can only be bought at %s - %s.", policySubject.getDesc(), from.toString(), to.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HourLimitBuyPolicy buyPolicy = (HourLimitBuyPolicy) o;
        return Objects.equals(from, buyPolicy.from) && Objects.equals(to, buyPolicy.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
