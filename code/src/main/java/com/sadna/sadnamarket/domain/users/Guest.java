package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.auth.AuthFacade;

public class Guest extends IUser {
    int guestID;

    public Guest(int guestID){
        cart=new Cart();
        this.guestID=guestID;
    }


    @Override
    public boolean isLoggedIn() {
        return false;
    }
  
    
}
