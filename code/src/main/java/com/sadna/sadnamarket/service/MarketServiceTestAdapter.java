package com.sadna.sadnamarket.service;

import com.sadna.sadnamarket.api.Response;
import org.springframework.beans.factory.annotation.Autowired;

public class MarketServiceTestAdapter {
    //this is an adapter for the tests
    //please add the true configuration here when you make additions to the actual service
    //thanks
    @Autowired
    MarketService real;

    public Response guestEnterSystem(){
        return Response.createResponse(false, "Imagine this is session ID or something");
    }

    public Response guestLeaveSystem(String uuid){
        return Response.createResponse(false, "Guest has left");
    }

    public Response guestCartExists(String uuid){
        return Response.createResponse(false, "false");
    }

    public Response signUp(String uuid, String email, String username, String passwordHash){
        return Response.createResponse(false, "{\"userId\": \"4\", \"token\": \"3424234\"}");
    }

    public Response memberExists(int userId){
        return Response.createResponse(false, "true");
    }

    public Response tokenValid(String token){
        return Response.createResponse(false, "true");
    }

    public Response login(String email, String passwordHash){
        return Response.createResponse(false, "{\"userId\": \"4\", \"token\": \"3424234\"}");
    }

    public Response logout(String token){
        return Response.createResponse(false, "true");
    }
}
