package com.sadna.sadnamarket.domain.buyPolicies;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RoshChodeshBuyPolicy extends SimpleBuyPolicy{
    RoshChodeshBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id, buytypes, subject);
        setErrorDescription(Error.makeRoshChodeshBuyPolicyError(subject.getSubject()));
    }

    public RoshChodeshBuyPolicy() {
    }

    public static boolean isRoshChodesh() {
        JewishCalendar jewishCalendar = new JewishCalendar(LocalDate.now());
        return !jewishCalendar.isRoshChodesh();
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            return !isRoshChodesh();
        }
        return false;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }
}
