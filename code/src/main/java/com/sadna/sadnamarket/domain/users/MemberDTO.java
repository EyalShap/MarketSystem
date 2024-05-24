package com.sadna.sadnamarket.domain.users;

public class MemberDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    public MemberDTO(Member member) {
        this.username = member.getUsername();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.emailAddress = member.getEmailAddress();
        this.phoneNumber = member.getPhoneNumber();
    }

    public MemberDTO(String username, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

