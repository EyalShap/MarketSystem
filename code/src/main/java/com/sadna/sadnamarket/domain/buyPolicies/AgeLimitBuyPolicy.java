package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class AgeLimitBuyPolicy extends SimpleBuyPolicy{
    private int minAge;
    private int maxAge; // -1 if no limit

    AgeLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int minAge, int maxAge) {
        super(id, buytypes, subject);

        if(minAge < -1 || maxAge < -1 || (maxAge != -1 && minAge > maxAge))
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("age limit", String.valueOf(minAge), String.valueOf(maxAge)));

        this.minAge = minAge;
        this.maxAge = maxAge;
        this.setErrorDescription(Error.makeAgeLimitBuyPolicyError(subject.getSubject(), minAge, maxAge));
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            return (user != null) && isAgeInLimit(LocalDate.parse(user.getBirthDate()));
        }
        
        return true;
    }

    @Override
    protected boolean dependsOnUser() {
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

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
