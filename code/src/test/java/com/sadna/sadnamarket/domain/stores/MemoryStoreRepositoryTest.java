package com.sadna.sadnamarket.domain.stores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemoryStoreRepositoryTest {

    private IStoreRepository repo;

    @BeforeEach
    public void setUp() {
        repo = new MemoryStoreRepository();
    }

    @Test
    void findStoreByID() {
        Store expected0 = new Store(0, "Alice", new StoreInfo("American Eagle", "Beer Sheva", "Eagle@gmail.com", "0548970173"));
        Store expected1 = new Store(1, "Bob", new StoreInfo("Shufersal",  "Beer Sheva", "Shufersal@gmail.com", "0548970173"));
        Store expected2 = new Store(2, "Netta", new StoreInfo("H&m",  "Beer Sheva", "Hm@gmail.com", "0548970173"));

        repo.addStore("Alice", "American Eagle",  "Beer Sheva", "Eagle@gmail.com", "0548970173");
        repo.addStore("Bob", "Shufersal",  "Beer Sheva", "Shufersal@gmail.com", "0548970173");
        repo.addStore("Netta", "H&m",  "Beer Sheva", "Hm@gmail.com", "0548970173");

        assertEquals(expected0, repo.findStoreByID(0));
        assertEquals(expected1, repo.findStoreByID(1));
        assertEquals(expected2, repo.findStoreByID(2));
    }

    @Test
    void getAllStoreIds() {
        Set<Integer> expected0 = new HashSet<>();
        Set<Integer> expected1 = new HashSet<>();
        Set<Integer> expected2 = new HashSet<>();
        Set<Integer> expected3 = new HashSet<>();
        Collections.addAll(expected1, 0);
        Collections.addAll(expected2, 0, 1);
        Collections.addAll(expected3, 0, 1, 2);

        assertEquals(expected0, new HashSet<>(repo.getAllStoreIds()));

        repo.addStore("Alice", "American Eagle", "Beer Sheva", "Eagle@gmail.com", "0548970173");
        assertEquals(expected1, new HashSet<>(repo.getAllStoreIds()));

        repo.addStore("Bob", "Shufersal",  "Beer Sheva", "Shufersal@gmail.com", "0548970173");
        assertEquals(expected2, new HashSet<>(repo.getAllStoreIds()));

        repo.addStore("Netta", "H&m",  "Beer Sheva", "Hm@gmail.com", "0548970173");
        assertEquals(expected3, new HashSet<>(repo.getAllStoreIds()));
    }

    @Test
    void deleteStore() {
        repo.addStore("Alice", "American Eagle",  "Beer Sheva", "Eagle@gmail.com", "0548970173");
        repo.addStore("Bob", "Shufersal",  "Beer Sheva", "Shufersal@gmail.com", "0548970173");

        assertTrue(repo.findStoreByID(0).getIsActive());
        assertTrue(repo.findStoreByID(1).getIsActive());

        repo.deleteStore(0);
        assertFalse(repo.findStoreByID(0).getIsActive());
        assertTrue(repo.findStoreByID(1).getIsActive());

        repo.deleteStore(1);
        assertFalse(repo.findStoreByID(0).getIsActive());
        assertFalse(repo.findStoreByID(1).getIsActive());
    }

    @Test
    void addStoreAlreadyExists() {
        repo.addStore("Alice", "American Eagle",  "Beer Sheva", "Eagle@gmail.com", "0548970173");

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            repo.addStore("Alice", "American Eagle",  "Beer Sheva", "Eagle@gmail.com", "0548970173");
        });

        String expectedMessage1 = "A store with the name American Eagle already exists.";
        assertEquals(expectedMessage1, expected1.getMessage());
    }

    @Test
    void storeExists() {
        repo.addStore("Alice", "American Eagle",  "Beer Sheva", "Eagle@gmail.com", "0548970173");
        repo.addStore("Bob", "Shufersal",  "Beer Sheva", "Shufersal@gmail.com", "0548970173");
        repo.addStore("Netta", "H&m",  "Beer Sheva", "Hm@gmail.com", "0548970173");

        assertTrue(repo.storeExists(0));
        assertTrue(repo.storeExists(1));
        assertTrue(repo.storeExists(2));
        assertFalse(repo.storeExists(3));
        assertFalse(repo.storeExists(4));
    }
}