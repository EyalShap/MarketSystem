package com.sadna.sadnamarket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.service.MarketServiceTestAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GuestTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

    @BeforeEach
    void clean(){
        bridge.reset();
    }
    @Test
    void guestEnterTest() {
        Response resp = bridge.guestEnterSystem();
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        Assertions.assertFalse(resp.getDataJson().isEmpty());
    }

    @Test
    void guestLeaveTest() {
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.guestLeaveSystem(uuid);
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        resp = bridge.guestCartExists(uuid);
        Assertions.assertEquals(resp.getDataJson(), "false");
    }

    @Test
    void guestLeaveTwiceTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.guestLeaveSystem(uuid);
        resp = bridge.guestLeaveSystem(resp.getDataJson());
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void guestSignUpTest() {
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String username = "JohnDoe";
        resp = bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        String token = resp.getDataJson();
        resp = bridge.memberExists(username);
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals(resp.getDataJson(), "true");
        resp = bridge.authenticate(token,username);
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals(resp.getDataJson(), "true");
    }

    @Test
    void guestSignupMemberAlreadyExistsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String username = "JohnDoe";
        resp = bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        resp = bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
        resp = bridge.signUp(uuid, "jane@john.com", username, "thisIsn'tWhatAHashLooksLikeEither");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void guestLoginTest() {
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String username = "JohnDoe";
        resp = bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        String token = resp.getDataJson();
        bridge.logout(username);
        resp = bridge.login(username, "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        String token1 = resp.getDataJson();
        Assertions.assertNotEquals("", token1);
    }

    @Test
    void guestLoginAlreadyLoggedInTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String username = "JohnDoe";
        bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        resp = bridge.login(username, "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void guestLoginWrongPasswordTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String username = "JohnDoe";
        resp = bridge.signUp(uuid, "john@john.com", username, "thisIsn'tWhatAHashLooksLike");
        String token = resp.getDataJson();
        bridge.logout(username);
        resp = bridge.login(username, "thisPasswordIsVeryWrong");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void getStoreDataTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getStoreData("", "", storeId));
        try{
            resp = bridge.getStoreData("", null, storeId);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, StoreDTO.class));
            StoreDTO storeDTO = objectMapper.readValue(json, StoreDTO.class);
            Assertions.assertEquals(storeId, storeDTO.getStoreId());
            Assertions.assertEquals("TestStore", storeDTO.getStoreName());
        }catch (Exception e){

        }
    }

    @Test
    void getStoreDataDoesntExistTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        try{
            resp = bridge.getStoreData("", null, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getClosedStoreDataNotOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        try{
            bridge.closeStore(ownerToken, ownerUsername, storeId);
            resp = bridge.getStoreData("", null, storeId);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getClosedStoreDataOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getStoreData(ownerToken, ownerUsername, storeId));
        try{
            bridge.closeStore(ownerToken, ownerUsername, storeId);
            resp = bridge.getStoreData("", null, storeId);
            Assertions.assertTrue(resp.getError());
            resp = bridge.getStoreData(ownerToken, ownerUsername, storeId);
            Assertions.assertFalse(resp.getError());
            String json1 = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json1, StoreDTO.class));
            StoreDTO storeDTO = objectMapper.readValue(json1, StoreDTO.class);
            Assertions.assertEquals(storeId, storeDTO.getStoreId());
            Assertions.assertEquals("TestStore", storeDTO.getStoreName());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getProductData("", "", productId));
        try{
            resp = bridge.getProductData("", "", productId);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, ProductDTO.class));
            ProductDTO productDTO = objectMapper.readValue(json, ProductDTO.class);
            Assertions.assertEquals(productId, productDTO.getProductID());
            Assertions.assertEquals("TestProduct", productDTO.getProductName());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataDoesntExistTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        try{
            resp = bridge.getProductData("", "", Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataClosedStoreNotOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        try{
            bridge.closeStore(ownerToken, ownerUsername, storeId);
            resp = bridge.getProductData("", "", productId);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataClosedStoreOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getProductData(ownerToken, ownerUsername, productId));
        try{
            bridge.closeStore(ownerToken, ownerUsername, storeId);
            resp = bridge.getProductData(ownerToken, ownerUsername, productId);
            Assertions.assertFalse(resp.getError());
            String json1 = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json1, ProductDTO.class));
            ProductDTO productDTO = objectMapper.readValue(json1, ProductDTO.class);
            Assertions.assertEquals(productId, productDTO.getProductID());
            Assertions.assertEquals("TestProduct", productDTO.getProductName());
        }catch (Exception e){

        }
    }

    @Test
    void searchProductsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "NotTestProduct", 100.3, "NotProduct"));
        try{
            resp = bridge.searchProduct("TestProduct", -1, -1, "Product", -1, -1);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { }));
            List<ProductDTO> results = objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { });
            Assertions.assertEquals(1, results.size());
            Assertions.assertEquals(productId, results.get(0).getProductID());
            Assertions.assertEquals("TestProduct", results.get(0).getProductName());
        }catch (Exception e){

        }
    }

    @Test
    void searchAndFilterProductsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 200, "NotProduct"));
        try{
            resp = bridge.searchProduct(null, 100, 105, "Product", -1, -1);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { }));
            List<ProductDTO> results = objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { });
            Assertions.assertEquals(1, results.size());
            Assertions.assertEquals(productId, results.get(0).getProductID());
            Assertions.assertEquals("Product", results.get(0).getProductCategory());
        }catch (Exception e){

        }
    }

    @Test
    void searchAndFilterProductsNoResultsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 200, "NotProduct"));
        try{
            resp = bridge.searchProduct(null, 500, 505, "Product", -1, -1);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void searchProductsInStoreTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "NotTestProduct", 100.3, "NotProduct"));
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore2 Boogaloo");
        int storeId2 = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId2,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        try{
            resp = bridge.searchProductInStore(storeId, "TestProduct", -1, -1, "Product", -1);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, new TypeReference<Map<ProductDTO, Integer>>() { }));
            Map<ProductDTO, Integer> results = objectMapper.readValue(json, new TypeReference<Map<ProductDTO, Integer>>() { });
            Assertions.assertEquals(1, results.keySet().size());
            Assertions.assertEquals(productId, results.keySet().iterator().next().getProductID());
            Assertions.assertEquals("TestProduct", results.keySet().iterator().next().getProductName());
        }catch (Exception e){

        }
    }

    @Test
    void searchAndFilterProductsInStoreTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 200, "NotProduct"));
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore2 Boogaloo");
        int storeId2 = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId2,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        try{
            resp = bridge.searchProductInStore(storeId, null, 100, 105, "Product", -1);
            Assertions.assertFalse(resp.getError());
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { }));
            List<ProductDTO> results = objectMapper.readValue(json, new TypeReference<List<ProductDTO>>() { });
            Assertions.assertEquals(1, results.size());
            Assertions.assertEquals(productId, results.get(0).getProductID());
            Assertions.assertEquals("Product", results.get(0).getProductCategory());
        }catch (Exception e){

        }
    }

    @Test
    void searchAndFilterProductsInStoreNoResultsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 200, "NotProduct"));
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore2 Boogaloo");
        int storeId2 = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId2,
                new ProductDTO(-1, "TestProduct", 500.5, "Product"));
        try{
            resp = bridge.searchProductInStore(storeId, "TestProduct", 500, 505, "Product", -1);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void searchAndFilterProductsInStoreDoesntExistTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 200, "NotProduct"));
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore2 Boogaloo");
        int storeId2 = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerUsername, storeId2,
                new ProductDTO(-1, "TestProduct", 500.5, "Product"));
        try{
            resp = bridge.searchProductInStore(Integer.MAX_VALUE, "TestProduct", 500, 505, "Product", -1);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void addToBasketTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            resp = bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            Assertions.assertFalse(resp.getError());
            Assertions.assertEquals("true", resp.getDataJson());
            resp = bridge.getGuestBasket(uuid, storeId);
            String json = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, new TypeReference<Map<Integer,Integer>>() { }));
            Map<Integer, Integer> basket = objectMapper.readValue(json, new TypeReference<Map<Integer,Integer>>() { });
            Assertions.assertTrue(basket.containsKey(productId));
            Assertions.assertEquals(1, basket.get(productId));
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            resp = bridge.getGuestBasket(uuid, storeId);
            String json1 = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json1, new TypeReference<Map<Integer,Integer>>() { }));
             basket = objectMapper.readValue(json1, new TypeReference<Map<Integer,Integer>>() { });
            Assertions.assertEquals(2, basket.get(productId));
        }catch (Exception e){

        }
    }

    @Test
    void addToBasketNotEnoughAmountTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            resp = bridge.addProductToBasketGuest(uuid, storeId, productId, 3);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void addToBasketProductDoesntExistTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            resp = bridge.addProductToBasketGuest(uuid, storeId, Integer.MAX_VALUE, 3);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void editBasketTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            resp = bridge.setGuestBasketProductAmount(uuid, storeId, productId, 2);
            Assertions.assertFalse(resp.getError());
            Assertions.assertEquals("true", resp.getDataJson());
            resp = bridge.getGuestBasket(uuid, storeId);
            String json = resp.getDataJson();
            Map<Integer, Integer> basket = objectMapper.readValue(json, new TypeReference<Map<Integer,Integer>>() { });
            Assertions.assertTrue(basket.containsKey(productId));
            Assertions.assertEquals(2, basket.get(productId));
        }catch (Exception e){

        }
    }

    @Test
    void editBasketIllegalTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            resp = bridge.setGuestBasketProductAmount(uuid, storeId, productId, 3);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void editBasketProductRemovedTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            bridge.removeProductFromStore(ownerToken, ownerUsername, storeId, productId);
            resp = bridge.setGuestBasketProductAmount(uuid, storeId, productId, 2);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void buyCartTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            resp = bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                    new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                            "+97254-989-4939", "jimjimmy@gmail.com"));
            Assertions.assertFalse(resp.getError());
            Assertions.assertEquals(bridge.getStoreProductAmount(storeId, productId).getDataJson(), "1");
        }catch (Exception e){

        }
    }

    @Test
    void buyCartIncorrectDetailsTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 2);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            resp = bridge.buyCartGuest(uuid, new CreditCardDTO("1234234534564567", "103", new Date(), "123456789"),
                    new AddressDTO("Israel", "Petah Tikvah", "Benyamin 12", "Apartment 12", "5", "Jim Jimmy",
                            "+97254-989-4939", "jimjimmy@gmail"));
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void buyCartStoreNotEnoughProductTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 5);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 5);
            bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 4);
            resp = bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                    new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                            "+97254-989-4939", "jimjimmy@gmail.com"));
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void buyCartStoreCannotSupplyTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 5);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 2);
            resp = bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                    new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                            "+97254-989-4939", "jimjimmy@gmail.com"));
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void buyCartStoreCannotPayTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(false);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(false);

        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        String ownerUsername = "GuyStore";
        resp = bridge.signUp(uuid, "guywhoowns@store.com", ownerUsername, "password");
        String ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 5);
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid, storeId, productId, 2);
            resp = bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                    new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                            "+97254-989-4939", "jimjimmy@gmail.com"));
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }
}
