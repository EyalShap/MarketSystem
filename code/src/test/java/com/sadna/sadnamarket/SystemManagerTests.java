package com.sadna.sadnamarket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.service.MarketService;
import com.sadna.sadnamarket.service.MarketServiceTestAdapter;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class SystemManagerTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketService bridge;

    String buyerUsername;
    String username;
    String token;
    int storeId;
    String maliciousUsername;
    String maliciousToken;

    @BeforeEach
    void clean(){
        bridge.reset();
        String storeOwnerUsername = "StoreOwnerMan";
        Response resp = bridge.enterAsGuest();
        String uuid = resp.getDataJson();
        resp = bridge.register(storeOwnerUsername, "imaginaryPassowrd","Ross","Geller", "storeowner@store.com","05003030330" );
        String storeOwnerToken = resp.getDataJson();
        resp = bridge.openStore(storeOwnerToken, storeOwnerUsername, "Store's Store");
        storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(storeOwnerToken, storeOwnerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.enterAsGuest();
        uuid = resp.getDataJson();
        buyerUsername = "Billy";
        resp = bridge.register(buyerUsername, "imaginaryPassowrd","Chandler", "Bing", "bill@buyer.com", "0500030303");
        String buyerToken = resp.getDataJson();

        resp = bridge.addProductToStore(storeOwnerToken, storeOwnerUsername, storeId, new ProductDTO(-1 , "product", 100.0, "cat"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(storeOwnerToken, storeOwnerUsername, storeId, productId, 10);
        bridge.addProductToCart(buyerUsername, storeId, productId, 5);
        bridge.memberSetAddress(buyerToken, buyerUsername, new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
        "+97254-989-4939", "jimjimmy@gmail.com", "123456782"));
        bridge.buyCartMember(buyerToken, buyerUsername, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"));

        resp = bridge.enterAsGuest();
        uuid = resp.getDataJson();
        maliciousUsername = "Mallory";
        resp = bridge.register(maliciousUsername, "stolenPasswordBecauseImEvil",uuid, "mal@mal.com", );
        maliciousToken = resp.getDataJson();

        resp = bridge.enterAsGuest();
        uuid = resp.getDataJson();
        username = "IAmAboutToBeGivenGreatPowers";
        resp = bridge.register(username, "noOneCanSeeMyPassword","Joei", "Tribiani", "paul@sadna.com","0505050550" );
        token = resp.getDataJson();

        bridge.setSystemAdminstor(username);
    }

    @Test
    void seeStoreOrderHistoryTest(){
        try {
            Response resp = bridge.getStorePurchaseHistory(token, username, storeId);
            Assertions.assertFalse(resp.getError());
            List<OrderDTO> history = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<OrderDTO>>() { });
            Assertions.assertEquals(1, history.size());
        }catch (Exception e){

        }
    }

    @Test
    void seeStoreOrderHistoryDoesntExistTest(){
        try {
            Response resp = bridge.getStorePurchaseHistory(token, username, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void seeStoreOrderHistoryNoPermissionTest(){
        try {
            Response resp = bridge.getStorePurchaseHistory(maliciousToken, maliciousUsername, storeId);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }


    @Test
    void seeUserOrderHistoryTest(){
        try {
            Response resp = bridge.getOrderHistory(token, username, buyerUsername);
            Assertions.assertFalse(resp.getError());
            List<OrderDTO> history = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<OrderDTO>>() { });
            Assertions.assertEquals(1, history.size());
        }catch (Exception e){

        }
    }

    @Test
    void seeUserOrderHistoryDoesntExistTest(){
        try {
            Response resp = bridge.getUserPurchaseHistory(token, username, "Username that nobody has");
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }

    @Test
    void seeUserOrderHistoryNoPermissionTest(){
        try {
            Response resp = bridge.getUserPurchaseHistory(maliciousToken, maliciousUsername, buyerUsername);
            Assertions.assertTrue(resp.getError());
        }catch (Exception e){

        }
    }
}
