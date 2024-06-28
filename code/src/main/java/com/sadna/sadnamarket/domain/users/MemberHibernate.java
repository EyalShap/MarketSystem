package com.sadna.sadnamarket.domain.users;

import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "Members")
public class MemberHibernate {
    @Id
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String phoneNumber;
    @Column
    private boolean isLoggedIn;
    @Column
    private int notifyID;

    @OneToMany
    @JoinTable(
        name = "User_roles",
        joinColumns = @JoinColumn(name = "username")
    )
    private List<UserRoleHibernate> roles;
    @OneToMany
    @JoinTable(
        name = "User_orders",
        joinColumns = @JoinColumn(name = "username")
    )
    private List<UserOrders> orders;

    @OneToOne
    @JoinTable(
        name = "cart",
        joinColumns = @JoinColumn(name = "cartId")
    )
    private CartHibernate cart;

    public MemberHibernate(String username, String firstName, String lastName, String email, String phoneNumber,int notifyID, boolean isLoggedIn) { 
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLoggedIn = false;
        this.notifyID = notifyID;
        this.isLoggedIn = isLoggedIn;
    }
    public MemberHibernate(String username, String firstName, String lastName, String email, String phoneNumber) { 
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLoggedIn = false;
        this.notifyID = 0;
        this.isLoggedIn = false;
    }
    public String getUsername() {
        return username;
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
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public int getNotifyID() {
        return notifyID;
    }
    public void setNotifyID(int notifyID) {
        this.notifyID = notifyID;
    }
}
