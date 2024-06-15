package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public int addMaximumDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        DiscountPolicy newDiscountPolicy = new MaximumDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOrDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        DiscountPolicy newDiscountPolicy = new OrDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnCategorySimpleDiscount(double percentage, String categoryName, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnCategoryName(categoryName);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnProductSimpleDiscount(double percentage, int productID, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnProductID(productID);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException {
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(nextId, percentage, condition);
        newDiscountPolicy.setOnStore();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addTakeMaxXorDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(nextId, discountA, discountB);
        newDiscountPolicy.setMax();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addTakeMinXorDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(nextId, discountA, discountB);
        newDiscountPolicy.setMin();
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addAdditionDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        DiscountPolicy newDiscountPolicy = new AdditionDiscountPolicy(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    @Override
    public int addAndDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException {
        DiscountPolicy newDiscountPolicy = new AndDiscount(nextId, discountA, discountB);
        return addDiscountPolicyToMaps(newDiscountPolicy);
    }

    private int addDiscountPolicyToMaps(DiscountPolicy newDiscountPolicy) throws JsonProcessingException {
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

    public DiscountPolicy findDiscountPolicyByID(int discountPolicyID) throws Exception {
        if(!discountPolicyExists(discountPolicyID)) {
            throw new Exception();
        }
        return discountPolicies.get(discountPolicyID);
    }
    public boolean discountPolicyExists(int discountPolicyID) {
        return discountPolicies.containsKey(discountPolicyID);
    }
}
