package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MemoryDiscountPolicyRepository implements IDiscountPolicyRepository{
    private Map<Integer, Discount> discountPolicies;
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
        Discount newDiscountPolicy = new MaximumDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOrDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new OrDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnCategorySimpleDiscount(double percentage, String categoryName, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnCategoryName(categoryName);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnProductSimpleDiscount(double percentage, String productName, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnProductName(productName);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnStore();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addTakeMaxXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(nextId, discountA, discountB);
        newDiscountPolicy.setMax();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addTakeMinXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(nextId, discountA, discountB);
        newDiscountPolicy.setMin();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addAdditionDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new AdditionDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addAndDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new AndDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    private int addDiscountPolicyToMaps(Discount newDiscountPolicy) throws JsonProcessingException {
        String newConditionDesc = newDiscountPolicy.getClass().getName() + "-" + objectMapper.writeValueAsString(newDiscountPolicy);
        if(!discountPoliciesDesc.containsKey(newConditionDesc)) {
            discountPolicies.put(nextId, newDiscountPolicy);
            discountPoliciesDesc.put(newConditionDesc, nextId);
            nextId++;
            return nextId - 1;
        }
        else {
            return discountPoliciesDesc.get(newConditionDesc);
        }
    }


    @Override
    public Set<Integer> getAllPolicyIds() {
        return discountPolicies.keySet();
    }

    public Discount findDiscountPolicyByID(int discountPolicyID) throws Exception {
        if(!discountPolicyExists(discountPolicyID)) {
            throw new Exception();
        }
        return discountPolicies.get(discountPolicyID);
    }
    public boolean discountPolicyExists(int discountPolicyID) {
        return discountPolicies.containsKey(discountPolicyID);
    }
}
