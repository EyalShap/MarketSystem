package com.sadna.sadnamarket.domain.auth;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

public class AuthFacade {

    private TokenService tokenService;
    private IAuthRepository iAuthRepository;
    private AuthFacade(IAuthRepository iAuthRepository) {
        this.iAuthRepository=iAuthRepository;
        tokenService=new TokenService();
    }
    
    public String auth(String username, String password) {
        
        iAuthRepository.login(username,password); 
        // If the user is authenticated, generate a JWT token for the user
        String token = tokenService.generateToken(username);
        return token;
    
    }
    public String auth(String jwt) {
        if(!tokenService.validateToken(jwt))
            throw new IllegalArgumentException("jwt isnt valid");
        else 
            return jwt;
    
    }

}