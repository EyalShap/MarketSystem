package com.sadna.sadnamarket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.MarketServiceTestAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ConcurrencyTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

    String ownerUsername;
    String ownerToken;
    int storeId;
    String maliciousUsername;
    String maliciousToken;

    @BeforeEach
    void clean(){
        bridge.reset();
        ownerUsername = "StoreOwnerMan";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "storeowner@store.com", ownerUsername, "imaginaryPassowrd");
        ownerToken = resp.getDataJson();
        resp = bridge.openStore(ownerToken, ownerUsername, "Store's Store");
        storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(ownerToken, ownerUsername, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        maliciousUsername = "Mallory";
        resp = bridge.signUp(uuid, "mal@mal.com", maliciousUsername, "stolenPasswordBecauseImEvil");
        maliciousToken = resp.getDataJson();
    }

    @Test
    void twoBuyLastProductTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 1);
        resp = bridge.guestEnterSystem();
        String uuid1 = resp.getDataJson();
        resp = bridge.guestEnterSystem();
        String uuid2 = resp.getDataJson();
        try{
            bridge.addProductToBasketGuest(uuid1, storeId, productId, 1);
            bridge.addProductToBasketGuest(uuid2, storeId, productId, 1);
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    bridge.buyCartGuest(uuid1, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                            new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                                    "+97254-989-4939", "jimjimmy@gmail.com", "123456782"));
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    bridge.buyCartGuest(uuid2, new CreditCardDTO("4580458045804580", "852", new Date(1930297600), "213958804"),
                            new AddressDTO("Israel", "Kfar Shmaryahu", "Jabotinsky 39", "Apartment 13", "1234567", "Bob Bobby",
                                    "+97255-123-4569", "bobbobby@gmail.com", "213958804"));
                }
            });
            t1.run();
            t2.run();
            t1.join();
            t2.join();
            Assertions.assertEquals(bridge.getStoreProductAmount(storeId, productId).getDataJson(), "0");
        }catch (Exception e){

        }
    }

    @Test
    void costumerBuyRemovedProductTest(){
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(),Mockito.any(), Mockito.any())).thenReturn(true);

        Response resp = bridge.addProductToStore(ownerToken, ownerUsername, storeId,
                new ProductDTO(-1, "TestProduct", 100.3, "Product"));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(ownerToken, ownerUsername, storeId, productId, 5);
        resp = bridge.guestEnterSystem();
        String uuid1 = resp.getDataJson();
        final int[] succeeded = new int[2];
        succeeded[0] = 0;
        succeeded[1] = 0;
        try{
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    bridge.addProductToBasketGuest(uuid1, storeId, productId, 1);
                    Response resp = bridge.buyCartGuest(uuid1, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                            new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                                    "+97254-989-4939", "jimjimmy@gmail.com", "123456782"));
                    if(!resp.getError()){
                        succeeded[0]++;
                        succeeded[1] = 0;
                    }
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response resp = bridge.removeProductFromStore(ownerToken, ownerUsername,storeId,productId);
                    if(!resp.getError()){
                        succeeded[0]++;
                        succeeded[1] = 5;
                    }
                }
            });
            t1.run();
            t2.run();
            t1.join();
            t2.join();
            Assertions.assertFalse(succeeded[0] == 2 && succeeded[1] == 0);
        }catch (Exception e){

        }
    }

    @Test
    void twoAppointManagerSameUser(){
        //make mallory an owner so we have 2 owners to make this test
        bridge.appointOwner(ownerToken, ownerUsername, storeId, maliciousUsername);
        bridge.acceptOwnerAppointment(maliciousToken, maliciousUsername, storeId, 1);

        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();

        try{
            final int[] succeeded = new int[1];
            succeeded[0] = 0;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response resp = bridge.appointManager(ownerToken, ownerUsername, storeId, appointeeUsername, new LinkedList<>());
                    if(!resp.getError()){
                        succeeded[0]++;
                    }
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response resp = bridge.appointManager(maliciousToken, maliciousUsername, storeId, appointeeUsername, new LinkedList<>());
                    if(!resp.getError()){
                        succeeded[0]++;
                    }
                }
            });
            t1.run();
            t2.run();
            t1.join();
            t2.join();
            Assertions.assertNotEquals(2, succeeded[0]);
            bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1); //it is my understanding that "appointerUsername" field isn't actually used
            Assertions.assertEquals("true", bridge.getIsManager(ownerToken, ownerUsername, storeId, appointeeUsername).getDataJson());
        }catch (Exception e){

        }
    }
}
