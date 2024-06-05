package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.stores.Store;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface IBuyPolicyRepository {
    public BuyPolicy findBuyPolicyByID(int policyId) throws Exception;
    public Set<Integer> getAllPolicyIds();
    public int addProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max);
    public int addProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max) throws Exception;
    public int addCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max);
    public int addCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws Exception;
    public int addCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes);
    public int addCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes);
    public boolean buyPolicyExists(int policyId);
}
