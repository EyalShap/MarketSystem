package com.sadna.sadnamarket.domain.auth;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class AuthRepositoryMemoryImpl implements IAuthRepository {

    private static HashMap<String,String> userNameAndPassword;

    public AuthRepositoryMemoryImpl(){
        userNameAndPassword=new HashMap<>();;
    }
    @Override
    public void login(String username, String password) {
        if(!hasMember(username))
        throw new NoSuchElementException("user doesnt exist");
        if(!isPasswordCorrect(username,password))
            throw new IllegalArgumentException("password is incorrect");
    }
    
    private boolean isPasswordCorrect(String userName,String password){
        return userNameAndPassword.get(userName).equals(password);
    }
    
    private boolean hasMember(String username){
        if(userNameAndPassword.containsKey(username))
            return true;
        return false;
    }
    @Override
    public HashMap<String, String> getAll() {
            return userNameAndPassword;
    }
    @Override
    public void add(String username, String password) {
        if (hasMember(username))
            throw new IllegalArgumentException("username already exits");
        userNameAndPassword.put(username,password);
    }
    @Override
    public void delete(String username) {
        userNameAndPassword.remove(username);
    }

}
