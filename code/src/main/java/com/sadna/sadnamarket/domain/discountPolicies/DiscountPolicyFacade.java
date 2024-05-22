package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;

public class DiscountPolicyFacade {

    public int addDiscountPolicy() {
        //proxy function
        // assuming it returns the id of the new policy
        return 0;
    }

    public double calculatePrice(List<Integer> discountPolicyIds, List<CartItemDTO> cart, String username) {
        return 10;
    }
}
