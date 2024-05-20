package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;
import java.util.Map;

@JsonFilter("filter")
public class StoreDTO {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private Map<Integer, Integer> productAmounts;
    private int founderId;
    private List<Integer> ownerIds;
    private List<Integer> managerIds;
    private List<Integer> sellerIds;
    private List<Integer> buyPolicyIds;
    private List<Integer> discountPolicyIds;
    private List<Integer> orderIds;

    public StoreDTO(int storeId, boolean isActive, String storeName, Map<Integer, Integer> productAmounts, int founderId, List<Integer> ownerIds, List<Integer> managerIds, List<Integer> sellerIds, List<Integer> buyPolicyIds, List<Integer> discountPolicyIds, List<Integer> orderIds) {
        this.storeId = storeId;
        this.isActive = isActive;
        this.storeName = storeName;
        this.productAmounts = productAmounts;
        this.founderId = founderId;
        this.ownerIds = ownerIds;
        this.managerIds = managerIds;
        this.sellerIds = sellerIds;
        this.buyPolicyIds = buyPolicyIds;
        this.discountPolicyIds = discountPolicyIds;
        this.orderIds = orderIds;
    }

    public int getStoreId() {
        return this.storeId;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return this.productAmounts;
    }

    public int getFounderId() {
        return this.founderId;
    }

    public List<Integer> getOwnerIds() {
        return this.ownerIds;
    }

    public List<Integer> getManagerIds() {
        return this.managerIds;
    }

    public List<Integer> getSellerIds() {
        return this.sellerIds;
    }

    public List<Integer> getBuyPolicyIds() {
        return this.buyPolicyIds;
    }

    public List<Integer> getDiscountPolicyIds() {
        return this.discountPolicyIds;
    }

    public List<Integer> getOrderIds() {
        return this.orderIds;
    }
}
