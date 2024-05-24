package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonFilter("filter")
public class StoreDTO {
    private int storeId;
    private boolean isActive;
    private String storeName;
    private double rank;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalTime[] openingHours;
    private LocalTime[] closingHours;
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

    public StoreDTO(Store store) {
        this.storeId = store.getStoreId();
        this.isActive = store.getIsActive();
        this.storeName = store.getStoreInfo().getStoreName();
        this.rank = store.getStoreInfo().getRank();
        this.address = store.getStoreInfo().getAddress();
        this.email = store.getStoreInfo().getEmail();
        this.phoneNumber = store.getStoreInfo().getPhoneNumber();
        this.openingHours = store.getStoreInfo().getOpeningHours();
        this.closingHours = store.getStoreInfo().getClosingHours();
        this.productAmounts = store.getProductAmounts();
        this.founderUsername = store.getFounderUsername();
        this.ownerUsernames = store.getOwnerUsernames();
        this.managerUsernames = store.getManagerUsernames();
        this.sellerUsernames = store.getSellerUsernames();
        this.buyPolicyIds = store.getBuyPolicyIds();
        this.discountPolicyIds = store.getDiscountPolicyIds();
        this.orderIds = store.getOrderIds();
    }

    public StoreDTO(int storeId, boolean isActive, String storeName, double rank, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours, Map<Integer, Integer> productAmounts, String founderUsername, List<String> ownerUsernames, List<String> managerUsernames, List<String> sellerUsernames, List<Integer> buyPolicyIds, List<Integer> discountPolicyIds, List<Integer> orderIds) {
        this.storeId = storeId;
        this.isActive = isActive;
        this.storeName = storeName;
        this.rank = rank;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
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

    public LocalTime[] getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(LocalTime[] openingHours) {
        this.openingHours = openingHours;
    }

    public LocalTime[] getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(LocalTime[] closingHours) {
        this.closingHours = closingHours;
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

    public List<String> getOwnerUsernames() {
        return ownerUsernames;
    }

    public void setOwnerUsernames(List<String> ownerUsernames) {
        this.ownerUsernames = ownerUsernames;
    }

    public List<String> getManagerUsernames() {
        return managerUsernames;
    }

    public void setManagerUsernames(List<String> managerUsernames) {
        this.managerUsernames = managerUsernames;
    }

    public List<String> getSellerUsernames() {
        return sellerUsernames;
    }

    public void setSellerUsernames(List<String> sellerUsernames) {
        this.sellerUsernames = sellerUsernames;
    }

    public List<Integer> getBuyPolicyIds() {
        return buyPolicyIds;
    }

    public void setBuyPolicyIds(List<Integer> buyPolicyIds) {
        this.buyPolicyIds = buyPolicyIds;
    }

    public List<Integer> getDiscountPolicyIds() {
        return discountPolicyIds;
    }

    public void setDiscountPolicyIds(List<Integer> discountPolicyIds) {
        this.discountPolicyIds = discountPolicyIds;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDTO storeDTO = (StoreDTO) o;
        return storeId == storeDTO.storeId && isActive == storeDTO.isActive && Double.compare(storeDTO.rank, rank) == 0 && Objects.equals(storeName, storeDTO.storeName) && Objects.equals(address, storeDTO.address) && Objects.equals(email, storeDTO.email) && Objects.equals(phoneNumber, storeDTO.phoneNumber) && Arrays.equals(openingHours, storeDTO.openingHours) && Arrays.equals(closingHours, storeDTO.closingHours) && Objects.equals(productAmounts, storeDTO.productAmounts) && Objects.equals(founderUsername, storeDTO.founderUsername) && Objects.equals(ownerUsernames, storeDTO.ownerUsernames) && Objects.equals(managerUsernames, storeDTO.managerUsernames) && Objects.equals(sellerUsernames, storeDTO.sellerUsernames) && Objects.equals(buyPolicyIds, storeDTO.buyPolicyIds) && Objects.equals(discountPolicyIds, storeDTO.discountPolicyIds) && Objects.equals(orderIds, storeDTO.orderIds);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(storeId, isActive, storeName, rank, address, email, phoneNumber, productAmounts, founderUsername, ownerUsernames, managerUsernames, sellerUsernames, buyPolicyIds, discountPolicyIds, orderIds);
        result = 31 * result + Arrays.hashCode(openingHours);
        result = 31 * result + Arrays.hashCode(closingHours);
        return result;
    }
}
