package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.orders.IOrderRepository;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.users.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.*;

//this is the main facade
//there will be a function for every use case
//have fun

public class MarketService {
    private static MarketService instance;
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private OrderFacade orderFacade;
    private StoreFacade storeFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private AuthFacade authFacade;
    private static ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(MarketService.class);

    public MarketService(IStoreRepository storeRepository) {
        this.productFacade = new ProductFacade();
        this.orderFacade = new OrderFacade(new MemoryOrderRepository());
        this.storeFacade = new StoreFacade(storeRepository);
        this.buyPolicyFacade = new BuyPolicyFacade();
        this.discountPolicyFacade = new DiscountPolicyFacade();
        this.userFacade = new UserFacade(new MemoryRepo(),storeRepository);
        this.authFacade = new AuthFacade(new AuthRepositoryMemoryImpl(), userFacade);

        this.storeFacade.setUserFacade(userFacade);
        this.storeFacade.setProductFacade(productFacade);
        this.storeFacade.setOrderFacade(orderFacade);
        this.storeFacade.setBuyPolicyFacade(buyPolicyFacade);
        this.storeFacade.setDiscountPolicyFacade(discountPolicyFacade);
    }

    public static MarketService getInstance() {
        if (instance == null) {
            instance = new MarketService(new MemoryStoreRepository());
        }
        return instance;
    }

    // ----------------------- Stores -----------------------

    private void checkToken(String token, String username) {
        if(!authFacade.login(token).equals(username)) {
            logger.error(String.format("failed to verify token for user %s", username));
            throw new IllegalArgumentException(String.format("Token is not valid for user %s.", username));
        }
    }

