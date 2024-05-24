package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.orders.IOrderRepository;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.users.IUserRepository;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.domain.users.MemoryRepo;
import com.sadna.sadnamarket.domain.users.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(MarketService.class);

    public MarketService(IStoreRepository storeRepository) {
        this.userFacade = new UserFacade(new MemoryRepo());
        this.productFacade = new ProductFacade();
        this.orderFacade = new OrderFacade(new MemoryOrderRepository());
        this.storeFacade = new StoreFacade(storeRepository);
        this.buyPolicyFacade = new BuyPolicyFacade();
        this.discountPolicyFacade = new DiscountPolicyFacade();

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

    // returns id of the created store
    public Response createStore(String founderUsername, String storeName) {
        try {
            int newStoreId = storeFacade.createStore(founderUsername, storeName); // will throw an exception if the store already exists
            logger.info(String.format("User %d created a store with id %d.", founderUsername, newStoreId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            logger.error("createStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(String token, String username, int storeId, String productName, int productQuantity, int productPrice, String category) {
        try {
            int newProductId = storeFacade.addProductToStore(username, storeId, productName, productQuantity, productPrice, category);
            logger.info(String.format("User %d added product %d to store %d.", username, newProductId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newProductId));
        }
        catch (Exception e) {
            logger.error("addProductToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response deleteProductFromStore(String token, String username, int storeId, int productId) {
        try {
            int deletedProductId = storeFacade.deleteProduct(username, storeId, productId);
            logger.info(String.format("User %d deleted product %d from store %d.", username, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(deletedProductId));
        }
        catch (Exception e) {
            logger.error("deleteProductFromStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response updateProductInStore(String token, String username, int storeId, int productId, String newProductName, int newQuantity, int newPrice, String newCategory) {
        try {
            int updateProductId = storeFacade.updateProduct(username, storeId, productId, newProductName, newQuantity, newPrice, newCategory);
            logger.info(String.format("User %d updated product %d in store %d.", username, productId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(updateProductId));
        }
        catch (Exception e) {
            logger.error("updateProductInStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response closeStore(String token, String username, int storeId) {
        try {
            boolean storeClosed = storeFacade.closeStore(username, storeId);
            logger.info(String.format("User %d closed store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(storeClosed));
        }
        catch (Exception e) {
            logger.error("closeStore: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOwners(String token, String username, int storeId) {
        try {
            List<MemberDTO> owners = storeFacade.getOwners(username, storeId);
            logger.info(String.format("User %d got owners of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(owners));
        }
        catch (Exception e) {
            logger.error("getOwners: " + e.getMessage());

            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getManagers(String token, String username, int storeId) {
        try {
            List<MemberDTO> managers = storeFacade.getManagers(username, storeId);
            logger.info(String.format("User %d got managers of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(managers));
        }
        catch (Exception e) {
            logger.error("getManagers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getSellers(String token, String username, int storeId) {
        try {
            List<MemberDTO> sellers = storeFacade.getSellers(username, storeId);
            logger.info(String.format("User %d got sellers of store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellers));
        }
        catch (Exception e) {
            logger.error("getSellers: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreOwnerRequest(String currentOwnerUsername, String newOwnerUsername, int storeId) {
        try {
            storeFacade.sendStoreOwnerRequest(currentOwnerUsername, newOwnerUsername, storeId);
            logger.info(String.format("User %d nominated user %d as owner of store %d.", currentOwnerUsername, newOwnerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response sendStoreManagerRequest(String currentOwnerUsername, String newManagerUsername, int storeId, Set<Integer> permissions) {
        try {
            storeFacade.sendStoreManagerRequest(currentOwnerUsername, newManagerUsername, storeId, permissions);
            logger.info(String.format("User %d nominated user %d as manager of store %d.", currentOwnerUsername, newManagerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("sendStoreManagerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreOwnerRequest(String newOwnerUsername, int storeId) {
        try {
            userFacade.acceptStoreOwnerRequest(newOwnerUsername, storeId);
            logger.info(String.format("User %d accepted owner nomination in store %d.", newOwnerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newOwnerUsername));
        }
        catch (Exception e) {
            logger.error("acceptStoreOwnerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptStoreManagerRequest(String newManagerUsername, int storeId) {
        try {
            userFacade.acceptStoreManagerRequest(newManagerUsername, storeId);
            logger.info(String.format("User %d accepted manager nomination in store %d.", newManagerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newManagerUsername));
        }
        catch (Exception e) {
            logger.error("acceptStoreManagerRequest: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreOrderHistory(String token, String username, int storeId) {
        try {
            String history = storeFacade.getStoreOrderHistory(username, storeId);
            logger.info(String.format("User %d got order history from store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(history));
        }
        catch (Exception e) {
            logger.error("getStoreOrderHistory: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreInfo(String token, String username, int storeId) {
        try {
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

    public Response getProductsInfo(String token, String username, int storeId) {
        try {
            Map<String, Integer> productDTOs = storeFacade.getProductsInfo(username, storeId);
            logger.info(String.format("A user got products info of store %d.", storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(productDTOs));
        }
        catch (Exception e) {
            logger.error("getProductsInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addSellerToStore(int storeId, String adderUsername, String sellerUsername) {
        try {
            storeFacade.addSeller(storeId, adderUsername, sellerUsername);
            logger.info(String.format("User %d added user %d as a seller to store %d.", adderUsername, sellerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(sellerUsername));
        }
        catch (Exception e) {
            logger.error("addSellerToStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addBuyPolicy(String token, String username, int storeId) {
        try {
            int policyId = storeFacade.addBuyPolicy(username, storeId);
            logger.info(String.format("User %d added buy policy with id %d to store %d.", username, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addBuyPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addDiscountPolicy(String token, String username, int storeId) {
        try {
            int policyId = storeFacade.addDiscountPolicy(username, storeId);
            logger.info(String.format("User %d added discount policy with id %d to store %d.", username, policyId, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(policyId));
        }
        catch (Exception e) {
            logger.error("addDiscountPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }
}
