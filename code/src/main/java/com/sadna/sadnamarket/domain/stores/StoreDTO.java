package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.Objects;

public class StoreDTO {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private double rank;
    private String address;
    private String email;
    private String phoneNumber;
    private Map<Integer, Integer> productAmounts;
    private String founderUsername;
    private Set<String> ownerUsernames;
    private Set<String> managerUsernames;
    //private Set<String> sellerUsernames;
    private Set<Integer> orderIds;

    public StoreDTO() {
    }

    public StoreDTO(Store store) {
        this.storeId = store.getStoreId();
        this.isActive = store.getIsActive();
        this.storeName = store.getStoreInfo().getStoreName();
        this.rank = store.getStoreInfo().getRank();
        this.address = store.getStoreInfo().getAddress();
        this.email = store.getStoreInfo().getEmail();
        this.phoneNumber = store.getStoreInfo().getPhoneNumber();
        this.productAmounts = store.getProductAmounts();
        this.founderUsername = store.getFounderUsername();
        this.ownerUsernames = store.getOwnerUsernames();
        this.managerUsernames = store.getManagerUsernames();
        //this.sellerUsernames = store.getSellerUsernames();
        this.orderIds = store.getOrderIds();
    }

    public StoreDTO(int storeId, boolean isActive, String storeName, double rank, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours, Map<Integer, Integer> productAmounts, String founderUsername, Set<String> ownerUsernames, Set<String> managerUsernames, Set<Integer> orderIds) {
        this.storeId = storeId;
        this.isActive = isActive;
        this.storeName = storeName;
        this.rank = rank;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.productAmounts = productAmounts;
        this.founderUsername = founderUsername;
        this.ownerUsernames = ownerUsernames;
        this.managerUsernames = managerUsernames;
        //this.sellerUsernames = sellerUsernames;
        this.orderIds = orderIds;
    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public void setProductAmounts(Map<Integer, Integer> productAmounts) {
        this.productAmounts = productAmounts;
    }

    public String getFounderUsername() {
        return founderUsername;
    }

    public void setFounderUsername(String founderUsername) {
        this.founderUsername = founderUsername;
    }

    public Set<String> getOwnerUsernames() {
        return ownerUsernames;
    }

    public void setOwnerUsernames(Set<String> ownerUsernames) {
        this.ownerUsernames = ownerUsernames;
    }

    public Set<String> getManagerUsernames() {
        return managerUsernames;
    }

    public void setManagerUsernames(Set<String> managerUsernames) {
        this.managerUsernames = managerUsernames;
    }

    /*public Set<String> getSellerUsernames() {
        return sellerUsernames;
    }

    public void setSellerUsernames(Set<String> sellerUsernames) {
        this.sellerUsernames = sellerUsernames;
    }*/

    public Set<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(Set<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDTO storeDTO = (StoreDTO) o;
        return storeId == storeDTO.storeId && isActive == storeDTO.isActive && Double.compare(storeDTO.rank, rank) == 0 && Objects.equals(storeName, storeDTO.storeName) && Objects.equals(address, storeDTO.address) && Objects.equals(email, storeDTO.email) && Objects.equals(phoneNumber, storeDTO.phoneNumber) && Objects.equals(productAmounts, storeDTO.productAmounts) && Objects.equals(founderUsername, storeDTO.founderUsername) && Objects.equals(ownerUsernames, storeDTO.ownerUsernames) && Objects.equals(managerUsernames, storeDTO.managerUsernames) && Objects.equals(orderIds, storeDTO.orderIds);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(storeId, isActive, storeName, rank, address, email, phoneNumber, productAmounts, founderUsername, ownerUsernames, managerUsernames, orderIds);
        return result;
    }
}
