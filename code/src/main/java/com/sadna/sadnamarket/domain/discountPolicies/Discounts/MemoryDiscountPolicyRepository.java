package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicy;

import java.util.HashMap;
import java.util.Map;

public class MemoryDiscountPolicyRepository implements IDiscountPolicyRepository{
    private Map<Integer, DiscountPolicy> discountPolicies;
    private Map<String, Integer> discountPoliciesDesc;
    private int nextId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MemoryDiscountPolicyRepository(){
        discountPolicies = new HashMap<>();
        discountPoliciesDesc = new HashMap<>();
        nextId = 0;
    }
    @Override
    public int addMaximumDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addOrDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addOnCategorySimpleDiscount(double percentage, String categoryName, Condition condition) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addOnProductSimpleDiscount(double percentage, String productName, Condition condition) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addTakeMaxXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addTakeMinXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addAdditionDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addAndDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        return 0;
    }
}
