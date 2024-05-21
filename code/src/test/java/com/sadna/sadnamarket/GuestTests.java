package com.sadna.sadnamarket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.service.MarketServiceTestAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        resp = bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        String resJson = resp.getDataJson();
        Assertions.assertDoesNotThrow(() -> objectMapper.readTree(resJson));
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        resp = bridge.memberExists(node.get("userId").asInt());
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals(resp.getDataJson(), "true");
        resp = bridge.tokenValid(node.get("token").asText());
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals(resp.getDataJson(), "true");
    }

    @Test
    void guestSignupMemberAlreadyExistsTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        resp = bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
        resp = bridge.signUp(uuid, "john@john.com", "Jane Doe", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void guestLoginTest() {
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        bridge.logout(node.get("token").asText());
        resp = bridge.login("john@john.com", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertFalse(resp.getError());
        String resJson1 = resp.getDataJson();
        Assertions.assertDoesNotThrow(() -> objectMapper.readTree(resJson1));
    }

    @Test
    void guestLoginAlreadyLoggedInTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        resp = bridge.login("john@john.com", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void guestLoginWrongPasswordTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "john@john.com", "John Doe", "thisIsn'tWhatAHashLooksLike");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        bridge.logout(node.get("token").asText());
        resp = bridge.login("john@john.com", "thisPasswordIsWrong");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void getStoreDataTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getStoreData("", -1, storeId));
        try{
            resp = bridge.getStoreData("", -1, storeId);
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
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        bridge.openStore(ownerToken, ownerId, "TestStore");
        try{
            resp = bridge.getStoreData("", -1, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getClosedStoreDataNotOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        try{
            bridge.closeStore(ownerToken, ownerId, storeId);
            resp = bridge.getStoreData("", -1, storeId);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getClosedStoreDataOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getStoreData(ownerToken, ownerId, storeId));
        try{
            bridge.closeStore(ownerToken, ownerId, storeId);
            resp = bridge.getStoreData("", -1, storeId);
            Assertions.assertTrue(resp.getError());
            resp = bridge.getStoreData(ownerToken, ownerId, storeId);
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
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerId, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product", false));
        int productId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getProductData("", -1, productId));
        try{
            resp = bridge.getProductData("", -1, productId);
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
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        bridge.addProductToStore(ownerToken, ownerId, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product", false));
        try{
            resp = bridge.getProductData("", -1, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataClosedStoreNotOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerId, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product", false));
        int productId = Integer.parseInt(resp.getDataJson());
        try{
            bridge.closeStore(ownerToken, ownerId, storeId);
            resp = bridge.getProductData("", -1, productId);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void getProductDataClosedStoreOwnerTest(){
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "guywhoowns@store.com", "Guy Store", "password");
        String resJson = resp.getDataJson();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(resJson);
        }catch (Exception e){

        }
        String ownerToken = node.get("token").asText();
        int ownerId = node.get("userId").asInt();
        resp = bridge.openStore(ownerToken, ownerId, "TestStore");
        int storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(ownerToken, ownerId, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product", false));
        int productId = Integer.parseInt(resp.getDataJson());
        Assertions.assertDoesNotThrow(() -> bridge.getProductData(ownerToken, ownerId, productId));
        try{
            bridge.closeStore(ownerToken, ownerId, storeId);
            resp = bridge.getProductData(ownerToken, ownerId, productId);
            Assertions.assertFalse(resp.getError());
            String json1 = resp.getDataJson();
            Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json1, ProductDTO.class));
            ProductDTO productDTO = objectMapper.readValue(json1, ProductDTO.class);
            Assertions.assertEquals(productId, productDTO.getProductID());
            Assertions.assertEquals("TestProduct", productDTO.getProductName());
        }catch (Exception e){

        }
    }
}
