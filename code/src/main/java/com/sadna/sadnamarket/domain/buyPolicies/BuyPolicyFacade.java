package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountManager;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicy;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;

import java.time.LocalTime;
import java.util.*;

public class BuyPolicyFacade {
    private Map<Integer, BuyPolicyManager> mapper;
    private IBuyPolicyRepository buyPolicyRepository;
    private ProductFacade productFacade;
    private UserFacade userFacade;
    private StoreFacade storeFacade;

    public BuyPolicyFacade(IBuyPolicyRepository buyPolicyRepository) {
        this.mapper = new HashMap<Integer, BuyPolicyManager>();
        this.buyPolicyRepository = buyPolicyRepository;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setStoreFacade(StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public BuyPolicy getBuyPolicy(int policyId) throws Exception {
        return buyPolicyRepository.findBuyPolicyByID(policyId);
    }

    // only the ones that are in the requirements for now, we can add more if needed
    public int createProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max, String username) throws JsonProcessingException {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addProductKgBuyPolicy(productId, buytypes, min, max);
    }

    public int createProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addProductAmountBuyPolicy(productId, buytypes, min, max);
    }

    public int createCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addCategoryAgeLimitBuyPolicy(category, buytypes, min, max);
    }

    public int createCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addCategoryHourLimitBuyPolicy(category, buytypes, from, to);
    }

    public int createCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addCategoryRoshChodeshBuyPolicy(category, buytypes);
    }

    public int createCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        return buyPolicyRepository.addCategoryHolidayBuyPolicy(category, buytypes);
    }

    public int createAndBuyPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        BuyPolicy policy1 = buyPolicyRepository.findBuyPolicyByID(policyId1);
        BuyPolicy policy2 = buyPolicyRepository.findBuyPolicyByID(policyId2);
        return buyPolicyRepository.addAndBuyPolicy(policy1, policy2);
    }

    public int createOrBuyPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        BuyPolicy policy1 = buyPolicyRepository.findBuyPolicyByID(policyId1);
        BuyPolicy policy2 = buyPolicyRepository.findBuyPolicyByID(policyId2);
        return buyPolicyRepository.addOrBuyPolicy(policy1, policy2);
    }

    public int createConditioningBuyPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create buy policy", username));

        BuyPolicy policy1 = buyPolicyRepository.findBuyPolicyByID(policyId1);
        BuyPolicy policy2 = buyPolicyRepository.findBuyPolicyByID(policyId2);
        return buyPolicyRepository.addConditioningBuyPolicy(policy1, policy2);
    }

    public void addPolicyToStore(int storeId, int policyId) throws Exception {
        if(!buyPolicyRepository.buyPolicyExists(policyId))
            throw new Exception();
        BuyPolicyManager manager = mapper.getOrDefault(storeId, new BuyPolicyManager(this));
        manager.addBuyPolicy(policyId);
    }

    private boolean hasPermission(String username, Permission permission) {
        for(int storeId : storeFacade.getAllStoreIds())
            if(storeFacade.hasPermission(username, storeId, permission))
                return true;
        return false;
    }

    // --------------------------------------------------------------------------------

    public String canBuy(int storeId, List<CartItemDTO> cart, String username) throws Exception {
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        MemberDTO user = userFacade.isExist(username) ?  userFacade.getMemberDTO(username) : null;
        return buyPolicyManager.canBuy(cart, getProductDTOs(cart), user);
    }

    private Map<Integer, ProductDTO> getProductDTOs(List<CartItemDTO> cart) {
        Map<Integer, ProductDTO> res = new HashMap<>();
        for(CartItemDTO item : cart) {
            int productId = item.getProductId();
            res.put(productId, productFacade.getProductDTO(productId));
        }
        return res;
    }

}
