package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;

import java.util.Set;

public interface IDiscountPolicyRepository {
    public boolean discountPolicyExists(int policyId);
    public Set<Integer> getAllPolicyIds();
    public DiscountPolicy findDiscountPolicyByID(int policyId) throws Exception;

    public int addMaximumDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
    public int addOrDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
    public int addOnCategorySimpleDiscount(double percentage,String categoryName, Condition condition) throws JsonProcessingException;
    public int addOnProductSimpleDiscount(double percentage,int productID, Condition condition) throws JsonProcessingException;
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException;
    public int addTakeMaxXorDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
    public int addTakeMinXorDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
    public int addAdditionDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
    public int addAndDiscount(DiscountPolicy discountA, DiscountPolicy discountB) throws JsonProcessingException;
}
