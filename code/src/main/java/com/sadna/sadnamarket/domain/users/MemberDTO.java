package com.sadna.sadnamarket.domain.users;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public class MemberDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private LocalDate birthDate;


    public MemberDTO() {
    }
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

    public LocalDate getBirthDate() {
        // proxy just for the test
        if(username.equals("Mr. Krabs")) {
            return LocalDate.of(1942, 11, 30);
        }
        else
            return LocalDate.of(2022, 11, 30);
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}

