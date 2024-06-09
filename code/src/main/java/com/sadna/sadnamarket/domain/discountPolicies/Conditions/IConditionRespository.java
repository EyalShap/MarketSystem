package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IConditionRespository {
    int createMinBuyCondition(int minBuy) throws JsonProcessingException;
    int createMinProductCondition(int minAmount, String productName) throws JsonProcessingException;
    int createMinProductOnCategoryCondition(int minAmount, String categoryName) throws JsonProcessingException;
    int createMinProductOnCStoreCondition(int minAmount) throws JsonProcessingException;
    int createTrueCondition()throws JsonProcessingException;
    int createXorCondition(Condition conditionA, Condition conditionB) throws JsonProcessingException;
    int createAndCondition(Condition conditionA, Condition conditionB) throws JsonProcessingException;
    int createOrCondition(Condition conditionA, Condition conditionB) throws JsonProcessingException;
}
