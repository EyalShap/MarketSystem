package com.sadna.sadnamarket.domain.stores;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private int storeId;
    private String storeName;
    private int founderId;
    private List<Integer> ownerIds;
    private List<Integer> buyPolicyIds;
    private List<Integer> discountPolicyIds;

    public Store(int storeId, String storeName, int founderId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.founderId = founderId;
        this.ownerIds = new ArrayList<>();
        this.ownerIds.add(founderId);
        this.buyPolicyIds = new ArrayList<>();
        this.buyPolicyIds.add(0); // assuming buyPolicyId = 0 is default policy
        this.discountPolicyIds = new ArrayList<>();
    }

    public int getStoreId() {
        return this.storeId;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public int getFounderId() {
        return this.founderId;
    }
}
