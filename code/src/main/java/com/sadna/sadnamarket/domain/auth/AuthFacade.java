package com.sadna.sadnamarket.domain.auth;

import com.sadna.sadnamarket.domain.users.UserFacade;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

public class AuthFacade {

    private TokenService tokenService;
    private IAuthRepository iAuthRepository;
    private UserFacade userFacade;
    public AuthFacade(IAuthRepository iAuthRepository, UserFacade userFacade) {
        this.iAuthRepository=iAuthRepository;
        tokenService=new TokenService();
        this.userFacade=userFacade;

    }
    
    public String login(String username, String password) {
        String token = auth(username,password);
        userFacade.login(username, password);
        return token;
    
    }
    public String login(String username, String password, int guestId) {
        // If the user is authenticated, generate a JWT token for the user
        String token = auth(username,password);
        userFacade.login(username, password,guestId);
        return token;
    
    }
    private String auth(String username, String password){
        iAuthRepository.login(username,password); 
        // If the user is authenticated, generate a JWT token for the user
        return tokenService.generateToken(username);
    }
    public String login(String jwt) {
        if(!tokenService.validateToken(jwt))
            throw new IllegalArgumentException("jwt isnt valid");
        else 
            return jwt;
    
    }

    public void register(String username, String password){
        iAuthRepository.add(username, password);
    }

}