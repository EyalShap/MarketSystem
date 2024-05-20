package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member extends IUser {
    private List<UserRole> roles;
    private HashMap<String,Integer> orders;
    private List<Notification> notifes;

    private boolean isLoggedIn;

    public List<UserRole> getUserRoles(){
        return roles;
    }
    private Member(){
        roles=new ArrayList<>();
        orders=new HashMap<>();
        isLoggedIn=false;
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
    public void addRequest(){
        
    }

    public void logout(){
        this.setLogin(false);
    }
   
    public void purchase(){
        this.cart.purchase();
    }



    
}
