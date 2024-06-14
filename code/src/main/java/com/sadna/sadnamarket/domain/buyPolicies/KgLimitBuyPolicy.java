package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KgLimitBuyPolicy extends SimpleBuyPolicy{
    private double minKg;
    private double maxKg; // -1 for no limit

    KgLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, double minKg, double maxKg) {
        super(id, buytypes, subject);

        if(minKg < -1 || maxKg < -1 || (maxKg != -1 && minKg > maxKg))
            throw new IllegalArgumentException(Error.makeBuyPolicyParamsError("kg limit", String.format("%.0f", minKg).trim(), String.format("%.0f", maxKg).trim()));

        this.minKg = minKg;
        this.maxKg = maxKg;
        setErrorDescription(Error.makeKgLimitBuyPolicyError(subject.getSubject(), String.valueOf(minKg), String.valueOf(maxKg)));
    }

    public KgLimitBuyPolicy() {
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        double totalWeight = 0;
        for(CartItemDTO item : cart) {
            int productId = item.getProductId();
            ProductDTO product = products.get(productId);
            if(policySubject.isSubject(product)) {
                totalWeight += product.getProductWeight() * item.getAmount();
            }
        }
        if(maxKg == -1) {
            return totalWeight >= minKg;
        }
        return totalWeight >= minKg && totalWeight <= maxKg;
    }

    public double getMinKg() {
        return minKg;
    }

    public void setMinKg(double minKg) {
        this.minKg = minKg;
    }

    public double getMaxKg() {
        return maxKg;
    }

    public void setMaxKg(double maxKg) {
        this.maxKg = maxKg;
    }

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        if(minKg == -1)
            return String.format("More than %f Kg of %s must be bought.", minKg, policySubject.getDesc());
        if(maxKg == -1)
            return String.format("You can not buy more than %f Kg of %s.", maxKg, policySubject.getDesc());
        return String.format("%f - %f Kg of %s must be bought.", minKg, maxKg, policySubject.getDesc());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        KgLimitBuyPolicy that = (KgLimitBuyPolicy) o;
        return Double.compare(that.minKg, minKg) == 0 && Double.compare(that.maxKg, maxKg) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minKg, maxKg);
    }
}
