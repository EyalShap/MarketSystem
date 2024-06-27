package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@DiscriminatorValue("AGE_BUY_POLICY")
public class AgeLimitBuyPolicy extends RangeBuyPolicy{
    //private int minValue;
    //private int maxValue; // -1 if no limit

    AgeLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, int minAge, int maxAge) {
        super(id, buytypes, subject, minAge, maxAge);

        if((minAge == -1 && maxAge == -1) || minAge < -1 || maxAge < -1 || (maxAge != -1 && minAge > maxAge))
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("age limit", String.valueOf(minAge), String.valueOf(maxAge)));

        //this.from = minAge;
        //this.to = maxAge;
    }

    AgeLimitBuyPolicy(List<BuyType> buytypes, PolicySubject subject, int minAge, int maxAge) {
        super(buytypes, subject, minAge, maxAge);

        if((minAge == -1 && maxAge == -1) || minAge < -1 || maxAge < -1 || (maxAge != -1 && minAge > maxAge))
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("age limit", String.valueOf(minAge), String.valueOf(maxAge)));

        //this.from = minAge;
        //this.to = maxAge;
    }

    @Override
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        if(policySubject.get(0).subjectAmount(cart, products) > 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if(!((user != null) && isAgeInLimit(LocalDate.parse(user.getBirthDate(), formatter)))) {
                error.add(Error.makeAgeLimitBuyPolicyError(policySubject.get(0).getSubject(), minValue, maxValue));
            }
        }

        return error;
    }

    @Override
    protected boolean dependsOnUser() {
        return true;
    }

    @Override
    public String getPolicyDesc() {
        if(minValue == -1)
            return String.format("Buying %s is allowed only for users younger than %d.", policySubject.get(0).getDesc(), maxValue);
        if(maxValue == -1)
            return String.format("Buying %s is allowed only for users older than %d.", policySubject.get(0).getDesc(), minValue);
        return String.format("Buying %s is allowed only for users at ages %d - %d.", policySubject.get(0).getDesc(), minValue, maxValue);
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o))
            return false;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeLimitBuyPolicy that = (AgeLimitBuyPolicy) o;
        return minValue == that.minValue && maxValue == that.maxValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minValue, maxValue);
    }

    private boolean isAgeInLimit(LocalDate birthDate) {
        Period period = Period.between(birthDate, LocalDate.now());
        int age = period.getYears();
        if(maxValue == -1) {
            return age >= minValue;
        }
        return age <= maxValue && age >= minValue;
    }

    public int getMinAge() {
        return minValue;
    }

    public void setMinAge(int minAge) {
        this.minValue = minAge;
    }

    public int getMaxAge() {
        return maxValue;
    }

    public void setMaxAge(int maxAge) {
        this.maxValue = maxAge;
    }
}
