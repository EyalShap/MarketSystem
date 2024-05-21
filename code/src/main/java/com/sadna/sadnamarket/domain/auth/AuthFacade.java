package com.sadna.sadnamarket.domain.auth;

import com.sadna.sadnamarket.domain.users.UserFacade;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

public class AuthFacade {

private static AuthFacade instance;
  
    private AuthFacade() {
    }
    private static TokenService tokenService;

    public static AuthFacade getInstance() {
        if (instance == null) {
            instance = new AuthFacade();
            tokenService= new TokenService();
        }
        return instance;
    }


    public String auth(String username, String password) {
        
        UserFacade.getInstance().login(username,password); 
        // If the user is authenticated, generate a JWT token for the user
        String token = tokenService.generateToken(username);
        
        return token;
    
    }
    public String auth(String username, String password,int guestId) {
        
        UserFacade.getInstance().login(username,password); 
        // If the user is authenticated, generate a JWT token for the user
        String token = tokenService.generateToken(username);
        
        return token;
    
    }
    public void logout() {
        // Logic to log out the current user
    }

}