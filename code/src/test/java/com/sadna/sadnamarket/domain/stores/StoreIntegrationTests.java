package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.service.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StoreIntegrationTests {
    private Store store;

    private StoreInfo generateStoreInfo() {
        LocalTime openingHour = LocalTime.of(10, 0);
        LocalTime closingHour = LocalTime.of(21, 0);
        LocalTime fridayClosingHour = LocalTime.of(14, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        return new StoreInfo("Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours);
    }

    private BankAccountDTO generateBankAccount() {
        return new BankAccountDTO("123", "456", "789", "Willy");
    }

    private Store generateStore() {
        Store s = new Store(0, "Willy", generateStoreInfo());
        s.setBankAccount(generateBankAccount());
        return s;
    }

    private List<CartItemDTO> generateCart0() {
        CartItemDTO item0 = new CartItemDTO(0, 0, 50);
        CartItemDTO item1 = new CartItemDTO(0, 1, 25);
        CartItemDTO item2 = new CartItemDTO(0, 2, 70);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, item0, item1, item2);
        return cart;
    }

    private List<CartItemDTO> generateCart1() {
        CartItemDTO item0 = new CartItemDTO(0, 0, 50);
        CartItemDTO item1 = new CartItemDTO(0, 1, 25);
        CartItemDTO item2 = new CartItemDTO(0, 2, 400);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, item0, item1, item2);
        return cart;
    }

    private List<CartItemDTO> generateCart2() {
        CartItemDTO item0 = new CartItemDTO(0, 0, 1000);
        CartItemDTO item1 = new CartItemDTO(0, 1, 25);
        CartItemDTO item2 = new CartItemDTO(0, 2, 400);
        CartItemDTO item3 = new CartItemDTO(0, 3, 200);
        CartItemDTO item4 = new CartItemDTO(0, 4, 200);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, item0, item1, item2, item3, item4);
        return cart;
    }

    private StoreDTO generateStoreDTO() {
        LocalTime openingHour = LocalTime.of(10, 0);
        LocalTime closingHour = LocalTime.of(21, 0);
        LocalTime fridayClosingHour = LocalTime.of(14, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        List<String> owners = new ArrayList<>();
        owners.add("Willy");
        return new StoreDTO(0, true, "Chocolate Factory", 3, "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours, new HashMap<>(), "Willy", owners, new ArrayList<>(), new ArrayList<>());
    }

    private void fillStoreProducts() {
        store.addProduct(0, 200);
        store.addProduct(1, 200);
        store.addProduct(2, 200);
    }

    @BeforeEach
    void setUp() {
        this.store = generateStore();
    }

    @Test
    void getStoreInfo() {
        StoreInfo expected = generateStoreInfo();

        assertEquals(expected, store.getStoreInfo());
    }

    @Test
    void getBankAccount() {
        BankAccountDTO expected = generateBankAccount();

        assertEquals(expected, store.getBankAccount());
    }

    @Test
    void setBankAccount() {
        BankAccountDTO expected0 = generateBankAccount();
        BankAccountDTO expected1 =  new BankAccountDTO("321", "654", "987", "Mr. Krabs");
        BankAccountDTO newAccount =  new BankAccountDTO("321", "654", "987", "Mr. Krabs");

        assertEquals(expected0, store.getBankAccount());

        store.setBankAccount(newAccount);
        assertEquals(expected1, store.getBankAccount());
    }

    @Test
    void updateStockSuccess() {
        fillStoreProducts();
        Map<Integer, Integer> expected0 = new HashMap<>();
        Map<Integer, Integer> expected1 = new HashMap<>();
        expected0.put(0, 200);
        expected0.put(1, 200);
        expected0.put(2, 200);
        expected1.put(0, 150);
        expected1.put(1, 175);
        expected1.put(2, 130);

        assertEquals(expected0, store.getProductAmounts());

        List<CartItemDTO> cart = generateCart0();
        store.updateStock(cart);
        assertEquals(expected1, store.getProductAmounts());
    }

    @Test
    void updateStockProductDoesNotExist() {
        fillStoreProducts();

        Map<Integer, Integer> expected0 = new HashMap<>();
        expected0.put(0, 200);
        expected0.put(1, 200);
        expected0.put(2, 200);

        assertEquals(expected0, store.getProductAmounts());

        List<CartItemDTO> cart = generateCart0();
        cart.add(new CartItemDTO(0, 3, 100)); // product that does not exist in the store

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store.updateStock(cart);
        });
        assertEquals(Error.makeProductDoesntExistInStoreError(0, 3) + "\n", expected1.getMessage());
        assertEquals(expected0, store.getProductAmounts());
    }

    @Test
    void updateStockNotEnoughInStock() {
        fillStoreProducts();

        Map<Integer, Integer> expected0 = new HashMap<>();
        expected0.put(0, 200);
        expected0.put(1, 200);
        expected0.put(2, 200);

        assertEquals(expected0, store.getProductAmounts());

        List<CartItemDTO> cart = generateCart1();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store.updateStock(cart);
        });
        assertEquals(Error.makeNotEnoughInStcokError(0, 2, 400, 200) + "\n", expected1.getMessage());
        assertEquals(expected0, store.getProductAmounts());
    }

    @Test
    void updateStockNoProductAndNoStock() {
        fillStoreProducts();

        Map<Integer, Integer> expected0 = new HashMap<>();
        expected0.put(0, 200);
        expected0.put(1, 200);
        expected0.put(2, 200);

        assertEquals(expected0, store.getProductAmounts());

        List<CartItemDTO> cart = generateCart2();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store.updateStock(cart);
        });
        String error =
                Error.makeNotEnoughInStcokError(0, 0, 1000, 200) + "\n" +
                Error.makeNotEnoughInStcokError(0, 2, 400, 200) + "\n" +
                Error.makeProductDoesntExistInStoreError(0, 3) + "\n"+
                Error.makeProductDoesntExistInStoreError(0, 4) + "\n";
        assertEquals(error, expected1.getMessage());
        assertEquals(expected0, store.getProductAmounts());
    }

    @Test
    void updateStockStoreClosed() {
        store.closeStore();

        List<CartItemDTO> cart = generateCart0();

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            store.updateStock(cart);
        });
        assertEquals(Error.makeStoreClosedError(0), expected.getMessage());
    }

    @Test
    void getStoreDTO() {
        StoreDTO expected = generateStoreDTO();
        StoreDTO res = store.getStoreDTO();
        assertEquals(expected, res);
    }

    @Test
    void checkCartSuccess() {
        fillStoreProducts();
        List<CartItemDTO> cart = generateCart0();
        assertEquals("", store.checkCart(cart));
    }

    @Test
    void checkCartProductDoesNotExist() {
        fillStoreProducts();
        List<CartItemDTO> cart = generateCart0();
        cart.add(new CartItemDTO(0, 3, 100)); // product that does not exist in the store
        assertEquals(Error.makeProductDoesntExistInStoreError(0, 3) + "\n", store.checkCart(cart));
    }

    @Test
    void checkCartNotEnoughInStock() {
        fillStoreProducts();
        List<CartItemDTO> cart = generateCart1();
        assertEquals(Error.makeNotEnoughInStcokError(0, 2, 400, 200) + "\n", store.checkCart(cart));
    }

    @Test
    void checkCartNoProductAndNoStock() {
        fillStoreProducts();
        List<CartItemDTO> cart = generateCart2();
        String error =
                Error.makeNotEnoughInStcokError(0, 0, 1000, 200) + "\n" +
                        Error.makeNotEnoughInStcokError(0, 2, 400, 200) + "\n" +
                        Error.makeProductDoesntExistInStoreError(0, 3) + "\n"+
                        Error.makeProductDoesntExistInStoreError(0, 4) + "\n";
        assertEquals(error, store.checkCart(cart));
    }

    @Test
    void checkCartStoreClosed() {
        store.closeStore();
        List<CartItemDTO> cart = generateCart0();
        assertEquals(Error.makeStoreClosedError(0), store.checkCart(cart));
    }
}