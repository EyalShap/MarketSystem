package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.users.UserDTO;
import com.sadna.sadnamarket.domain.users.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

//this is the main facade
//there will be a function for every use case
//have fun

public class MarketService {
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private OrderFacade orderFacade;
    private StoreFacade storeFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private static ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(MarketService.class);

    public MarketService(IStoreRepository storeRepository) {
        this.userFacade = new UserFacade();
        this.productFacade = new ProductFacade();
        this.orderFacade = new OrderFacade();
        this.storeFacade = new StoreFacade(storeRepository);
        this.buyPolicyFacade = new BuyPolicyFacade();
        this.discountPolicyFacade = new DiscountPolicyFacade();

        this.storeFacade.setUserFacade(userFacade);
        this.storeFacade.setProductFacade(productFacade);
        this.storeFacade.setOrderFacade(orderFacade);
        this.storeFacade.setBuyPolicyFacade(buyPolicyFacade);
        this.storeFacade.setDiscountPolicyFacade(discountPolicyFacade);
    }

    // ----------------------- Stores -----------------------

    // returns id of the created store
    public Response createStore(int founderId, String storeName) {
        try {
            int newStoreId = storeFacade.createStore(founderId, storeName); // will throw an exception if the store already exists
            logger.info(String.format("User %d created a store with id %d.", founderId, newStoreId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            logger.error("createStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(int userId, int storeId, String productName, int productQuantity, int productPrice) {
        try {
            int newProductId = storeFacade.addProductToStore(userId, storeId, productName, productQuantity, productPrice);
            logger.info(String.format("User %d added product %d to store %d.", userId, newProductId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newProductId));
        }
        catch (Exception e) {
            logger.error("addProductToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response deleteProductFromStore(int userId, int storeId, int productId) {
        try {
            int deletedProductId = storeFacade.deleteProduct(userId, storeId, productId);
            logger.info(String.format("User %d deleted product %d from store %d.", userId, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(deletedProductId));
        }
        catch (Exception e) {
            logger.error("deleteProductFromStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response updateProductInStore(int userId, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        try {
            int updateProductId = storeFacade.updateProduct(userId, storeId, productId, newProductName, newQuantity, newPrice);
            logger.info(String.format("User %d updated product %d in store %d.", userId, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(updateProductId));
        }
        catch (Exception e) {
            logger.error("updateProductInStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response closeStore(int userId, int storeId) {
        try {
            boolean storeClosed = storeFacade.closeStore(userId, storeId);
            logger.info(String.format("User %d closed store %d.", userId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(storeClosed));
        }
        catch (Exception e) {
            logger.error("closeStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOwners(int userId, int storeId) {
        try {
            List<UserDTO> owners = storeFacade.getOwners(userId, storeId);
            logger.info(String.format("User %d got owners of store %d.", userId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(owners));
        }
        catch (Exception e) {
            logger.error("getOwners: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getManagers(int userId, int storeId) {
        try {
            List<UserDTO> managers = storeFacade.getManagers(userId, storeId);
            logger.info(String.format("User %d got managers of store %d.", userId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(managers));
        }
        catch (Exception e) {
            logger.error("getManagers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getSellers(int userId, int storeId) {
        try {
            List<UserDTO> sellers = storeFacade.getSellers(userId, storeId);
            logger.info(String.format("User %d got sellers of store %d.", userId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellers));
        }
        catch (Exception e) {
            logger.error("getSellers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        try {
            storeFacade.sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
            logger.info(String.format("User %d nominated user %d as owner of store %d.", currentOwnerId, newOwnerId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        try {
            storeFacade.sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, permissions);
            logger.info(String.format("User %d nominated user %d as manager of store %d.", currentOwnerId, newManagerId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreManagerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreOwnerRequest(int newOwnerId, int storeId) {
        try {
            userFacade.acceptStoreOwnerRequest(newOwnerId, storeId);
            logger.info(String.format("User %d accepted owner nomination in store %d.", newOwnerId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newOwnerId));
        }
        catch (Exception e) {
            logger.error("acceptStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreManagerRequest(int newManagerId, int storeId) {
        try {
            userFacade.acceptStoreManagerRequest(newManagerId, storeId);
            logger.info(String.format("User %d accepted manager nomination in store %d.", newManagerId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newManagerId));
        }
        catch (Exception e) {
            logger.error("acceptStoreManagerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreOrderHistory(int userId, int storeId) {
        try {
            String history = storeFacade.getStoreOrderHisotry(userId, storeId);
            logger.info(String.format("User %d got order history from store %d.", userId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(history));
        }
        catch (Exception e) {
            logger.error("getStoreOrderHistory: " + e.getMessage());
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
            logger.info(String.format("A user got store info of store %d.", storeId));
            return Response.createResponse(false, json);
        }
        catch (Exception e) {
            logger.error("getStoreInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getProductsInfo(int storeId) {
        try {
            Map<String, Integer> productDTOs = storeFacade.getProductsInfo(storeId);
            logger.info(String.format("A user got products info of store %d.", storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(productDTOs));
        }
        catch (Exception e) {
            logger.error("getProductsInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addSellerToStore(int storeId, int adderId, int sellerId) {
        try {
            storeFacade.addSeller(storeId, adderId, sellerId);
            logger.info(String.format("User %d added user %d as a seller to store %d.", adderId, sellerId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellerId));
        }
        catch (Exception e) {
            logger.error("addSellerToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addBuyPolicy(int userId, int storeId) {
        try {
            int policyId = storeFacade.addBuyPolicy(userId, storeId);
            logger.info(String.format("User %d added buy policy with id %d to store %d.", userId, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addBuyPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addDiscountPolicy(int userId, int storeId) {
        try {
            int policyId = storeFacade.addDiscountPolicy(userId, storeId);
            logger.info(String.format("User %d added discount policy with id %d to store %d.", userId, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addDiscountPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }
}
