package com.sadna.sadnamarket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
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
import java.util.Map;

@SpringBootTest
class StoreOwnerTests {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketServiceTestAdapter bridge;

    String username;
    String token;
    int storeId;
    String maliciousUsername;
    String maliciousToken;

    @BeforeEach
    void clean() {
        bridge.reset();
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(), Mockito.any(), Mockito.any())).thenReturn(true);
        username = "StoreOwnerMan";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "storeowner@store.com", username, "imaginaryPassowrd");
        token = resp.getDataJson();
        resp = bridge.openStore(token, username, "Store's Store");
        storeId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreBankAccount(token, username, storeId, new BankAccountDTO("10", "392", "393013", "2131516175"));
        resp = bridge.guestEnterSystem();
        uuid = resp.getDataJson();
        maliciousUsername = "Mallory";
        resp = bridge.signUp(uuid, "mal@mal.com", maliciousUsername, "stolenPasswordBecauseImEvil");
        maliciousToken = resp.getDataJson();
    }

    @Test
    void addProductTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        Assertions.assertFalse(resp.getError());
        String productIdString = resp.getDataJson();
        Assertions.assertDoesNotThrow(() -> Integer.parseInt(productIdString));
        int productId = Integer.parseInt(productIdString);
        try {
            resp = bridge.getProductData(token, username, productId);
            Assertions.assertFalse(resp.getError());
        } catch (Exception e) {

        }
    }

    @Test
    void addProductBadInfoTest() {
        Response resp = bridge.addProductToStore(token, username, storeId, new ProductDTO(-1, "", -100.0, "cat", -10, 8));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void addProductNoPermissionTest() {
        Response resp = bridge.addProductToStore(maliciousToken, maliciousUsername, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void editProductTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.editStoreProduct(token, username, storeId, productId, new ProductDTO(-1, "product", 200.0, "cat", 3.5, 4));
        Assertions.assertFalse(resp.getError());
        try {
            resp = bridge.getProductData(token, username, productId);
            Assertions.assertFalse(resp.getError());
            ProductDTO productDTO = objectMapper.readValue(resp.getDataJson(), ProductDTO.class);
            Assertions.assertEquals(200.0, productDTO.getProductPrice());
            resp = bridge.setStoreProductAmount(token, username, storeId, productId, 1);
            Assertions.assertFalse(resp.getError());
            resp = bridge.getStoreProductAmount(storeId, productId);
            Assertions.assertEquals("1", resp.getDataJson());
        } catch (Exception e) {
        }
    }

    @Test
    void editProductBadInfoTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.editStoreProduct(token, username, storeId, productId,
                new ProductDTO(-1, null, -200.0, null, -10, 3));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void editProductDoesntExistTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.editStoreProduct(token, username, storeId, Integer.MAX_VALUE,
                new ProductDTO(-1, null, -200.0, null, -10, 3));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void editProductWrongStoreTest() {
        Response resp = bridge.openStore(token, username, "New Store");
        int newStoreId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(token, username, newStoreId, new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.editStoreProduct(token, username, storeId, productId, new ProductDTO(-1, null, 200.0, null, -10, 4));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void editProductNoPermissionTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.editStoreProduct(maliciousToken, maliciousUsername, storeId, productId,
                new ProductDTO(-1, null, 200.0, null, -10, 5));
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void removeProductTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.removeProductFromStore(token, username, storeId, productId);
        Assertions.assertFalse(resp.getError());
        try {
            resp = bridge.getProductData(token, username, productId);
            Assertions.assertTrue(resp.getError());
        } catch (Exception e) {
        }
    }

    @Test
    void removeProductDoesntExistTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.removeProductFromStore(token, username, storeId, Integer.MAX_VALUE);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void removeProductWrongStoreTest() {
        Response resp = bridge.openStore(token, username, "New Store");
        int newStoreId = Integer.parseInt(resp.getDataJson());
        resp = bridge.addProductToStore(token, username, newStoreId, new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.removeProductFromStore(token, username, storeId, productId);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void removeProductNoPermissionTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        resp = bridge.removeProductFromStore(maliciousToken, maliciousUsername, storeId, productId);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointOwnerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.logout(appointeeUsername);

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());

        resp = bridge.login(appointeeUsername, "password");
        apointeeToken = resp.getDataJson();
        resp = bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, storeId, 1);
        Assertions.assertFalse(resp.getError());

        resp = bridge.getIsOwner(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());
    }

    @Test
    void appointOwnerRejectTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.logout(appointeeUsername);

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());

        resp = bridge.login(appointeeUsername, "password");
        apointeeToken = resp.getDataJson();
        resp = bridge.rejectOwnerAppointment(apointeeToken, appointeeUsername, storeId, username);
        Assertions.assertFalse(resp.getError());

        resp = bridge.getIsOwner(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("false", resp.getDataJson());
    }

    @Test
    void appointOwnerAlreadyOwnerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointOwner(token, username, storeId, appointeeUsername);
        bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        resp = bridge.appointOwner(token, username, storeId, appointeeUsername);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointOwnerDoesntExistTest() {
        Response resp = bridge.appointOwner(token, username, storeId, "Eric");
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointOwnerNoPermissionTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        resp = bridge.appointOwner(maliciousToken, maliciousUsername, storeId, appointeeUsername);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointOwnerOriginalOwnerDoesntExistTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        resp = bridge.appointOwner("token that isn't real", "username that nobody has", storeId, appointeeUsername);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointManagerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.logout(appointeeUsername);

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());

        resp = bridge.login(appointeeUsername, "password");
        apointeeToken = resp.getDataJson();
        resp = bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);
        Assertions.assertFalse(resp.getError());

        resp = bridge.getIsManager(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());
    }

    @Test
    void appointManagerRejectTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.logout(appointeeUsername);

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("true", resp.getDataJson());

        resp = bridge.login(appointeeUsername, "password");
        apointeeToken = resp.getDataJson();
        resp = bridge.rejectManagerAppointment(apointeeToken, appointeeUsername, storeId, username);
        Assertions.assertFalse(resp.getError());

        resp = bridge.getIsManager(token, username, storeId, appointeeUsername);
        Assertions.assertFalse(resp.getError());
        Assertions.assertEquals("false", resp.getDataJson());
    }

    @Test
    void appointManagerAlreadyManagerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointManagerAlreadyOwnerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointOwner(token, username, storeId, appointeeUsername);
        bridge.acceptOwnerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        resp = bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointManagerDoesntExistTest() {
        Response resp = bridge.appointManager(token, username, storeId, "Eric", new LinkedList<>());
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointManagerNoPermissionTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        resp = bridge.appointManager(maliciousToken, maliciousUsername, storeId, appointeeUsername, new LinkedList<>());
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void appointManagerOwnerDoesntExistTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        resp = bridge.appointManager("token that isn't real", "username that nobody has", storeId, appointeeUsername,
                new LinkedList<>());
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void changeManagerPermissionsTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);
        List<Integer> newPerms = new LinkedList<>();
        newPerms.add(1);
        resp = bridge.changeManagerPermissions(token, username, appointeeUsername, storeId, newPerms);
        Assertions.assertFalse(resp.getError());
        try {
            resp = bridge.getManagerPermissions(token, username, storeId, appointeeUsername);
            List<Integer> perms = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<Integer>>() {
            });
            Assertions.assertEquals(1, perms.size());
            Assertions.assertEquals(1, perms.get(0));
        } catch (Exception e) {

        }
    }

    @Test
    void changeManagerPermissionsNotOwnerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);
        List<Integer> newPerms = new LinkedList<>();
        newPerms.add(1);
        resp = bridge.changeManagerPermissions(maliciousToken, maliciousUsername, appointeeUsername, storeId, newPerms);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void changeManagerPermissionsNotManagerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        List<Integer> newPerms = new LinkedList<>();
        newPerms.add(1);
        resp = bridge.changeManagerPermissions(token, username, appointeeUsername, storeId, newPerms);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void closeStoreTest() {
        Response resp = bridge.closeStore(token, username, storeId);
        Assertions.assertFalse(resp.getError());
    }

    @Test
    void closeStoreDoesntExistTest() {
        Response resp = bridge.closeStore(token, username, Integer.MAX_VALUE);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void closeStoreNotOwnerTest() {
        Response resp = bridge.closeStore(maliciousToken, maliciousUsername, storeId);
        Assertions.assertTrue(resp.getError());
    }

    @Test
    void getStoreRoleTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        try {
            resp = bridge.getStoreOwners(token, username, storeId);
            List<MemberDTO> owners = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<MemberDTO>>() {
            });
            Assertions.assertEquals(1, owners.size());
            Assertions.assertEquals(username, owners.get(0).getUsername());

            resp = bridge.getStoreManagers(token, username, storeId);
            List<MemberDTO> managers = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<MemberDTO>>() {
            });
            Assertions.assertEquals(1, managers.size());
            Assertions.assertEquals(appointeeUsername, managers.get(0).getUsername());
        } catch (Exception e) {

        }
    }

    @Test
    void getStoreRoleDoesntExistTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        try {
            resp = bridge.getStoreOwners(token, username, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());

            resp = bridge.getStoreManagers(token, username, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        } catch (Exception e) {

        }
    }

    @Test
    void getStoreRoleNotOwnerTest() {
        String appointeeUsername = "Eric";
        Response resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        resp = bridge.signUp(uuid, "eric@excited.com", appointeeUsername, "password");
        String apointeeToken = resp.getDataJson();
        bridge.appointManager(token, username, storeId, appointeeUsername, new LinkedList<Integer>());
        bridge.acceptManagerAppointment(apointeeToken, appointeeUsername, storeId, 1);

        try {
            resp = bridge.getStoreOwners(maliciousToken, maliciousUsername, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());

            resp = bridge.getStoreManagers(maliciousToken, maliciousUsername, Integer.MAX_VALUE);
            Assertions.assertTrue(resp.getError());
        } catch (Exception e) {

        }
    }

    @Test
    void seeStoreOrderHistoryTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(token, username, storeId, productId, 10);
        resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.addProductToBasketGuest(uuid, storeId, productId, 5);
        bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                        "+97254-989-4939", "jimjimmy@gmail.com"));
        try {
            resp = bridge.getStorePurchaseHistory(token, username, storeId);
            List<OrderDTO> history = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<OrderDTO>>() {
            });
            Assertions.assertEquals(1, history.size());
        } catch (Exception e) {

        }
    }

    @Test
    void seeStoreOrderHistoryNeverPurchaseTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(token, username, storeId, productId, 10);
        try {
            resp = bridge.getStorePurchaseHistory(token, username, storeId);
            List<OrderDTO> history = objectMapper.readValue(resp.getDataJson(), new TypeReference<List<OrderDTO>>() {
            });
            Assertions.assertEquals(0, history.size());
        } catch (Exception e) {

        }
    }

    @Test
    void seeStoreOrderHistoryNoOwnerTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(token, username, storeId, productId, 10);
        resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.addProductToBasketGuest(uuid, storeId, productId, 5);
        bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                        "+97254-989-4939", "jimjimmy@gmail.com"));
        try {
            resp = bridge.getStorePurchaseHistory(maliciousToken, maliciousUsername, storeId);
            Assertions.assertTrue(resp.getError());
        } catch (Exception e) {

        }
    }

    @Test
    void seeStoreOrderHistoryDoesntExistTest() {
        Response resp = bridge.addProductToStore(token, username, storeId,
                new ProductDTO(-1, "product", 100.0, "cat", 3.5, 2));
        int productId = Integer.parseInt(resp.getDataJson());
        bridge.setStoreProductAmount(token, username, storeId, productId, 10);
        resp = bridge.guestEnterSystem();
        String uuid = resp.getDataJson();
        bridge.addProductToBasketGuest(uuid, storeId, productId, 5);
        bridge.buyCartGuest(uuid, new CreditCardDTO("4722310696661323", "103", new Date(1830297600), "123456782"),
                new AddressDTO("Israel", "Yerukham", "Benyamin 12", "Apartment 12", "8053624", "Jim Jimmy",
                        "+97254-989-4939", "jimjimmy@gmail.com"));
        try {
            resp = bridge.getStorePurchaseHistory("nopety nope nope", "doesnt exist", storeId);
            Assertions.assertTrue(resp.getError());
        } catch (Exception e) {

        }
    }
}
