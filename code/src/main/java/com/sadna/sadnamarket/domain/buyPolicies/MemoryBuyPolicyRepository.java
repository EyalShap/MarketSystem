package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryBuyPolicyRepository implements IBuyPolicyRepository{
    private Map<Integer, BuyPolicy> buyPolicies;
    private Map<String, Integer> buyPoliciesDesc;
    private int nextId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MemoryBuyPolicyRepository() {
        this.buyPolicies = new HashMap<>();
        this.buyPoliciesDesc = new HashMap<>();
        this.nextId = 0;
    }

    @Override
    public BuyPolicy findBuyPolicyByID(int policyId) throws Exception {
        if(!buyPolicyExists(policyId))
            throw new Exception();

        return buyPolicies.get(policyId);
    }

    @Override
    public Set<Integer> getAllPolicyIds() {
        return buyPolicies.keySet();
    }

    @Override
    public int addProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max) throws JsonProcessingException {
        BuyPolicy newPolicy =  new KgLimitBuyPolicy(nextId, buytypes, new ProductSubject(productId), min, max);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max) throws Exception {
        BuyPolicy newPolicy = new AmountBuyPolicy(nextId, buytypes, new ProductSubject(productId), min, max);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max) throws JsonProcessingException {
        BuyPolicy newPolicy = new AgeLimitBuyPolicy(nextId, buytypes, new CategorySubject(category), min, max);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws Exception {
        BuyPolicy newPolicy = new HourLimitBuyPolicy(nextId, buytypes, new CategorySubject(category), from, to);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        BuyPolicy newPolicy = new RoshChodeshBuyPolicy(nextId, buytypes, new CategorySubject(category));
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        BuyPolicy newPolicy = new HolidayBuyPolicy(nextId, buytypes, new CategorySubject(category));
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addAndBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        BuyPolicy newPolicy = new AndBuyPolicy(nextId, policy1, policy2);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addOrBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        BuyPolicy newPolicy = new OrBuyPolicy(nextId, policy1, policy2);
        return addPolicyToMaps(newPolicy);
    }

    @Override
    public int addConditioningBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        BuyPolicy newPolicy = new ConditioningBuyPolicy(nextId, policy1, policy2);
        return addPolicyToMaps(newPolicy);
    }

    private int addPolicyToMaps(BuyPolicy newPolicy) throws JsonProcessingException {
        String policyDesc = objectMapper.writeValueAsString(newPolicy.toString());
        if(!buyPoliciesDesc.containsKey(policyDesc)) {
            buyPolicies.put(nextId, newPolicy);
            buyPoliciesDesc.put(policyDesc, nextId);
            nextId++;
            return nextId - 1;
        }
        else {
            return buyPoliciesDesc.get(policyDesc);
        }
    }

    @Override
    public boolean buyPolicyExists(int policyId) {
        return buyPolicies.containsKey(policyId);
    }
}
