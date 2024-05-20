package com.sadna.sadnamarket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.service.MarketServiceTestAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuestTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

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
        try {
            node = objectMapper.readTree(resJson1);
        }catch (Exception e){

        }
        resp = bridge.login("john@john.com", "thisIsn'tWhatAHashLooksLike");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
        bridge.logout(node.get("token").asText());
        resp = bridge.login("john@john.com", "thisPasswordIsWrong");
        Assertions.assertNotEquals(resp, null);
        Assertions.assertTrue(resp.getError());
    }

}
