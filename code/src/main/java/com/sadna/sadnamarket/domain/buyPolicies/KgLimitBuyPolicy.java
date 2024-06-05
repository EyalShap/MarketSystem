package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KgLimitBuyPolicy extends SimpleBuyPolicy{
    private double minKg;
    private double maxKg; // -1 for no limit

    KgLimitBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject, double minKg, double maxKg) {
        super(id, buytypes, subject);
        this.minKg = minKg;
        this.maxKg = maxKg;
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
}
