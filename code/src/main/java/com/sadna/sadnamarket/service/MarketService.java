package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
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
        this.userFacade = new UserFacade(new MemoryRepo(),storeFacade, orderFacade);
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

    public static MarketService getNewInstance() {
        return new MarketService(new MemoryStoreRepository());
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

    public Response setStoreBankAccount(String token, String username, int storeId, BankAccountDTO bankAccount) {
        try {
            checkToken(token, username);
            storeFacade.setStoreBankAccount(username, storeId, bankAccount);
            logger.info(String.format("User %s changed store %d bank account.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
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
            List<OrderDTO> history = storeFacade.getStoreOrderHistory(username, storeId);
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
            if(productDTOsAmounts.isEmpty()){
                logger.error("getProductsInfo: No products found");
                return Response.createResponse(true, "No products found");
            }
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

    public Response addBuyPolicy(String token, String username, int storeId, String args) {
        try {
            checkToken(token, username);
            storeFacade.addBuyPolicy(username, storeId, args);
            logger.info(String.format("User %s added buy policy to store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
        }
        catch (Exception e) {
            logger.error("addBuyPolicy: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addDiscountPolicy(String token, String username, int storeId, String args) {
        try {
            checkToken(token, username);
            storeFacade.addDiscountPolicy(username, storeId, args);
            logger.info(String.format("User %s added discount policy to store %d.", username, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(true));
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

    public Response getManagerPermissions(String token, String currentOwnerUsername, String managerUsername, int storeId) {
        try {
            checkToken(token, currentOwnerUsername);
            if(!storeFacade.getIsManager(currentOwnerUsername, storeId, managerUsername)){
                logger.error("getManagerPermissions: User " + managerUsername + " isn't a manager");
                return Response.createResponse(true, "User isn't a manager");
            }
            logger.info(String.format("User %s got permission of user %s in store %d", currentOwnerUsername, managerUsername, storeId));
            return Response.createResponse(false, objectMapper.writeValueAsString(userFacade.getManagerPermissions(currentOwnerUsername, managerUsername, storeId)));
        }
        catch (Exception e) {
            logger.error("getManagerPermissions: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
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

    public Response memberExists(String username){
        try{
            boolean res = userFacade.isExist(username);
            return Response.createResponse(false, String.valueOf(res));

        }catch(Exception e){
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response authenticate(String token, String username){
        try{
            checkToken(token, username);
            return Response.createResponse(false, "true");

        }catch(Exception e){
            return Response.createResponse(false, "false");
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

    public Response getUserCart(int guestId) {
        try {
            List<CartItemDTO> items = userFacade.getCartItems(guestId);
            return Response.createResponse(false, objectMapper.writeValueAsString(items));
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
            List<String> orders=userFacade.getUserOrders(username);
            return Response.createResponse(false,objectMapper.writeValueAsString(orders));
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }

    public Response getOrderDTOHistory(String username) {
        try {
            List<OrderDTO> orders=userFacade.getUserOrderDTOs(username);
            return Response.createResponse(false,objectMapper.writeValueAsString(orders));
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
    public Response purchaseCart(String username,CreditCardDTO creditCard, String country, String city, String addressLine1, String addressLine2, String zipCode, String ordererName, String contactPhone, String contactEmail, String name) {
        try {
            userFacade.purchaseCart(username,creditCard,country,city,addressLine1,addressLine2,zipCode,ordererName,contactPhone,contactEmail);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response viewCart(int guestId) {
        try {
            userFacade.viewCart(guestId);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response purchaseCart(int guestId,CreditCardDTO creditCard, String country, String city, String addressLine1, String addressLine2, String zipCode, String ordererName, String contactPhone, String contactEmail, String name) {
        try {
            userFacade.purchaseCart(guestId,creditCard,country,city,addressLine1,addressLine2,zipCode,ordererName,contactPhone,contactEmail,name);
            return Response.createResponse();
        } catch (Exception e) { 
            return Response.createResponse(true, e.getMessage());
       
        }
    }
    public Response getIsOwner(String token, String username, int storeId, String ownerUsername) {
        checkToken(token, username);
        try {
            return Response.createResponse(false, String.valueOf(storeFacade.getIsOwner(username, storeId, ownerUsername)));
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getIsManager(String token, String username, int storeId, String managerUsername) {
        checkToken(token, username);
        try {
            return Response.createResponse(false, String.valueOf(storeFacade.getIsManager(username, storeId, managerUsername)));
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }
}
