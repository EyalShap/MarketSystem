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
    private List<Notification> notifes;
    private static final Logger logger = LogManager.getLogger(Member.class);

    private boolean isLoggedIn;

    public List<UserRole> getUserRoles(){
        return roles;
    }
    public Member(String name){
        roles=new ArrayList<>();
        orders=new HashMap<>();
        isLoggedIn=false;
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
        this.isLoggedIn=true;
    }

    public void addNotification(String message){
        notifes.add(new Notification(message));
    }
    public void addRequest(String message,int store_id){
        notifes.add(new Request(message,store_id));
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

    public String getName(){
        return name;
    }
    
}
