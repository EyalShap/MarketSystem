package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.api.Response;
import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreController;
import com.sadna.sadnamarket.domain.users.UserController;
//this is the main facade
//there will be a function for every use case
//have fun

public class MarketService {
    private static UserController userController;
    private static StoreController storeController;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public MarketService() {
        this.userController = UserController.getInstance();
        this.storeController = StoreController.getInstance();
    }

    // ----------------------- Stores -----------------------

    // returns id of the created store
    public static String createStore(int founderId, String storeName) {
        //check if the user can create a new store
        if(!userController.canCreateStore(founderId)) {
            throw new IllegalArgumentException(String.format("User with id %d can not create a new store.", founderId));
        }

        int newStoreId = storeController.createStore(founderId, storeName); // will throw an exception if the store already exists

        try {
            return objectMapper.writeValueAsString(newStoreId);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
