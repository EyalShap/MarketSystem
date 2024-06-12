package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class SpecificDateBuyPolicy extends SimpleBuyPolicy{
    private int day;
    private int month;
    private int year;

    SpecificDateBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int day, int month, int year) {
        super(id, buytypes, subject);
        if(day != -1 && (day < 1 || day > 31)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("specific date", "day: " + day));
        }
        if(month != -1 && (month < 1 || month > 12)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("specific date", "month: " + month));
        }
        if(year != -1 && year < LocalDate.now().getYear()) {
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("specific date", "year: " + year));
        }
        this.day = day;
        this.month = month;
        this.year = year;
        setErrorDescription(Error.makeSpecificDateBuyPolicyError(subject.getSubject()));
    }

    public SpecificDateBuyPolicy() {
    }

    public static LocalDate getCurrDate() {
        return LocalDate.now();
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            LocalDate now = getCurrDate();
            if(day != -1 && now.getDayOfMonth() != day)
                return true;
            if(month != -1 && now.getMonthValue() != month)
                return true;
            if(year != -1 && now.getYear() != year)
                return true;
            return false;
        }
        return true;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }
}
