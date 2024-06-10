package com.sadna.sadnamarket.api;

import java.time.LocalTime;

public class CreateStoreRequest {
    String founderUsername;
    String storeName;
    String address;
    String email;
    String phoneNumber;
    LocalTime[] openingHours;
    LocalTime[] closingHours;

    public String getFounderUsername() {
        return founderUsername;
    }

    public void setFounderUsername(String founderUsername) {
        this.founderUsername = founderUsername;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
}
