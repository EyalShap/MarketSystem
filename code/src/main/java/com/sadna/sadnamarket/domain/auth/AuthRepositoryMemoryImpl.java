package com.sadna.sadnamarket.domain.auth;

import java.util.HashMap;
import java.util.NoSuchElementException;

import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.service.Error;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthRepositoryMemoryImpl implements IAuthRepository {

    private static HashMap<String,String> userNameAndPassword;
    private static final Logger logger = LogManager.getLogger(AuthFacade.class);

    public AuthRepositoryMemoryImpl(){
        userNameAndPassword=new HashMap<>();;
    }
    @Override
    public void login(String username, String password) {
        logger.info("start-Login. username: {} ", username);
        if(!hasMember(username)){
            logger.error("user doesnt exist");
            throw new NoSuchElementException(Error.makeAuthUserDoesntExistError());
        }
        if(!isPasswordCorrect(username,password)){
            logger.error("password incorrect");
            throw new IllegalArgumentException(Error.makeAuthPasswordIncorrectError());
            }
        logger.info("end-Login.");

    }
    
    private boolean isPasswordCorrect(String userName,String password){
        logger.info("start-isPasswordCorrect. username: {} ", userName);
        boolean res= verifyPassword(password,userNameAndPassword.get(userName));
        logger.info("end-isPasswordCorrect. returnedValue:{}",res);
        return res;
    }
    
    private synchronized boolean hasMember(String username){
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
            throw new IllegalArgumentException(Error.makeAuthUsernameExistsError());
        userNameAndPassword.put(username,hashPassword(password));
    }
    @Override
    public void delete(String username) {
        logger.info("start-delete. username: {} ", username);
        userNameAndPassword.remove(username);
        logger.info("end delete {}",username);
    }
    private static String hashPassword(String password) {
    logger.info("start-hashPassword");
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    logger.info("end-hashPassword. returnedValue:{}",hashedPassword);
    return hashedPassword;
  }

  private static boolean verifyPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }
@Override
public void clear() {
    
}

}
