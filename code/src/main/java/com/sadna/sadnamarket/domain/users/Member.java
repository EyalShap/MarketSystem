package com.sadna.sadnamarket.domain.users;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member extends IUser {
    private String name;
    private List<UserRole> roles;
    private HashMap<String,Integer> orders;
    private HashMap<Integer,Notification> notifes;
    private static final Logger logger = LogManager.getLogger(Member.class);
    private boolean isLoggedIn;
    private int notifyID;

    public List<UserRole> getUserRoles(){
        return roles;
    }
    public Member(String name){
        roles=new ArrayList<>();
        notifes=new HashMap<>();
        orders=new HashMap<>();
        isLoggedIn=false;
        notifyID=0;
        logger.info("hiiii");
        this.name=name;
        
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
        role.sendRequest(userFacade, name, userName,"Owner");
    }

     public void addManagerRequest(UserFacade userFacade, String userName, int store_id) {
        UserRole role=getRoleOfStore(store_id);
        role.sendRequest(userFacade, name, userName,"Manager");
    }

    public UserRole getRoleOfStore(int store_id){
        for(UserRole role: getUserRoles()){
            if(role.getStoreId()==store_id){
                return role;
            }
            else{
                throw new IllegalArgumentException("User has no role in this store");
            }
        }
        throw new IllegalArgumentException("User has no role in this store");
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

    // public void removeRole(int storeId){
    //     for (UserRole role : roles) {
    //         if(role.getStoreId()==storeId)
    //             role.leaveRole();
    //             roles.remove(role); break;
    //     }
        
    //}
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
    public HashMap<Integer,Notification> getNotifications(){
        return notifes;
    }

    public String getName(){
        return name;
    }
    public void getRequest(String senderName, int storeId,String reqType) {
        notifes.put(++notifyID,new Request(senderName,"You got appointment request",storeId,reqType));
    }
    public void acceptToOwner(int requestID) {
        notifes.get(requestID).acceptOwner(this);
    }
     public void acceptToManager(int requestID) {
        notifes.get(requestID).acceptManager(this);
    }
   
    
    
}
