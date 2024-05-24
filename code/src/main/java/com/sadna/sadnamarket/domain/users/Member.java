package com.sadna.sadnamarket.domain.users;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member extends IUser {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private List<UserRole> roles;
    private List<Integer> orders;
    private HashMap<Integer,Notification> notifes;
    private static final Logger logger = LogManager.getLogger(Member.class);
    private boolean isLoggedIn;
    private int notifyID;

    public List<UserRole> getUserRoles(){
        return roles;
    }
    public Member(String username,String firstName, String lastName,String emailAddress,String phoneNumber){
        roles=new ArrayList<>();
        notifes=new HashMap<>();
        orders=new ArrayList<>();
        isLoggedIn=false;
        notifyID=0;
        logger.info("hiiii");
        this.username=username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
   
    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    public void setCart(Cart cart){
        if (this.cart.isEmpty())
            this.cart=cart;
    }

    public void setLogin(boolean isLoggedIn){
        this.isLoggedIn=isLoggedIn;
    }

    public void addNotification(String message){
        notifes.put(++notifyID,new Notification(message));
    }
    public void addOwnerRequest(UserFacade userFacade,String userName,int store_id){
        UserRole role=getRoleOfStore(store_id);
         if(role.getApointee().equals(userName)){
            throw new IllegalArgumentException("You disallowed appoint the one who apointed you!");
        }
        role.sendRequest(userFacade, username, userName,"Owner");
    }

     public void addManagerRequest(UserFacade userFacade, String userName, int store_id) {
        UserRole role=getRoleOfStore(store_id);
        role.sendRequest(userFacade, username, userName,"Manager");
    }

    public UserRole getRoleOfStore(int store_id){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==store_id){
                return role;
            }
        }
        throw new IllegalArgumentException("User has no role in this store");
    }
    private boolean hasRoleInStore(int store_id){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==store_id){
                return true;
            }
        }
        return false;
    }

    public void logout(){
        this.setLogin(false);
    }
   
    public void purchase(){
        this.cart.purchase();
    }

    public void addRole(UserRole role){
        roles.add(role);
    }
    public void removeRole(UserRole role){
        roles.remove(role);
    }

    public void addPermissionToRole(Permission permission, int storeId){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==storeId){
                role.addPermission(permission);
            }
        }
    }

    public boolean hasPermissionToRole(Permission permission, int storeId){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==storeId && role.hasPermission(permission)){
                return true;
            }
        }
        return false;
    }
    public void removePermissionFromRole(Permission permission, int storeId){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==storeId){
                role.removePermission(permission);
            }
        }
    }
    public HashMap<Integer,Notification> getNotifications(){
        return notifes;
    }

    public String getUsername(){
        return username;
    }
    public void getRequest(String senderName, int storeId,String reqType) {
        if(hasRoleInStore(storeId))
            throw new IllegalStateException("member already has role in store");
        notifes.put(++notifyID,new Request(senderName,"You got appointment request",storeId,reqType));
    }
    public void accept(int requestID) {
        notifes.get(requestID).accept(this);
    }
    public Request getRequest(int request_id){
        return (Request) notifes.get(request_id);
    }


   // Getter for firstName
   public String getFirstName() {
    return firstName;
}

// Setter for firstName
public void setFirstName(String firstName) {
    this.firstName = firstName;
}

// Getter for lastName
public String getLastName() {
    return lastName;
}

// Setter for lastName
public void setLastName(String lastName) {
    this.lastName = lastName;
}

// Getter for emailAddress
public String getEmailAddress() {
    return emailAddress;
}

// Setter for emailAddress
public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
}

// Getter for phoneNumber
public String getPhoneNumber() {
    return phoneNumber;
}

// Setter for phoneNumber
public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
}

public List<Integer> getOrdersHistory(){
    return orders;
}
    
    
}
