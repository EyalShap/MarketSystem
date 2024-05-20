package com.sadna.sadnamarket.domain.auth;

import com.sadna.sadnamarket.domain.users.UserController;

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


    public boolean login(String username, String password) {
        
    if (UserController.login(username,password)) {
        // If the user is authenticated, generate a JWT token for the user
        String token = tokenService.generateToken(username);
        
        // Return the token to the client (you may want to return the token in a different way, such as through a DTO)
        System.out.println("Login successful");
        return true;
    } else {
        // If authentication fails, return false
        System.out.println("Login failed. Invalid username or password.");
        return false;
    }
    }

    public void logout() {
        // Logic to log out the current user
    }

}