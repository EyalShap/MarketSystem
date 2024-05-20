package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member implements IUser {
    private List<userRole> roles;
    private HashMap<String,Integer> orders;
    private boolean isLoggedIn;


    private Member(){
        roles=new ArrayList<>();
        orders=new HashMap<>();
        isLoggedIn=true;
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
    public void addNotification(){

    }



    
}