    public Response createStore(String token, String founderUsername, String storeName, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours) {
        try {
            checkToken(token, founderUsername);
            int newStoreId = storeFacade.createStore(founderUsername, storeName, address, email, phoneNumber, openingHours, closingHours); // will throw an exception if the store already exists
            logger.info(String.format("User %s created a store with id %d.", founderUsername, newStoreId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            logger.error("createStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(String token, String username, int storeId, String productName, int productQuantity, int productPrice, String category) {
        try {
            checkToken(token, username);
            int newProductId = storeFacade.addProductToStore(username, storeId, productName, productQuantity, productPrice, category);
            logger.info(String.format("User %s added product %d to store %d.", username, newProductId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newProductId));
        }
        catch (Exception e) {
            logger.error("addProductToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response deleteProductFromStore(String token, String username, int storeId, int productId) {
        try {
            checkToken(token, username);
            int deletedProductId = storeFacade.deleteProduct(username, storeId, productId);
            logger.info(String.format("User %s deleted product %d from store %d.", username, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(deletedProductId));
        }
        catch (Exception e) {
            logger.error("deleteProductFromStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response updateProductInStore(String token, String username, int storeId, int productId, String newProductName, int newQuantity, int newPrice, String newCategory) {
        try {
            checkToken(token, username);
            int updateProductId = storeFacade.updateProduct(username, storeId, productId, newProductName, newQuantity, newPrice, newCategory);
            logger.info(String.format("User %s updated product %d in store %d.", username, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(updateProductId));
        }
        catch (Exception e) {
            logger.error("updateProductInStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response closeStore(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            boolean storeClosed = storeFacade.closeStore(username, storeId);
            logger.info(String.format("User %s closed store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(storeClosed));
        }
        catch (Exception e) {
            logger.error("closeStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOwners(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            List<MemberDTO> owners = storeFacade.getOwners(username, storeId);
            logger.info(String.format("User %s got owners of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(owners));
        }
        catch (Exception e) {
            logger.error("getOwners: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getManagers(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            List<MemberDTO> managers = storeFacade.getManagers(username, storeId);
            logger.info(String.format("User %s got managers of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(managers));
        }
        catch (Exception e) {
            logger.error("getManagers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getSellers(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            List<MemberDTO> sellers = storeFacade.getSellers(username, storeId);
            logger.info(String.format("User %s got sellers of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellers));
        }
        catch (Exception e) {
            logger.error("getSellers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreOwnerRequest(String token, String currentOwnerUsername, String newOwnerUsername, int storeId) {
        try {
            checkToken(token, currentOwnerUsername);
            storeFacade.sendStoreOwnerRequest(currentOwnerUsername, newOwnerUsername, storeId);
            logger.info(String.format("User %s nominated User %s as owner of store %d.", currentOwnerUsername, newOwnerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreManagerRequest(String token, String currentOwnerUsername, String newManagerUsername, int storeId) {
        try {
            checkToken(token, currentOwnerUsername);
            storeFacade.sendStoreManagerRequest(currentOwnerUsername, newManagerUsername, storeId);
            logger.info(String.format("User %s nominated User %s as manager of store %d.", currentOwnerUsername, newManagerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreManagerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptRequest(String token, String newUsername, int storeId) {
        try {
            checkToken(token, newUsername);
            userFacade.accept(newUsername, storeId);
            logger.info(String.format("User %s accepted nomination in store %d.", newUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newUsername));
        }
        catch (Exception e) {
            logger.error("acceptStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreOrderHistory(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            String history = storeFacade.getStoreOrderHistory(username, storeId);
            logger.info(String.format("User %s got order history from store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(history));
        }
        catch (Exception e) {
            logger.error("getStoreOrderHistory: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreInfo(String token, String username, int storeId) {
        try {
            if(username != null)
                checkToken(token, username);
            Set<String> fields = Set.of("storeId", "storeName");
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("filter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

            StoreDTO storeDTO = storeFacade.getStoreInfo(username, storeId);
            String json = objectMapper.writer(filterProvider).writeValueAsString(storeDTO);
            logger.info(String.format("A user got store info of store %d.", storeId));
            return Response.createResponse(false, json);
        }
        catch (Exception e) {
            logger.error("getStoreInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreProductsInfo(String token, String username, int storeId, String category, double price, double minProductRank) {
        try {
            if(username != null)
                checkToken(token, username);

            Map<ProductDTO, Integer> productDTOsAmounts = storeFacade.getProductsInfo(username, storeId, category, price, minProductRank);
            logger.info(String.format("A user got products info of store %d.", storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(productDTOsAmounts));
        }
        catch (Exception e) {
            logger.error("getProductsInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreProductAmount(String token, String username, int storeId, int productId) {
        try {
            if(username != null)
                checkToken(token, username);

            int amount = storeFacade.getProductAmount(username, storeId, productId);
            logger.info(String.format("A user got product %d amount in store %d.", productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(amount));
        }
        catch (Exception e) {
            logger.error("getProductsInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addSellerToStore(String token, int storeId, String adderUsername, String sellerUsername) {
        try {
            checkToken(token, adderUsername);
            storeFacade.addSeller(storeId, adderUsername, sellerUsername);
            logger.info(String.format("User %s added User %s as a seller to store %d.", adderUsername, sellerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellerUsername));
        }
        catch (Exception e) {
            logger.error("addSellerToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addBuyPolicy(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            int policyId = storeFacade.addBuyPolicy(username, storeId);
            logger.info(String.format("User %s added buy policy with id %d to store %d.", username, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addBuyPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addDiscountPolicy(String token, String username, int storeId) {
        try {
            checkToken(token, username);
            int policyId = storeFacade.addDiscountPolicy(username, storeId);
            logger.info(String.format("User %s added discount policy with id %d to store %d.", username, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addDiscountPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response changeManagerPermission(String token, String currentOwnerUsername, String newManagerUsername, int storeId, Set<Permission> permission) {
        try {
            checkToken(token, currentOwnerUsername);
            storeFacade.addManagerPermission(currentOwnerUsername, newManagerUsername, storeId, permission);
            logger.info(String.format("User %s added permission to user %s in store %d", currentOwnerUsername, newManagerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newManagerUsername));
        }
        catch (Exception e) {
            logger.error("addManagerPermission: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

}
