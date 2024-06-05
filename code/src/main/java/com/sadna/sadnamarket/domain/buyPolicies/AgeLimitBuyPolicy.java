package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class AgeLimitBuyPolicy extends SimpleBuyPolicy{
    private int minAge;
    private int maxAge; // -1 if no limit

    AgeLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int minAge, int maxAge) {
        super(id, buytypes, subject);
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(user == null) {
            return false; // We do not know the age of a guest.
        }
        if(policySubject.subjectAmount(cart, products) > 0) {
            return isAgeInLimit(user.getBirthDate());
        }
        return true;
    }

    private boolean isAgeInLimit(LocalDate birthDate) {
        Period period = Period.between(birthDate, LocalDate.now());
        int age = period.getYears();
        if(maxAge == -1) {
            return age >= minAge;
        }
        return age <= maxAge && age >= minAge;
    }
}
