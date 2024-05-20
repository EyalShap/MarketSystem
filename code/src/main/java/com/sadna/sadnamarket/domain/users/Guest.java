package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.auth.AuthFacade;

public class Guest extends IUser {

    

    public Guest(int guestId){
        cart=new Cart();
    }


    @Override
    public boolean isLoggedIn() {
        return false;
    }
  
    public void login(String username,String password){
        AuthFacade.getInstance().auth(username, password);
    }
}
