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
import com.sadna.sadnamarket.domain.supply.AddressDTO;
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
        this.discountPolicyFacade = new DiscountPolicyFacade(productFacade);
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
        instance =new MarketService(new MemoryStoreRepository());
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
            addBuyPolicy(token, founderUsername, newStoreId,"");
            addDiscountPolicy(token, founderUsername, newStoreId,"");
            logger.info(String.format("User %s created a store with id %d.", founderUsername, newStoreId));
            return Response.createResponse(false, objectMapper.writeValueAsString(newStoreId));
        }
        catch (Exception e) {
            logger.error("createStore: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToStore(String token, String username, int storeId, String productName, int productQuantity, int productPrice, String category, double rank) {
        try {
            checkToken(token, username);
            int newProductId = storeFacade.addProductToStore(username, storeId, productName, productQuantity, productPrice, category, rank);
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

    public Response updateProductInStore(String token, String username, int storeId, int productId, String newProductName, int newQuantity, int newPrice, String newCategory, double newRank) {
        try {
            checkToken(token, username);
            int updateProductId = storeFacade.updateProduct(username, storeId, productId, newProductName, newQuantity, newPrice, newCategory, newRank);
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
  
  public Response getProductInfo(String token, String username, int productId) {
        try {
            if(username != null)
                checkToken(token, username);

            ProductDTO productDTO = storeFacade.getProductInfo(username, productId);
            String json = objectMapper.writeValueAsString(productDTO);
            logger.info(String.format("A user got product info of product %d.", productId));
            return Response.createResponse(false, json);
        }
        catch (Exception e) {
            logger.error("getStoreInfo: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getStoreProductsInfo(String token, String username, int storeId, String productName, String category, double price, double minProductRank) {
        try {
            if(username != null)
                checkToken(token, username);

            Map<ProductDTO, Integer> productDTOsAmounts = storeFacade.getProductsInfo(username, storeId, productName, category, price, minProductRank);
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
            logger.info("user {} tries to login", username);
            String token= authFacade.login(username, password);
            logger.info("user {} logged in", username);
            return Response.createResponse(false, token);

        }catch(Exception e){
            logger.error("error in login: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }
    public Response logout(String username){
        try{
            logger.info(username, username);
            userFacade.logout(username);
            logger.info(username);
            return Response.createResponse();

        }catch(Exception e){
            logger.error("error in logout: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }
        public Response exitGuest(int guestId){
            try{
                logger.info("guest {} tries to exit", guestId);
                userFacade.exitGuest(guestId);
                logger.info("guest {} exited", guestId);
                return Response.createResponse();

            }catch(Exception e){
                logger.error("error in exitGuest: "+e.getMessage());
                return Response.createResponse(true, e.getMessage());
            }
        }
    public Response enterAsGuest(){
        try{
            logger.info("guest tries to enter");
            int guestId=userFacade.enterAsGuest();
            logger.info("guest entered with id {}", guestId);
            return Response.createResponse(guestId);
        }catch(Exception e){
            logger.error("error in enterAsGuest: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response register(String username, String password,String firstName, String lastName,String emailAddress,String phoneNumber){
        try{
            logger.info("user {} tries to register", username);
            authFacade.register(username,password,firstName, lastName, emailAddress, phoneNumber);
            logger.info("user {} registered", username);
            return Response.createResponse();

        }catch(Exception e){
            logger.error("error in register: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response memberExists(String username){
        try{
            logger.info("user {} tries to check if exists", username);
            boolean res = userFacade.isExist(username);
            logger.info("user {} exists: {}", username, res);
            return Response.createResponse(false, String.valueOf(res));
        }catch(Exception e){
            logger.error("error in memberExists: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response authenticate(String token, String username){
        try{
            logger.info("user {} tries to authenticate", username);
            checkToken(token, username);
            logger.info("user {} authenticated", username);
            return Response.createResponse(false, "true");

        }catch(Exception e){
            logger.error("error in authenticate: "+e.getMessage());
            return Response.createResponse(false, "false");
        }
    }

    public Response setFirstName(String username, String firstName) {
        try {
            logger.info("user {} tries to set first name= {}", username,firstName);
            userFacade.setFirstName(username, firstName);
            logger.info("user {} set first name {}", username,firstName);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("error in setFirstName: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setLastName(String username, String lastName) {
        try {
            logger.info("user {} tries to set last name= {}", username,lastName);
            userFacade.setLastName(username, lastName);
            logger.info("user {} set last name= {}", username,lastName);
            return Response.createResponse();
        } catch (Exception e) {
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setEmailAddress(String username, String emailAddress) {
        try {
            logger.info("user {} tries to set email address= {}", username,emailAddress);
            userFacade.setEmailAddress(username, emailAddress);
            logger.info("user {} set email address= {}", username,emailAddress);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("error in setEmailAddress: "+e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setPhoneNumber(String username, String phoneNumber) {
        logger.info("Setting phoneNumber for username: {}", username);
        try {
            userFacade.setPhoneNumber(username, phoneNumber);
            logger.info("Set phoneNumber successful for username: {}", username);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Set phoneNumber failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToCart(String username, int storeId, int productId, int amount) {
        logger.info("Adding product to cart for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
        try {
            if (amount <= 0) {
                logger.error("Amount should be above 0 for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
                throw new IllegalArgumentException("amount should be above 0");
            }
            userFacade.addProductToCart(username, storeId, productId, amount);
            logger.info("Add product to cart successful for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Add product to cart failed for username: {}, storeId: {}, productId: {}, amount: {}. Error: {}", username, storeId, productId, amount, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response addProductToCart(int guestId, int storeId, int productId, int amount) {
        logger.info("Adding product to cart for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
        try {
            if (amount <= 0) {
                logger.error("Amount should be above 0 for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
                throw new IllegalArgumentException("amount should be above 0");
            }
            userFacade.addProductToCart(guestId, storeId, productId, amount);
            logger.info("Add product to cart successful for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Add product to cart failed for guestId: {}, storeId: {}, productId: {}, amount: {}. Error: {}", guestId, storeId, productId, amount, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response removeProductFromCart(String username, int storeId, int productId) {
        logger.info("Removing product from cart for username: {}, storeId: {}, productId: {}", username, storeId, productId);
        try {
            userFacade.removeProductFromCart(username, storeId, productId);
            logger.info("Remove product from cart successful for username: {}, storeId: {}, productId: {}", username, storeId, productId);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Remove product from cart failed for username: {}, storeId: {}, productId: {}. Error: {}", username, storeId, productId, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response removeProductFromCart(int guestId, int storeId, int productId) {
        logger.info("Removing product from cart for guestId: {}, storeId: {}, productId: {}", guestId, storeId, productId);
        try {
            userFacade.removeProductFromCart(guestId, storeId, productId);
            logger.info("Remove product from cart successful for guestId: {}, storeId: {}, productId: {}", guestId, storeId, productId);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Remove product from cart failed for guestId: {}, storeId: {}, productId: {}. Error: {}", guestId, storeId, productId, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response changeQuantityCart(String username, int storeId, int productId, int amount) {
        logger.info("Changing quantity in cart for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
        try {
            if (amount <= 0) {
                logger.error("Amount should be above 0 for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
                throw new IllegalArgumentException("amount should be above 0");
            }
            userFacade.changeQuantityCart(username, storeId, productId, amount);
            logger.info("Change quantity in cart successful for username: {}, storeId: {}, productId: {}, amount: {}", username, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Change quantity in cart failed for username: {}, storeId: {}, productId: {}, amount: {}. Error: {}", username, storeId, productId, amount, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response changeQuantityCart(int guestId, int storeId, int productId, int amount) {
        logger.info("Changing quantity in cart for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
        try {
            if (amount <= 0) {
                logger.error("Amount should be above 0 for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
                throw new IllegalArgumentException("amount should be above 0");
            }
            userFacade.changeQuantityCart(guestId, storeId, productId, amount);
            logger.info("Change quantity in cart successful for guestId: {}, storeId: {}, productId: {}, amount: {}", guestId, storeId, productId, amount);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Change quantity in cart failed for guestId: {}, storeId: {}, productId: {}, amount: {}. Error: {}", guestId, storeId, productId, amount, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getUserCart(int guestId) {
        logger.info("Getting user cart for guestId: {}", guestId);
        try {
            List<CartItemDTO> items = userFacade.getCartItems(guestId);
            logger.info("Get user cart successful for guestId: {}", guestId);
            return Response.createResponse(false, objectMapper.writeValueAsString(items));
        } catch (Exception e) {
            logger.error("Get user cart failed for guestId: {}. Error: {}", guestId, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response acceptRequest(String acceptingName, int requestID) {
        logger.info("Accepting request for acceptingName: {}, requestID: {}", acceptingName, requestID);
        try {
            userFacade.accept(acceptingName, requestID);
            logger.info("Accept request successful for acceptingName: {}, requestID: {}", acceptingName, requestID);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Accept request failed for acceptingName: {}, requestID: {}. Error: {}", acceptingName, requestID, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response setSystemAdminstor(String username) {
        logger.info("Setting system administrator for username: {}", username);
        try {
            userFacade.setSystemManagerUserName(username);
            logger.info("Set system administrator successful for username: {}", username);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Set system administrator failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response leaveRole(String username, int storeId) {
        logger.info("Leaving role for username: {}, storeId: {}", username, storeId);
        try {
            userFacade.leaveRole(username, storeId);
            logger.info("Leave role successful for username: {}, storeId: {}", username, storeId);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Leave role failed for username: {}, storeId: {}. Error: {}", username, storeId, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOrderHistory(String username) {
        logger.info("Getting order history for username: {}", username);
        try {
            List<String> orders = userFacade.getUserOrders(username);
            logger.info("Get order history successful for username: {}", username);
            return Response.createResponse(false, objectMapper.writeValueAsString(orders));
        } catch (Exception e) {
            logger.error("Get order history failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getOrderDTOHistory(String username) {
        logger.info("Getting order DTO history for username: {}", username);
        try {
            List<OrderDTO> orders = userFacade.getUserOrderDTOs(username);
            logger.info("Get order DTO history successful for username: {}", username);
            return Response.createResponse(false, objectMapper.writeValueAsString(orders));
        } catch (Exception e) {
            logger.error("Get order DTO history failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response getAllOrderDTOHistory(String username) {
        logger.info("Getting all order DTO history for username: {}", username);
        try {
            List<OrderDTO> orders = userFacade.getAllOrders(username);
            logger.info("Get all order DTO history successful for username: {}", username);
            return Response.createResponse(false, objectMapper.writeValueAsString(orders));
        } catch (Exception e) {
            logger.error("Get all order DTO history failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response viewCart(String username) {
        logger.info("Viewing cart for username: {}", username);
        try {
            userFacade.viewCart(username);
            logger.info("View cart successful for username: {}", username);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("View cart failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response purchaseCart(String username, CreditCardDTO creditCard, AddressDTO addressDTO) {
        logger.info("Purchasing cart for username: {}, creditCard: {}, addressDTO: {}", username, creditCard, addressDTO);
        try {
            userFacade.purchaseCart(username, creditCard, addressDTO);
            logger.info("Purchase cart successful for username: {}", username);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Purchase cart failed for username: {}. Error: {}", username, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response viewCart(int guestId) {
        logger.info("Viewing cart for guestId: {}", guestId);
        try {
            userFacade.viewCart(guestId);
            logger.info("View cart successful for guestId: {}", guestId);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("View cart failed for guestId: {}. Error: {}", guestId, e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

    public Response purchaseCart(int guestId, CreditCardDTO creditCard, AddressDTO addressDTO) {
        logger.info("Purchasing cart for guestId: {}, creditCard: {}, addressDTO: {}", guestId, creditCard, addressDTO);
        try {
            userFacade.purchaseCart(guestId, creditCard, addressDTO);
            logger.info("Purchase cart successful for guestId: {}", guestId);
            return Response.createResponse();
        } catch (Exception e) {
            logger.error("Purchase cart failed for guestId: {}. Error: {}", guestId, e.getMessage());
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

    public Response getAllProducts(String token, String username) {
        try {
            if (username != null)
                checkToken(token, username);

            List<ProductDTO> productDTOs = productFacade.getAllProducts();
            logger.info(String.format("User %s got all market products", username));
            return Response.createResponse(false, objectMapper.writeValueAsString(productDTOs));
        } catch (Exception e) {
            logger.error("getAllProducts: " + e.getMessage());
            return Response.createResponse(true, e.getMessage());
        }
    }

}
