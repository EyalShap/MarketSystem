package com.sadna.sadnamarket.domain.stores;

import java.util.List;

public class StoreDTO {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private List<Integer> productIds;
    private int founderId;
    private List<Integer> ownerIds;
    private List<Integer> managerIds;
    private List<Integer> sellerIds;
    private List<Integer> buyPolicyIds;
    private List<Integer> discountPolicyIds;

    public StoreDTO(int storeId, boolean isActive, String storeName, List<Integer> productIds, int founderId, List<Integer> ownerIds, List<Integer> managerIds, List<Integer> sellerIds, List<Integer> buyPolicyIds, List<Integer> discountPolicyIds) {
        this.storeId = storeId;
        this.isActive = isActive;
        this.storeName = storeName;
        this.productIds = productIds;
        this.founderId = founderId;
        this.ownerIds = ownerIds;
        this.managerIds = managerIds;
        this.sellerIds = sellerIds;
        this.buyPolicyIds = buyPolicyIds;
        this.discountPolicyIds = discountPolicyIds;
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

    public List<Integer> getProductIds() {
        return this.productIds;
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
}