package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.IConditionRespository;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MemoryConditionRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.Discount;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.IDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.MemoryDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyFacade {
    private Map<Integer, DiscountPolicyManager> mapper;
    private IConditionRespository conditionRepository;
    private IDiscountPolicyRepository discountPolicyRepository;
    private ProductFacade productFacade;
    private StoreFacade storeFacade;

    private static DiscountPolicyFacade instance;

    public DiscountPolicyFacade(IConditionRespository conditionRepository, IDiscountPolicyRepository discountPolicyRepository) {
        this.mapper = new HashMap<Integer, DiscountPolicyManager>();
        this.conditionRepository = conditionRepository;
        this.discountPolicyRepository = discountPolicyRepository;

    }
    public void setStoreFacade(StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public Discount getDiscountPolicy(int policyId) throws Exception {
        return discountPolicyRepository.findDiscountPolicyByID(policyId);
    }
    public static DiscountPolicyFacade getInstance() {
        if (instance == null) {
            instance = new DiscountPolicyFacade(new MemoryConditionRepository(), new MemoryDiscountPolicyRepository());
        }
        return instance;
    }

    // ---DiscountsPolicy-Creations---------------------------------------------------------------
    public int createAdditionDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addAdditionDiscount(policy1, policy2);
    }

    public int createOrDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addOrDiscount(policy1, policy2);
    }

    public int createAndDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addAndDiscount(policy1, policy2);
    }

    public int createMaximumDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addMaximumDiscount(policy1, policy2);
    }

    public int createTakeMaxXorDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addTakeMaxXorDiscount(policy1, policy2);
    }

    public int createTakeMinXorDiscountPolicy(int policyId1, int policyId2, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Discount policy1 = discountPolicyRepository.findDiscountPolicyByID(policyId1);
        Discount policy2 = discountPolicyRepository.findDiscountPolicyByID(policyId2);
        return discountPolicyRepository.addTakeMinXorDiscount(policy1, policy2);
    }

    public int createOnCategorySimpleDiscountPolicy(double percentage, String categoryName, int ConditionID, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition condition = conditionRepository.findConditionByID(ConditionID);
        return discountPolicyRepository.addOnCategorySimpleDiscount(percentage, categoryName, condition);
    }

    public int createOnProductSimpleDiscountPolicy(double percentage, String productName, int ConditionID, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition condition = conditionRepository.findConditionByID(ConditionID);
        return discountPolicyRepository.addOnProductSimpleDiscount(percentage, productName, condition);
    }

    public int createOnStoreSimpleDiscountPolicy(double percentage, int ConditionID, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition condition = conditionRepository.findConditionByID(ConditionID);
        return discountPolicyRepository.addOnStoreSimpleDiscount(percentage, condition);
    }
    // ---Conditions-Creations---------------------------------------------------------------
    public int createMinBuyCondition(int minBuy, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        return conditionRepository.createMinBuyCondition(minBuy);
    }

    public int createTrueCondition(String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        return conditionRepository.createTrueCondition();
    }

    public int createMinProductCondition(int minAmount, String productName, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        return conditionRepository.createMinProductCondition(minAmount, productName);
    }

    public int createMinProductOnCategoryCondition(int minAmount, String categoryName, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        return conditionRepository.createMinProductOnCategoryCondition(minAmount, categoryName);
    }
    public int createMinProductOnStoreCondition(int minAmount, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        return conditionRepository.createMinProductOnStoreCondition(minAmount);
    }
    public int createXorCondition(int conditionAId, int conditionBId, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition conditionA = conditionRepository.findConditionByID(conditionAId);
        Condition conditionB = conditionRepository.findConditionByID(conditionBId);
        return conditionRepository.createXorCondition(conditionA, conditionB);
    }
    public int createOrCondition(int conditionAId, int conditionBId, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition conditionA = conditionRepository.findConditionByID(conditionAId);
        Condition conditionB = conditionRepository.findConditionByID(conditionBId);
        return conditionRepository.createOrCondition(conditionA, conditionB);
    }
    public int createAndCondition(int conditionAId, int conditionBId, String username) throws Exception {
        if (!hasPermission(username, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not create discount policy", username));

        Condition conditionA = conditionRepository.findConditionByID(conditionAId);
        Condition conditionB = conditionRepository.findConditionByID(conditionBId);
        return conditionRepository.createAndCondition(conditionA, conditionB);
    }

    // --------------------------------------------------------------------------------
    public void addDiscountPolicyToStore(int storeId, int policyId) throws Exception {
        if(!discountPolicyRepository.discountPolicyExists(policyId))
            throw new Exception();
        if(!mapper.containsKey(storeId))
            mapper.put(storeId, new DiscountPolicyManager(this));
        DiscountPolicyManager manager = mapper.get(storeId);
        manager.addDiscountPolicy(policyId);
        Discount discount = discountPolicyRepository.findDiscountPolicyByID(policyId);
        //if the discount is composite it will return the discountsAB IDs and remove them
        // (if not composit it will return -1 thus won't remove anything
        manager.removeDiscountPolicy(discount.getDiscountAID());
        manager.removeDiscountPolicy(discount.getDiscountBID());
    }
    public void removeDiscountPolicyFromStore(int storeId, int policyId) throws Exception{
        if(!discountPolicyRepository.discountPolicyExists(policyId))
            throw new Exception();
        if(!mapper.containsKey(storeId))
            mapper.put(storeId, new DiscountPolicyManager(this));
        DiscountPolicyManager manager = mapper.get(storeId);
        manager.removeDiscountPolicy(policyId);
    }



    public List<ProductDataPrice> calculatePrice(int storeId, List<CartItemDTO> cart) throws Exception {
        int itemID;
        DiscountPolicyManager discountManager = mapper.get(storeId);
        Map<Integer, ProductDTO> productDTOMap = new HashMap<>();
        // get ProductDTOs
        for(CartItemDTO cartItemDTO : cart){
            itemID = cartItemDTO.getProductId();
            productDTOMap.put(itemID, productFacade.getProductDTO(itemID));
        }
        return discountManager.giveDiscount(cart, productDTOMap);
    }

    private boolean hasPermission(String username, Permission permission) {
        for(int storeId : storeFacade.getAllStoreIds())
            if(storeFacade.hasPermission(username, storeId, permission))
                return true;
        return false;
    }
}
