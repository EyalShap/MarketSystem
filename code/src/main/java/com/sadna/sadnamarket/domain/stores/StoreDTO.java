package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonFilter("filter")
public class StoreDTO {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private Map<Integer, Integer> productAmounts;
    private String founderUsername;
    private List<String> ownerUsernames;
    private List<String> managerUsernames;
    private List<String> sellerUsernames;
    private List<Integer> buyPolicyIds;
    private List<Integer> discountPolicyIds;
    private List<Integer> orderIds;

    public StoreDTO() {
    }

    public StoreDTO(int storeId, boolean isActive, String storeName, Map<Integer, Integer> productAmounts, String founderUsername, List<String> ownerUsernames, List<String> managerUsernames, List<String> sellerUsernames, List<Integer> buyPolicyIds, List<Integer> discountPolicyIds, List<Integer> orderIds) {
        this.storeId = storeId;
        this.isActive = isActive;
        this.storeName = storeName;
        this.productAmounts = productAmounts;
        this.founderUsername = founderUsername;
        this.ownerUsernames = ownerUsernames;
        this.managerUsernames = managerUsernames;
        this.sellerUsernames = sellerUsernames;
        this.buyPolicyIds = buyPolicyIds;
        this.discountPolicyIds = discountPolicyIds;
        this.orderIds = orderIds;
    }


    public int getStoreId() {
        return storeId;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getStoreName() {
        return storeName;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public String getFounderUsername() {
        return founderUsername;
    }

    public List<String> getOwnerUsernames() {
        return ownerUsernames;
    }

    public List<String> getManagerUsernames() {
        return managerUsernames;
    }

    public List<String> getSellerUsernames() {
        return sellerUsernames;
    }

    public List<Integer> getBuyPolicyIds() {
        return buyPolicyIds;
    }

    public List<Integer> getDiscountPolicyIds() {
        return discountPolicyIds;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDTO storeDTO = (StoreDTO) o;
        return storeId == storeDTO.storeId && isActive == storeDTO.isActive && Objects.equals(storeName, storeDTO.storeName) && Objects.equals(productAmounts, storeDTO.productAmounts) && Objects.equals(founderUsername, storeDTO.founderUsername) && Objects.equals(ownerUsernames, storeDTO.ownerUsernames) && Objects.equals(managerUsernames, storeDTO.managerUsernames) && Objects.equals(sellerUsernames, storeDTO.sellerUsernames) && Objects.equals(buyPolicyIds, storeDTO.buyPolicyIds) && Objects.equals(discountPolicyIds, storeDTO.discountPolicyIds) && Objects.equals(orderIds, storeDTO.orderIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, isActive, storeName, productAmounts, founderUsername, ownerUsernames, managerUsernames, sellerUsernames, buyPolicyIds, discountPolicyIds, orderIds);
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
