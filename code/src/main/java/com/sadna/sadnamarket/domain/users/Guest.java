package com.sadna.sadnamarket.domain.users;

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
