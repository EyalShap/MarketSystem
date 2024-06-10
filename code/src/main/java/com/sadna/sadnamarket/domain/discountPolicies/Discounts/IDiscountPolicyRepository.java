package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;

import java.util.Set;

public interface IDiscountPolicyRepository {
    public boolean discountPolicyExists(int policyId);
    public Set<Integer> getAllPolicyIds();
    public Discount findDiscountPolicyByID(int policyId) throws Exception;

    public int addMaximumDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
    public int addOrDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
    public int addOnCategorySimpleDiscount(double percentage,String categoryName, Condition condition) throws JsonProcessingException;
    public int addOnProductSimpleDiscount(double percentage,String productName, Condition condition) throws JsonProcessingException;
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException;
    public int addTakeMaxXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
    public int addTakeMinXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
    public int addAdditionDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
    public int addAndDiscount(Discount discountA, Discount discountB) throws JsonProcessingException;
}
