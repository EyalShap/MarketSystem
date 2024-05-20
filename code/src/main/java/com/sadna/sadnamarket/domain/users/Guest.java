package com.sadna.sadnamarket.domain.users;

public class Guest implements IUser {

    @Override
    public void addToCart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToCart'");
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
    public void login(){
        // TODO
    }
    
}
