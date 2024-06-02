package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.users.CartItemDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StoreTest {

    private Store store0;
    private Store store1;

    @BeforeEach
    public void setUp() {
        StoreInfo storeInfo0 = new StoreInfo("Store0", "Lehavim", "store0@gmail.com", "0546310765", null, null);
        StoreInfo storeInfo1 = new StoreInfo("Store1",  "Tel Aviv", "store1@gmail.com", "0546310764", null, null);
        store0 = new Store(0, "Alice", storeInfo0);
        store1 = new Store(1, "Bob", storeInfo1);
    }

    @Test
    void getStoreId() {
        assertEquals(0, store0.getStoreId());
        assertEquals(1, store1.getStoreId());
    }

    @Test
    void getFounderUsername() {
        assertEquals("Alice", store0.getFounderUsername());
        assertEquals("Bob", store1.getFounderUsername());
    }

    @Test
    void getIsActive() {
        assertTrue(store0.getIsActive());
        assertTrue(store1.getIsActive());

        store0.closeStore();
        assertFalse(store0.getIsActive());
        assertTrue(store1.getIsActive());

        store1.closeStore();
        assertFalse(store0.getIsActive());
        assertFalse(store1.getIsActive());
    }

    @Test
    void getOwnerUsernames() {
        Set<String> expected1 = new HashSet<>();
        Set<String> expected2 = new HashSet<>();
        Set<String> expected3 = new HashSet<>();
        Collections.addAll(expected1, "Alice");
        Collections.addAll(expected2, "Bob", "Alice");
        Collections.addAll(expected3, "Bob");

        assertEquals(expected1, new HashSet<>(store0.getOwnerUsernames()));

        store0.addStoreOwner("Bob");
        assertEquals(expected2, new HashSet<>(store0.getOwnerUsernames()));

        assertEquals(expected3, new HashSet<>(store1.getOwnerUsernames()));
    }

    @Test
    void getManagerUsernames() {
        Set<String> expected1 = new HashSet<>();
        Set<String> expected2 = new HashSet<>();
        Collections.addAll(expected2, "Bob", "Moshe");

        assertEquals(expected1, new HashSet<>(store0.getManagerUsernames()));
        assertEquals(expected1, new HashSet<>(store1.getManagerUsernames()));

        store1.addStoreManager("Bob");
        store1.addStoreManager("Moshe");
        assertEquals(expected2, new HashSet<>(store1.getManagerUsernames()));

        assertEquals(expected1, new HashSet<>(store0.getManagerUsernames()));
    }

    @Test
    void getSellerUsernames() {
        Set<String> expected1 = new HashSet<>();
        Set<String> expected2 = new HashSet<>();
        Collections.addAll(expected2, "Bob", "Moshe");

        assertEquals(expected1, new HashSet<>(store0.getSellerUsernames()));
        assertEquals(expected1, new HashSet<>(store1.getSellerUsernames()));

        store1.addSeller("Bob");
        store1.addSeller("Moshe");
        assertEquals(expected2, new HashSet<>(store1.getSellerUsernames()));

        assertEquals(expected1, new HashSet<>(store0.getSellerUsernames()));
    }

    @Test
    void getProductAmounts() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        Map<Integer, Integer> expected2 = new HashMap<>();
        Map<Integer, Integer> expected3 = new HashMap<>();
        expected2.put(1, 7);
        expected2.put(0, 10);
        expected3.put(0, 8);

        assertEquals(expected1, store0.getProductAmounts());
        assertEquals(expected1, store1.getProductAmounts());

        store1.addProduct(0, 10);
        store1.addProduct(1, 7);
        assertEquals(expected2, store1.getProductAmounts());

        store0.addProduct(0, 8);
        assertEquals(expected3, store0.getProductAmounts());
    }

    @Test
    void getOrderIds() {
        Set<Integer> expected1 = new HashSet<>();
        Set<Integer> expected2 = new HashSet<>();
        Collections.addAll(expected1, 0, 1);
        Collections.addAll(expected2, 2);

        store0.addOrderId(0);
        store0.addOrderId(1);
        assertEquals(expected1, new HashSet<>(store0.getOrderIds()));

        store1.addOrderId(2);
        assertEquals(expected2, new HashSet<>(store1.getOrderIds()));
    }

    @Test
    void addProductSuccess() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        expected1.put(0, 104);
        expected1.put(1, 342);
        expected1.put(2, 98);

        store0.addProduct(0, 104);
        store0.addProduct(1, 342);
        store0.addProduct(2, 98);
        assertEquals(expected1, store0.getProductAmounts());
    }

    @Test
    void addProductTwice() {
        store0.addProduct(0, 54);
        store0.addProduct(1, 32);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addProduct(0, 103);
        });
        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addProduct(1, 9);
        });

        String expectedMessage1 = "A product with id 0 already exists.";
        String expectedMessage2 = "A product with id 1 already exists.";
        assertEquals(expectedMessage1, expected1.getMessage());
        assertEquals(expectedMessage2, expected2.getMessage());
    }

    @Test
    void addProductNegAmount() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addProduct(0, -100);
        });

        String expectedMessage1 = "-100 is an illegal amount of products.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void deleteProductThatExists() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        Map<Integer, Integer> expected2 = new HashMap<>();
        Map<Integer, Integer> expected3 = new HashMap<>();
        expected1.put(0, 100);
        expected1.put(1, 76);
        expected2.put(0, 100);

        store0.addProduct(0, 100);
        store0.addProduct(1, 76);

        assertEquals(expected1, store0.getProductAmounts());

        store0.deleteProduct(1);
        assertEquals(expected2, store0.getProductAmounts());

        store0.deleteProduct(0);
        assertEquals(expected3, store0.getProductAmounts());

        store0.addProduct(0, 100);
        assertEquals(expected2, store0.getProductAmounts());
    }

    @Test
    void deleteProductThatDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.deleteProduct(0);
        });

        String expectedMessage1 = "Product Id 0 does not exist.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void deleteProductStoreNotActive() {
        store0.addProduct(0, 34);
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.deleteProduct(0);
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void setProductAmountsSuccess() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        Map<Integer, Integer> expected2 = new HashMap<>();
        Map<Integer, Integer> expected3 = new HashMap<>();
        expected1.put(0, 12);
        expected1.put(1, 43);
        expected2.put(0, 99);
        expected2.put(1, 43);
        expected3.put(0, 99);
        expected3.put(1, 1);

        store0.addProduct(0, 12);
        store0.addProduct(1, 43);
        assertEquals(expected1, store0.getProductAmounts());

        store0.setProductAmounts(0, 99);
        assertEquals(expected2, store0.getProductAmounts());

        store0.setProductAmounts(1, 1);
        assertEquals(expected3, store0.getProductAmounts());
    }

    @Test
    void setProductAmountProductDoesNotExist() {
        store0.addProduct(0, 33);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.setProductAmounts(3, 18);
        });

        String expectedMessage1 = "Product Id 3 does not exist.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void setProductAmountStoreNotActive() {
        store0.addProduct(0, 33);
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.setProductAmounts(0, 18);
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void setProductAmountNegNum() {
        store0.addProduct(0, 33);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.setProductAmounts(0, -80);
        });

        String expectedMessage1 = "-80 is an illegal amount of products.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    /*@Test
    void buyStoreProduct() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        Map<Integer, Integer> expected2 = new HashMap<>();
        expected1.put(0, 100);
        expected2.put(0, 50);

        store0.addProduct(0, 100);
        assertEquals(expected1, store0.getProductAmounts());

        store0.buyStoreProduct(0, 50);
        assertEquals(expected2, store0.getProductAmounts());
    }

    @Test
    void buyStoreProductDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.buyStoreProduct(0, 34);
        });

        String expectedMessage1 = "A product with id 0 does not exist.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void buyStoreProductNegAmount() {
        store0.addProduct(0, 9);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.buyStoreProduct(0, -5);
        });

        String expectedMessage1 = "-5 is an illegal amount of products.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void buyStoreProductLargerThanStock() {
        store0.addProduct(0, 33);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.buyStoreProduct(0, 34);
        });

        String expectedMessage1 = "You can not buy 34 of product 0 because there are only 33 in the store.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void buyStoreProductStoreNotActive() {
        store0.addProduct(0, 33);
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.buyStoreProduct(0, 18);
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }*/

    @Test
    void productExists() {
        store0.addProduct(0, 100);
        store0.addProduct(1, 30);

        assertTrue(store0.productExists(0));
        assertTrue(store0.productExists(1));
        assertFalse(store0.productExists(2));
    }

    @Test
    void isStoreOwner() {
        store0.addStoreOwner("Bob");
        store0.addStoreOwner("Netta");
        store1.addStoreOwner("Alice");
        store1.addStoreOwner("Netta");

        assertTrue(store0.isStoreOwner("Alice"));
        assertTrue(store1.isStoreOwner("Alice"));
        assertTrue(store0.isStoreOwner("Bob"));
        assertTrue(store1.isStoreOwner("Bob"));
        assertTrue(store0.isStoreOwner("Netta"));
        assertTrue(store1.isStoreOwner("Netta"));
    }

    @Test
    void isStoreManager() {
        store0.addStoreManager("Alice");
        store0.addStoreManager("Bob");
        store1.addStoreManager("Alice");
        store1.addStoreManager("Netta");

        assertTrue(store0.isStoreManager("Alice"));
        assertTrue(store1.isStoreManager("Alice"));
        assertTrue(store0.isStoreManager("Bob"));
        assertTrue(store1.isStoreManager("Netta"));
    }

    @Test
    void isSeller() {
        store0.addSeller("Alice");
        store0.addSeller("Bob");
        store1.addSeller("Alice");
        store1.addSeller("Netta");

        assertTrue(store0.isSeller("Alice"));
        assertTrue(store1.isSeller("Alice"));
        assertTrue(store0.isSeller("Bob"));
        assertTrue(store1.isSeller("Netta"));
    }

    @Test
    void addStoreOwnerAlreadyExists() {
        store0.addStoreOwner("Bob");

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addStoreOwner("Bob");
        });

        String expectedMessage1 = "User Bob is already a owner of store 0.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addStoreOwnerStoreNotActive() {
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addStoreOwner("Bob");
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addStoreManagerAlreadyExists() {
        store0.addStoreManager("Bob");

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addStoreManager("Bob");
        });

        String expectedMessage1 = "User Bob is already a manager of store 0.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addStoreManagerStoreNotActive() {
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addStoreManager("Bob");
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addSellerAlreadyExists() {
        store1.addSeller("Bob");

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store1.addSeller("Bob");
        });

        String expectedMessage1 = "User Bob is already a seller of store 1.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addSellerStoreNotActive() {
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addSeller("Bob");
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void closeStore() {
        assertTrue(store0.getIsActive());
        assertTrue(store1.getIsActive());

        store0.closeStore();
        assertFalse(store0.getIsActive());
        assertTrue(store1.getIsActive());

        store1.closeStore();
        assertFalse(store0.getIsActive());
        assertFalse(store1.getIsActive());
    }

    @Test
    void addOrderIdAlreadyExist() {
        store1.addOrderId(0);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store1.addOrderId(0);
        });

        String expectedMessage1 = "A order with id 0 already exists in store 1.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void addOrderIdStoreNotActive() {
        store0.closeStore();

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            store0.addOrderId(1);
        });

        String expectedMessage1 = "A store with id 0 is not active.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void buyCart() {
        Map<Integer, Integer> expected1 = new HashMap<>();
        Map<Integer, Integer> expected2 = new HashMap<>();
        expected1.put(1, 100);
        expected1.put(2, 200);
        expected2.put(1, 18);
        expected2.put(2, 197);

        CartItemDTO cartItemDTO1 = mock(CartItemDTO.class);
        CartItemDTO cartItemDTO2 = mock(CartItemDTO.class);
        when(cartItemDTO1.getProductId()).thenReturn(1);
        when(cartItemDTO2.getProductId()).thenReturn(2);
        when(cartItemDTO1.getAmount()).thenReturn(82);
        when(cartItemDTO2.getAmount()).thenReturn(3);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, cartItemDTO1, cartItemDTO2);

        store0.addProduct(1, 100);
        store0.addProduct(2, 200);

        assertEquals(expected1, store0.getProductAmounts());

        store0.buyCart(cart);
        assertEquals(expected2, store0.getProductAmounts());
    }

    @Test
    void checkCartSuccess() {
        CartItemDTO cartItemDTO1 = mock(CartItemDTO.class);
        CartItemDTO cartItemDTO2 = mock(CartItemDTO.class);
        CartItemDTO cartItemDTO3 = mock(CartItemDTO.class);
        when(cartItemDTO1.getProductId()).thenReturn(1);
        when(cartItemDTO2.getProductId()).thenReturn(2);
        when(cartItemDTO3.getProductId()).thenReturn(3);
        when(cartItemDTO1.getAmount()).thenReturn(100);
        when(cartItemDTO2.getAmount()).thenReturn(200);
        when(cartItemDTO3.getAmount()).thenReturn(300);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, cartItemDTO1, cartItemDTO2, cartItemDTO3);

        store0.addProduct(1, 1000);
        store0.addProduct(2, 1000);
        store0.addProduct(3, 1000);

        assertEquals("",store0.checkCart(cart));
    }

    @Test
    void checkCartStoreNotActive() {
        CartItemDTO cartItemDTO1 = mock(CartItemDTO.class);
        when(cartItemDTO1.getProductId()).thenReturn(1);
        when(cartItemDTO1.getAmount()).thenReturn(100);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, cartItemDTO1);

        store0.addProduct(1, 1000);
        store0.closeStore();

        assertNotEquals("",store0.checkCart(cart));
    }

    @Test
    void checkCartProductDoesNotExist() {
        CartItemDTO cartItemDTO1 = mock(CartItemDTO.class);
        when(cartItemDTO1.getProductId()).thenReturn(1);
        when(cartItemDTO1.getAmount()).thenReturn(100);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, cartItemDTO1);

        store0.addProduct(2, 1000);

        assertNotEquals("",store0.checkCart(cart));
    }

    @Test
    void checkCartProductNotInStcok() {
        CartItemDTO cartItemDTO1 = mock(CartItemDTO.class);
        when(cartItemDTO1.getProductId()).thenReturn(1);
        when(cartItemDTO1.getAmount()).thenReturn(100);
        List<CartItemDTO> cart = new ArrayList<>();
        Collections.addAll(cart, cartItemDTO1);

        store0.addProduct(1, 10);

        assertNotEquals("",store0.checkCart(cart));
    }
}