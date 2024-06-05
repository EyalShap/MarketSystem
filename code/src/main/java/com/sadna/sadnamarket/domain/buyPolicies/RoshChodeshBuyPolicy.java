package com.sadna.sadnamarket.domain.buyPolicies;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RoshChodeshBuyPolicy extends SimpleBuyPolicy{
    RoshChodeshBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id, buytypes, subject);
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            JewishCalendar jewishCalendar = new JewishCalendar(LocalDate.now());
            return !jewishCalendar.isRoshChodesh();
        }
        return false;
    }
}
