package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalTime;
import java.util.*;

public class HourLimitBuyPolicy extends RangeBuyPolicy{
    //private LocalTime minValue;
    //private LocalTime maxValue; // null if no limit

    HourLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, LocalTime from, LocalTime to) {
        super(id, buytypes, subject, (from == null) ? 0 : from.getHour() * 60 + from.getMinute(), (to == null) ? 1439 : to.getHour() * 60 + to.getMinute());
        if(to == null)
            to = LocalTime.of(23, 59, 59);
        if(from == null)
            from = LocalTime.of(0, 0);
        if(to.isBefore(from)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("hour limit", from.toString(), to.toString()));
        }
        //this.minValue = from;
        //this.maxValue = to;
    }

    private int getTime(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    private LocalTime getTime(int time) {
        return LocalTime.of(time / 60, time % 60);
    }

    @Override
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        if(policySubject.get(0).subjectAmount(cart, products) > 0) {
            if(!isTimeInLimit()) {
                LocalTime fromTime = getTime(minValue);
                LocalTime toTime = getTime(maxValue);
                error.add(Error.makeHourLimitBuyPolicyError(policySubject.get(0).getSubject(), fromTime, toTime));
            }
        }
        return error;
    }

    public static LocalTime getCurrTime() {
        return LocalTime.now();
    }

    private boolean isTimeInLimit() {
        LocalTime now = getCurrTime();
        return now.isBefore(getTime(maxValue)) && now.isAfter(getTime(minValue));
    }

    public LocalTime getFrom() {
        return getTime(minValue);
    }

    public void setFrom(LocalTime from) {
        this.minValue = getTime(from);
    }

    public LocalTime getTo() {
        return getTime(maxValue);
    }

    public void setTo(LocalTime to) {
        this.maxValue = getTime(to);
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        /*if(minValue == null)
            return String.format("%s can only be bought before %s.", policySubject.get(0).getDesc(), maxValue.toString());
        if(maxValue == null)
            return String.format("%s can only be bought after %s.", policySubject.get(0).getDesc(), minValue.toString());
        */
        return String.format("%s can only be bought at %s - %s.", policySubject.get(0).getDesc(), getTime(minValue).toString(), getTime(maxValue).toString());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HourLimitBuyPolicy buyPolicy = (HourLimitBuyPolicy) o;
        return Objects.equals(minValue, buyPolicy.minValue) && Objects.equals(maxValue, buyPolicy.maxValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minValue, maxValue);
    }
}
