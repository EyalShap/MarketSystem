package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.*;

public class Store {
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

    public Store(int storeId, String storeName, int founderId) {
        this.storeId = storeId;
        this.isActive = true;
        this.storeName = storeName;
        this.productAmounts = new HashMap<>();
        this.founderId = founderId;
        this.ownerIds = new ArrayList<>();
        this.ownerIds.add(founderId);
        this.managerIds = new ArrayList<>();
        this.sellerIds = new ArrayList<>();
        this.buyPolicyIds = new ArrayList<>();
        this.buyPolicyIds.add(0); // assuming buyPolicyId = 0 is default policy
        this.discountPolicyIds = new ArrayList<>();
        this.orderIds = new ArrayList<>();
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

    public List<Integer> getOwnerIds() {
        return this.ownerIds;
    }

    public List<Integer> getManagerIds() {
        return this.managerIds;
    }

    public List<Integer> getSellerIds() {
        return this.sellerIds;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return this.productAmounts;
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

    public void addProduct(int productId, int amount) {
        if(productExists(productId))
            throw new IllegalArgumentException(String.format("A product with id %d already exists.", productId));
        if(amount < 0)
            throw new IllegalArgumentException(String.format("%d is an illegal amount of products."));

        productAmounts.put(productId, amount);
    }

    public void deleteProduct(int productId) {
        if(!productExists(productId))
            throw new IllegalArgumentException(String.format("A product with id %d does not exist.", productId));

        productAmounts.remove(productId);
    }

    public void setProductAmounts(int productId, int newAmount) {
        if(!productExists(productId))
            throw new IllegalArgumentException(String.format("A product with id %d does not exist.", productId));
        if(newAmount < 0)
            throw new IllegalArgumentException(String.format("%d is an illegal amount of products."));

        productAmounts.put(productId, newAmount);
    }

    public boolean productExists(int productId) {
        return productAmounts.containsKey(productId);
    }

    public boolean isStoreOwner(int userId) {
        return ownerIds.contains(userId);
    }

    public boolean isStoreManager(int userId) {
        return managerIds.contains(userId);
    }

    public void addStoreOwner(int newOwnerId) {
        ownerIds.add(newOwnerId);
    }

    public void addStoreManager(int newManagerId) {
        managerIds.add(newManagerId);
    }

    public void closeStore(int userId) {
        if(!this.isActive)
            throw new IllegalArgumentException(String.format("A store with id %d is already closed.", storeId));
        if(founderId != userId)
            throw new IllegalArgumentException(String.format("A user with id %d can not close the store with id %d (not a founder).", userId, storeId));

        this.isActive = false;
    }

    public StoreDTO getStoreDTO() {
        return new StoreDTO(storeId, isActive, storeName, productAmounts, founderId, ownerIds, managerIds, sellerIds, buyPolicyIds, discountPolicyIds, orderIds);
    }


}
