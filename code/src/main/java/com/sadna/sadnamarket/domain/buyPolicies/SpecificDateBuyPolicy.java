package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SpecificDateBuyPolicy extends SimpleBuyPolicy{
    private int day;
    private int month;
    private int year;

    SpecificDateBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int day, int month, int year) throws Exception {
        super(id, buytypes, subject);
        if(day != -1 && (day < 1 || day > 31)) {
            throw new Exception();
        }
        if(month != -1 && (month < 1 || month > 12)) {
            throw new Exception();
        }
        if(year != -1 && year < LocalDate.now().getYear()) {
            throw new Exception();
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            LocalDate now = LocalDate.now();
            if(day != -1 && now.getDayOfMonth() != day)
                return false;
            if(month != -1 && now.getMonthValue() != month)
                return false;
            if(year != -1 && now.getYear() != year)
                return false;
        }
        return true;
    }
}
