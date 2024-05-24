package com.sadna.sadnamarket.service;

import java.util.List;

import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.auth.IAuthRepository;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
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

    public Response register(String username, String password,String firstName, String lastName,String emailAddress,String phoneNumber){
        try{
            authFacade.register(username,password,firstName, lastName, emailAddress, phoneNumber);
            return Response.createResponse();

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response setFirstName(String username, String firstName) {
        try {
            userFacade.setFirstName(username, firstName);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setLastName(String username, String lastName) {
        try {
            userFacade.setLastName(username, lastName);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setEmailAddress(String username, String emailAddress) {
        try {
            userFacade.setEmailAddress(username, emailAddress);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setPhoneNumber(String username, String phoneNumber) {
        try {
            userFacade.setPhoneNumber(username, phoneNumber);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response addProductToCart(String username, int storeId, int productId, int amount) {
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("amount should be above 0");
            userFacade.addProductToCart(username, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToCart(int guestId, int storeId, int productId, int amount) {
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("amount should be above 0");
            userFacade.addProductToCart(guestId, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response removeProductFromCart(String username, int storeId, int productId) {
        try {
            userFacade.removeProductFromCart(username, storeId, productId);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response removeProductFromCart(int guestId, int storeId, int productId) {
        try {
            userFacade.removeProductFromCart(guestId, storeId, productId);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response changeQuantityCart(String username, int storeId, int productId, int amount) {
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("amount should be above 0");
            userFacade.changeQuantityCart(username, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response changeQuantityCart(int guestId, int storeId, int productId, int amount) {
        try {
            userFacade.changeQuantityCart(guestId, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response acceptRequest(String acceptingName, int requestID) {
        try {
            userFacade.accept(acceptingName, requestID);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response setSystemAdminstor(String username) {
        try {
            userFacade.setSystemManagerUserName(username);;
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response leaveRole(String username,int storeId) {
        try {
            userFacade.leaveRole(username,storeId);;
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response getOrderHistory(String username) {
        try {
            //List<OrderDTO> orders=userFacade.getUserOrders(username);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response viewCart(String username) {
        try {
            userFacade.viewCart(username);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response purchaseCart(String username) {
        try {
            userFacade.purchaseCart(username);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }






}
