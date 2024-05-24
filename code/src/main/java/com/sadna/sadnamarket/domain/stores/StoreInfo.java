package com.sadna.sadnamarket.domain.stores;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class StoreInfo {
    private String storeName;
    private double rank;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalTime[] openingHours;
    private LocalTime[] closingHours;

    public StoreInfo(String storeName, double rank, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours) {
        this.storeName = storeName;
        this.rank = rank;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreInfo storeInfo = (StoreInfo) o;
        return Double.compare(storeInfo.rank, rank) == 0 && Objects.equals(storeName, storeInfo.storeName) && Objects.equals(address, storeInfo.address) && Objects.equals(email, storeInfo.email) && Objects.equals(phoneNumber, storeInfo.phoneNumber) && Arrays.equals(openingHours, storeInfo.openingHours) && Arrays.equals(closingHours, storeInfo.closingHours);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(storeName, rank, address, email, phoneNumber);
        result = 31 * result + Arrays.hashCode(openingHours);
        result = 31 * result + Arrays.hashCode(closingHours);
        return result;
    }
}
