package com.sadna.sadnamarket.domain.auth;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthRepositoryMemoryImpl implements IAuthRepository {

    private static HashMap<String,String> userNameAndPassword;
    private static final Logger logger = LogManager.getLogger(AuthFacade.class);

    public AuthRepositoryMemoryImpl(){
        userNameAndPassword=new HashMap<>();;
    }
    @Override
    public void login(String username, String password) {
        logger.info("start-Login. args: "+username+", "+password);
        if(!hasMember(username)){
            logger.error("user doesnt exist");
            throw new NoSuchElementException("user doesnt exist");
        }
        if(!isPasswordCorrect(username,password)){
            logger.error("user doesnt exist");
            throw new IllegalArgumentException("password is incorrect");
            }
        logger.info("end-Login.");

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
