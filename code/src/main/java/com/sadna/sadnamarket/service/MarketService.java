package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.stores.StoreController;
import com.sadna.sadnamarket.domain.users.UserFacade;
import org.springframework.stereotype.Service;

import java.util.*;

//this is the main facade
//there will be a function for every use case
//have fun

@Service
public class MarketService {
    private static MarketService instance;
    private static UserFacade userController;
    private static StoreController storeController;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private MarketService() {
        this.userController = UserFacade.getInstance();
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
            int newStoreId = storeFacade.createStore(founderId, storeName); // will throw an exception if the store already exists
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(int userId, int storeId, String productName, int productQuantity, int productPrice) {
        try {
            int newProductId = storeFacade.addProductToStore(userId, storeId, productName, productQuantity, productPrice);
            return Response.createResponse(false, objectMapper.writeValueAsString(newProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response deleteProductFromStore(int userId, int storeId, int productId) {
        try {
            int deletedProductId = storeFacade.deleteProduct(userId, storeId, productId);
            return Response.createResponse(false, objectMapper.writeValueAsString(deletedProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response updateProductInStore(int userId, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        try {
            int updateProductId = storeFacade.updateProduct(userId, storeId, productId, newProductName, newQuantity, newPrice);
            return Response.createResponse(false, objectMapper.writeValueAsString(updateProductId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response closeStore(int userId, int storeId) {
        try {
            boolean storeClosed = storeFacade.closeStore(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(storeClosed));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOwners(int userId, int storeId) {
        try {
            List<UserDTO> owners = storeFacade.getOwners(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(owners));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getManagers(int userId, int storeId) {
        try {
            List<UserDTO> managers = storeFacade.getManagers(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(managers));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getSellers(int userId, int storeId) {
        try {
            List<UserDTO> sellers = storeFacade.getSellers(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(sellers));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        try {
            storeFacade.sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        try {
            storeFacade.sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, permissions);
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreOwnerRequest(int newOwnerId, int storeId) {
        try {
            userFacade.acceptStoreOwnerRequest(newOwnerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(newOwnerId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreManagerRequest(int newManagerId, int storeId) {
        try {
            userFacade.acceptStoreManagerRequest(newManagerId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(newManagerId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreOrderHistory(int userId, int storeId) {
        try {
            String history = storeFacade.getStoreOrderHisotry(userId, storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(history));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreInfo(int storeId) {
        try {
            Set<String> fields = Set.of("storeId", "storeName");
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("filter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

            StoreDTO storeDTO = storeFacade.getStoreInfo(storeId);
            String json = objectMapper.writer(filterProvider).writeValueAsString(storeDTO);
            return Response.createResponse(false, json);
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getProductsInfo(int storeId) {
        try {
            Map<String, Integer> productDTOs = storeFacade.getProductsInfo(storeId);
            return Response.createResponse(false, objectMapper.writeValueAsString(productDTOs));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addSellerToStore(int storeId, int adderId, int sellerId) {
        try {
            storeFacade.addSeller(storeId, adderId, sellerId);
            return Response.createResponse(false, objectMapper.writeValueAsString(sellerId));
        }
        catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

}
