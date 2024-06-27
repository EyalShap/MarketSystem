package com.sadna.sadnamarket.domain.buyPolicies;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoshChodeshBuyPolicy extends SimpleBuyPolicy{
    RoshChodeshBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id, buytypes, subject);
    }

    public RoshChodeshBuyPolicy() {
    }

    public static boolean isRoshChodesh() {
        JewishCalendar jewishCalendar = new JewishCalendar(LocalDate.now());
        return !jewishCalendar.isRoshChodesh();
    }

    @Override
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        if(policySubject.get(0).subjectAmount(cart, products) > 0) {
            if(isRoshChodesh()) {
                error.add(Error.makeRoshChodeshBuyPolicyError(policySubject.get(0).getSubject()));
            }
        }
        return error;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        return String.format("%s can not be bought on Rosh Chodesh.", policySubject.get(0).getDesc());
    }
}
