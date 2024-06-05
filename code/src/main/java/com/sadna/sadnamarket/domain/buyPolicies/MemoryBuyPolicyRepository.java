package com.sadna.sadnamarket.domain.buyPolicies;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryBuyPolicyRepository implements IBuyPolicyRepository{
    private Map<Integer, BuyPolicy> buyPolicies;
    private int nextId;

    public MemoryBuyPolicyRepository() {
        this.buyPolicies = new HashMap<>();
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
    public int addProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max) {
        BuyPolicy newPolicy =  new KgLimitBuyPolicy(nextId, buytypes, new ProductSubject(productId), min, max);
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public int addProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max) throws Exception {
        BuyPolicy newPolicy = new AmountBuyPolicy(nextId, buytypes, new ProductSubject(productId), min, max);
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public int addCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max) {
        BuyPolicy newPolicy = new AgeLimitBuyPolicy(nextId, buytypes, new CategorySubject(category), min, max);
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public int addCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws Exception {
        BuyPolicy newPolicy = new HourLimitBuyPolicy(nextId, buytypes, new CategorySubject(category), from, to);
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public int addCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes) {
        BuyPolicy newPolicy = new RoshChodeshBuyPolicy(nextId, buytypes, new CategorySubject(category));
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public int addCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes) {
        BuyPolicy newPolicy = new HolidayBuyPolicy(nextId, buytypes, new CategorySubject(category));
        buyPolicies.put(nextId, newPolicy);
        nextId++;
        return nextId - 1;
    }

    @Override
    public boolean buyPolicyExists(int policyId) {
        return buyPolicies.containsKey(policyId);
    }
}
