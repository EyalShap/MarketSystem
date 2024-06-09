package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicy;

import java.util.HashMap;
import java.util.Map;

public class MemoryConditionRepository implements IConditionRespository{
    private Map<Integer, Condition> conditions;
    private Map<String, Integer> conditionsDesc;
    private int nextId;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public MemoryConditionRepository(){
        conditions = new HashMap<>();
        conditionsDesc = new HashMap<>();
        nextId = 0;
    }

    @Override
    public int createMinBuyCondition(int minBuy) throws JsonProcessingException{
        Condition newCondition = new MinBuyCondition(nextId, minBuy);
        return addConditionToMaps(newCondition);
    }

    @Override
    public int createMinProductCondition(int minAmount, String productName)throws JsonProcessingException {
        MinProductCondition newCondition = new MinProductCondition(nextId, minAmount);
        newCondition.setOnCategoryName(productName);
        return addConditionToMaps(newCondition);        }

    @Override
    public int createMinProductOnCategoryCondition(int minAmount, String categoryName)throws JsonProcessingException {
        MinProductCondition newCondition = new MinProductCondition(nextId, minAmount);
        newCondition.setOnCategoryName(categoryName);
        return addConditionToMaps(newCondition);
}

    @Override
    public int createMinProductOnCStoreCondition(int minAmount)throws JsonProcessingException {
        MinProductCondition newCondition = new MinProductCondition(nextId, minAmount);
        newCondition.setOnStore();
        return addConditionToMaps(newCondition);
}

    @Override
    public int createTrueCondition() throws JsonProcessingException {
        Condition newCondition = new TrueCondition(nextId);
        return addConditionToMaps(newCondition);
    }

    @Override
    public int createXorCondition(Condition conditionA, Condition conditionB)throws JsonProcessingException {
        Condition newCondition = new XorCondition(nextId, conditionA, conditionB);
        return addConditionToMaps(newCondition);
    }

    @Override
    public int createAndCondition(Condition conditionA, Condition conditionB)throws JsonProcessingException {
        Condition newCondition = new AndCondition(nextId, conditionA, conditionB);
        return addConditionToMaps(newCondition);
    }

    @Override
    public int createOrCondition(Condition conditionA, Condition conditionB) throws JsonProcessingException{
        Condition newCondition = new OrCondition(nextId, conditionA, conditionB);
        return addConditionToMaps(newCondition);
    }

    private int addConditionToMaps(Condition newCondition) throws JsonProcessingException {
        String newConditionDesc = newCondition.getClass().getName() + "-" + objectMapper.writeValueAsString(newCondition);
        if(!conditionsDesc.containsKey(newConditionDesc)) {
            conditions.put(nextId, newCondition);
            conditionsDesc.put(newConditionDesc, nextId);
            nextId++;
            return nextId - 1;
        }
        else {
            return conditionsDesc.get(newConditionDesc);
        }
    }
    public Condition findConditionByID(int condId) throws Exception {
        if(!conditionExists(condId)) {
            throw new Exception();
        }
        return conditions.get(condId);
    }
    public boolean conditionExists(int condId) {
        return conditions.containsKey(condId);
    }

}
