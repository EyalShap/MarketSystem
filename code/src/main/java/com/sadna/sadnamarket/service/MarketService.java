package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.stores.StoreController;
import com.sadna.sadnamarket.domain.users.UserController;
import org.springframework.stereotype.Service;

import java.util.HashMap;

//this is the main facade
//there will be a function for every use case
//have fun

@Service
public class MarketService {
    private static MarketService instance;
    private static UserController userController;
    private static StoreController storeController;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private MarketService() {
        this.userController = UserController.getInstance();
        this.storeController = StoreController.getInstance();
    }

    public static MarketService getInstance() {
        if (instance == null) {
            instance = new MarketService();
        }
        return instance;
    }

    // ----------------------- Stores -----------------------

    // returns id of the created store
    public Response createStore(int founderId, String storeName) {
        //check if the user can create a new store
        if(!userController.canCreateStore(founderId)) {
            return Response.createResponse(true, String.format("User with id %d can not create a new store.", founderId));
        }

        try {
            int newStoreId = storeController.createStore(founderId, storeName); // will throw an exception if the store already exists
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }
}
