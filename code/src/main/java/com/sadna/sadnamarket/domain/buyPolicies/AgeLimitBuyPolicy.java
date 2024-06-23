package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AgeLimitBuyPolicy extends SimpleBuyPolicy{
    private int minAge;
    private int maxAge; // -1 if no limit

    AgeLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int minAge, int maxAge) {
        super(id, buytypes, subject);

        if((minAge == -1 && maxAge == -1) || minAge < -1 || maxAge < -1 || (maxAge != -1 && minAge > maxAge))
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("age limit", String.valueOf(minAge), String.valueOf(maxAge)));

        this.minAge = minAge;
        this.maxAge = maxAge;
        this.setErrorDescription(Error.makeAgeLimitBuyPolicyError(subject.getSubject(), minAge, maxAge));
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return (user != null) && isAgeInLimit(LocalDate.parse(user.getBirthDate(), formatter));
        }
        
        return true;
    }

    @Override
    protected boolean dependsOnUser() {
        return true;
    }

    @Override
    public String getPolicyDesc() {
        if(minAge == -1)
            return String.format("Buying %s is allowed only for users younger than %d.", policySubject.getDesc(), maxAge);
        if(maxAge == -1)
            return String.format("Buying %s is allowed only for users older than %d.", policySubject.getDesc(), minAge);
        return String.format("Buying %s is allowed only for users at ages %d - %d.", policySubject.getDesc(), minAge, maxAge);
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o))
            return false;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeLimitBuyPolicy that = (AgeLimitBuyPolicy) o;
        return minAge == that.minAge && maxAge == that.maxAge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minAge, maxAge);
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
