package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.stores.StoreController;
import com.sadna.sadnamarket.domain.users.ManagerPermission;
import com.sadna.sadnamarket.domain.users.UserController;
import com.sadna.sadnamarket.domain.users.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        try {
            int newStoreId = storeController.createStore(founderId, storeName); // will throw an exception if the store already exists
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(int userId, int storeId, String productName, int productQuantity, int productPrice) {
        try {
            int newProductId = storeController.addProductToStore(userId, storeId, productName, productQuantity, productPrice);
            return Response.createResponse(false, objectMapper.writeValueAsString(newProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response deleteProductFromStore(int userId, int storeId, int productId) {
        try {
            int deletedProductId = storeController.deleteProduct(userId, storeId, productId);
            return Response.createResponse(false, objectMapper.writeValueAsString(deletedProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response updateProductInStore(int userId, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        try {
            int updateProductId = storeController.updateProduct(userId, storeId, productId, newProductName, newQuantity, newPrice);
            return Response.createResponse(false, objectMapper.writeValueAsString(updateProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response closeStore(int userId, int storeId) {
        try {
            boolean storeClosed = storeController.closeStore(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(storeClosed));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOwners(int userId, int storeId) {
        try {
            List<UserDTO> owners = storeController.getOwners(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(owners));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getManagers(int userId, int storeId) {
        try {
            List<UserDTO> managers = storeController.getManagers(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(managers));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getSellers(int userId, int storeId) {
        try {
            List<UserDTO> sellers = storeController.getSellers(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(sellers));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        try {
            storeController.sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        try {
            storeController.sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, permissions);
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreOwnerRequest(int newOwnerId, int storeId) {
        try {
            userController.acceptStoreOwnerRequest(newOwnerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(newOwnerId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreManagerRequest(int newManagerId, int storeId) {
        try {
            userController.acceptStoreManagerRequest(newManagerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(newManagerId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

}
