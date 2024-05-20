package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member extends IUser {
    private List<UserRole> roles;
    private HashMap<String,Integer> orders;
    

    private boolean isLoggedIn;


    private Member(){
        roles=new ArrayList<>();
        orders=new HashMap<>();
        isLoggedIn=false;
    }
    @Override
    public void addToCart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToCart'");
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    public void setCart(Cart cart){
        this.cart=cart;
    }

    public void login(Cart guestCart){
        if(cart.isEmpty()){
            cart=guestCart;
        }
        isLoggedIn=true;
    }

    public void addNotification(String message){

    }



    
}
