package com.sadna.sadnamarket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.service.Error;
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
class MemberTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

    String username;
    String token;

    @BeforeEach
    void clean(){
        bridge.reset();
        username = "TestMember";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "membertest@member.com", username, "imaginaryPassowrd");
        token = resp.getDataJson();
    }
    @Test
    void memberLogoutTest() {
        Response resp = bridge.logout(username);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());
    }

    @Test
    void memberLogoutDoesntExistTest() {
        Response resp = bridge.logout("username that isn't very real");
        Assertions.assertTrue(resp.getError());
        Assertions.assertEquals(Error.makeMemberUserDoesntExistError("username that isn't very real"), resp.getErrorString());
    }

    @Test
    void memberLogoutTwiceTest() {
        bridge.logout(username);
        Response resp = bridge.logout(username);
        Assertions.assertTrue(resp.getError());
        Assertions.assertEquals(Error.makeMemberUserIsNotLoggedInError(), resp.getErrorString());
    }

    @Test
    void memberOpenStoreTest() {
        Response resp = bridge.openStore(token, username, "Peter's Store");
        Assertions.assertFalse(resp.getError());
        int storeId = Integer.parseInt(resp.getDataJson());
        try {
            resp = bridge.getStoreData(token, username, storeId);
            Assertions.assertFalse(resp.getError());
            StoreDTO storeDTO = objectMapper.readValue(resp.getDataJson(), StoreDTO.class);
            Assertions.assertEquals("Peter's Store", storeDTO.getStoreName());
        }catch (Exception e){

        }
    }

    @Test
    void memberOpenStoreNotMemberTest() {
        Response resp = bridge.openStore("not token", "not username", "Peter's Store");
        Assertions.assertTrue(resp.getError());
        Assertions.assertEquals(Error.makeAuthInvalidJWTError(), resp.getErrorString());
    }
}
