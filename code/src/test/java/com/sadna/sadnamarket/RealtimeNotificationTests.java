package com.sadna.sadnamarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.ProductDTO;
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
import java.util.LinkedList;

@SpringBootTest
class RealtimeNotificationTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

    String username;
    String token;
    int storeId;
    int productId;
    FaketimeService fake;


    @BeforeEach
    void clean() {
        bridge.reset();
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(), Mockito.any(), Mockito.any())).thenReturn(true);
        username = "StoreOwnerMan";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "storeowner@store.com", username, "imaginaryPassowrd");
        token = resp.getDataJson();
        resp = bridge.openStore(token, username, "Store's Store");
        storeId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product", 3.5, 2,true,storeId));
        productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(token, username, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        fake = new FaketimeService();
        bridge.injectRealtime(fake);
    }

    @Test
    void buyCartTest() {
        String uuid = bridge.guestEnterSystem().getDataJson();

        bridge.setStoreProductAmount(token, username, storeId, productId, 2);
        try {
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            CreditCardDTO cardDTO = new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782");
            AddressDTO addressDTO = new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                    "+97254-989-4939", "jimjimmy@gmail.com");
            Response resp = bridge.buyCartGuest(uuid, cardDTO,addressDTO);
            Assertions.assertEquals("User made a purchase in your store Store's Store",fake.getMessages(username).get(0));
        } catch (Exception e) {

        }
    }

    @Test
    void buyCartMemberTest() {
        String uuid = bridge.guestEnterSystem().getDataJson();

        bridge.setStoreProductAmount(token, username, storeId, productId, 2);
        try {
            bridge.addProductToBasketMember(token,username, storeId, productId, 1);
            CreditCardDTO cardDTO = new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782");
            AddressDTO addressDTO = new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                    "+97254-989-4939", "jimjimmy@gmail.com");
            Response resp = bridge.buyCartMember(token,username, cardDTO,addressDTO);
            Assertions.assertEquals("User StoreOwnerMan made a purchase in your store Store's Store",fake.getMessages(username).get(0));
        } catch (Exception e) {

        }
    }

    @Test
    void buyCartLoggedOutTest() {
        String uuid = bridge.guestEnterSystem().getDataJson();

        bridge.setStoreProductAmount(token, username, storeId, productId, 2);
        try {
            bridge.addProductToBasketGuest(uuid, storeId, productId, 1);
            CreditCardDTO cardDTO = new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782");
            AddressDTO addressDTO = new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                    "+97254-989-4939", "jimjimmy@gmail.com");
            bridge.logout(username);
            Response resp = bridge.buyCartGuest(uuid, cardDTO,addressDTO);
            Assertions.assertEquals(null,fake.getMessages(username));
        } catch (Exception e) {

        }
    }

    @Test
    void sendOwnerRequestAcceptTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        Assertions.assertEquals("You got appointment request",fake.getMessages(appointeeUsername).get(0));
        resp = bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, 1, 1);
        Assertions.assertEquals("User " + appointeeUsername + " accepted request for Owner in " + storeId,fake.getMessages(username).get(0));
    }

    @Test
    void sendOwnerRequestRejectTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        Assertions.assertEquals("You got appointment request",fake.getMessages(appointeeUsername).get(0));
        resp = bridge.rejectOwnerAppointment(apointeeToken, appointeeUsername, 1,"");
        Assertions.assertEquals("User " + appointeeUsername + " rejected request for Owner in " + storeId,fake.getMessages(username).get(0));
    }

    @Test
    void sendManagerRequestAcceptTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<>());
        Assertions.assertEquals("You got appointment request",fake.getMessages(appointeeUsername).get(0));
        resp = bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, 1, 1);
        Assertions.assertEquals("User " + appointeeUsername + " accepted request for Manager in " + storeId,fake.getMessages(username).get(0));
    }

    @Test
    void sendManagerRequestRejectTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<>());
        Assertions.assertEquals("You got appointment request",fake.getMessages(appointeeUsername).get(0));
        resp = bridge.rejectOwnerAppointment(apointeeToken, appointeeUsername, 1,"");
        Assertions.assertEquals("User " + appointeeUsername + " rejected request for Manager in " + storeId,fake.getMessages(username).get(0));
    }

    @Test
    void closeStoreTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        resp = bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, 1, 1);
        bridge.closeStore(token,username,storeId);
        Assertions.assertEquals(String.format("The store \"%s\" was closed.", "Store's Store"),fake.getMessages(appointeeUsername).get(1));
    }

    @Test
    void closeStoreLoggedOutTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        resp = bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, 1, 1);
        bridge.logout(appointeeUsername);
        bridge.closeStore(token,username,storeId);
        Assertions.assertTrue(fake.getMessages(appointeeUsername).size() == 1);
    }
}