package com.sadna.sadnamarket.domain.users;

public class Guest extends IUser {

    

    public Guest(){
        cart=new Cart();
    }


    @Override
    public void addToCart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToCart'");
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
  
    
    
}
