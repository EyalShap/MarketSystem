package com.sadna.sadnamarket.service;

import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.auth.IAuthRepository;
import com.sadna.sadnamarket.domain.users.IUserRepository;
import com.sadna.sadnamarket.domain.users.MemoryRepo;
import com.sadna.sadnamarket.domain.users.UserFacade;

public class UserService {

    private IAuthRepository iAuthRepository;
    private AuthFacade authFacade;
    private UserFacade userFacade;
    private IUserRepository iUserRepository;
    public UserService(){
        iAuthRepository=new AuthRepositoryMemoryImpl();
        iUserRepository=new MemoryRepo();
        userFacade=new UserFacade(iUserRepository);
        authFacade=new AuthFacade(iAuthRepository,userFacade);
    }

    
    public Response login(String username, String password){
        try{
            String token= authFacade.login(username, password);
            return Response.createResponse(false, token);

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response logout(String username){
        try{
            userFacade.logout(username);
            return Response.createResponse();

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response exitGuest(int guestId){
        try{
            userFacade.exitGuest(guestId);
            return Response.createResponse();

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response enterAsGuest(){
        try{
            int guestId=userFacade.enterAsGuest();
            return Response.createResponse(guestId);

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response register(String username, String password){
        try{
            authFacade.register(username,password);
            return Response.createResponse();

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }
}
