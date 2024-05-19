package com.sadna.sadnamarket.domain.stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private List<Integer> productIds;
    private int founderId;
    private List<Integer> ownerIds;
    private List<Integer> managerIds;
    private List<Integer> buyPolicyIds;
    private List<Integer> discountPolicyIds;

    public Store(int storeId, String storeName, int founderId) {
        this.storeId = storeId;
        this.isActive = true;
        this.storeName = storeName;
        this.productIds = new ArrayList<>();
        this.founderId = founderId;
        this.ownerIds = new ArrayList<>();
        this.ownerIds.add(founderId);
        this.managerIds = new ArrayList<>();
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

    public boolean getIsActive() {
        return this.isActive;
    }

    public void addProduct(int productId) {
        if(productExists(productId))
            throw new IllegalArgumentException(String.format("A product with id %d already exists.", productId));

        productIds.add(productId);
    }

    public void deleteProduct(int productId) {
        if(!productExists(productId))
            throw new IllegalArgumentException(String.format("A product with id %d does not exist.", productId));

        productIds.remove(productId);
    }

    public boolean productExists(int productId) {
        return productIds.contains(productId);
    }

    /*
    public void addStoreOwner(int currentOwnerId, int newOwnerId) {
        if(!isStoreOwner(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d can not add a new owner to store with id %d.", currentOwnerId, storeId));
        if(isStoreOwner(newOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already a owner of store with id %d.", newOwnerId, storeId));

        ownerIds.add(newOwnerId);
    }

    private boolean isStoreOwner(int userId) {
        return ownerIds.contains(userId);
    }

    public void addStoreManager(int currentOwnerId, int newManagerId) {
        if(!isStoreOwner(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d can not add a new owner to store with id %d.", currentOwnerId, storeId));
        if(isStoreManager(newManagerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already a manager of store with id %d.", newManagerId, storeId));

        managerIds.add(newManagerId);
    }

    private boolean isStoreManager(int userId) {
        return managerIds.contains(userId);
    }*/

    public void closeStore(int userId) {
        if(!this.isActive)
            throw new IllegalArgumentException(String.format("A store with id %d is already closed.", storeId));
        if(founderId != userId)
            throw new IllegalArgumentException(String.format("A user with id %d can not close the store with id %d (not a founder).", userId, storeId));
        this.isActive = false;
    }

}
