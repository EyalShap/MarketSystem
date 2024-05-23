package com.sadna.sadnamarket.domain.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sadna.sadnamarket.domain.users.UserFacade;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

public class AuthFacade {

    private TokenService tokenService;
    private IAuthRepository iAuthRepository;
    private UserFacade userFacade;
    
    private static final Logger logger = LogManager.getLogger(AuthFacade.class);

    public AuthFacade(IAuthRepository iAuthRepository, UserFacade userFacade) {
        this.iAuthRepository=iAuthRepository;
        tokenService=new TokenService();
        this.userFacade=userFacade;
        logger.info("hiiii");


    }
    
    public String login(String username, String password) {
        logger.info("start-Login. args: "+username+", "+password);
        String token = auth(username,password);
        userFacade.login(username, password);
        logger.info("end-Login. returnedValue:"+ token);
        return token;
    
    }
    public String login(String username, String password, int guestId) {
        // If the user is authenticated, generate a JWT token for the user
        logger.info("start-Login. args: "+username+", "+password+", "+ guestId);
        String token = auth(username,password);
        userFacade.login(username, password,guestId);
        logger.info("end-Login. returnedValue:"+ token);
        return token;
    
    }
    private String auth(String username, String password){
        logger.info("start-auth. args: "+username+", "+password);
        iAuthRepository.login(username,password); 
        // If the user is authenticated, generate a JWT token for the user
        logger.info("end-auth. returnedValue:"+ tokenService.generateToken(username));
        return tokenService.generateToken(username);
    }
    public String login(String jwt) {
        if(!tokenService.validateToken(jwt))
            throw new IllegalArgumentException("jwt isnt valid");
        else 
            return jwt;

    }

    public void register(String username, String password,String firstName, String lastName,String emailAddress,String phoneNumber){
        logger.info("start-register. args: "+username+", "+password);
        iAuthRepository.add(username, password);
        userFacade.register(username, firstName, lastName, emailAddress, phoneNumber);
        logger.info("end-register.");

    }

}