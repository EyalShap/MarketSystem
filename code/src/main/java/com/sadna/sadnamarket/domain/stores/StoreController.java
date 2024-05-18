package com.sadna.sadnamarket.domain.stores;

import java.util.HashMap;

public class StoreController {
    private static StoreController instance;
    private int nextStoreId;
    private HashMap<Integer, Store> stores;

    private StoreController() {
        this.nextStoreId = 0;
        this.stores = new HashMap<>();
    }

    public static StoreController getInstance() {
        if (instance == null) {
            instance = new StoreController();
        }
        return instance;
    }

    // returns id of newly created store
    public int createStore(int founderId, String storeName) {
        // if store already exists, throw an exception
        if(storeIdExists(nextStoreId))
            throw new IllegalArgumentException(String.format("A store with the id \"%d\" already exists.", nextStoreId));
        if(storeNameExists(storeName))
            throw new IllegalArgumentException(String.format("A store with the name \"%s\" already exists.", storeName));

        Store createdStore = new Store(nextStoreId, storeName, founderId);
        stores.put(nextStoreId, createdStore);
        nextStoreId++;
        return nextStoreId - 1;
    }

    private boolean storeIdExists(int storeId) {
        return stores.containsKey(storeId);
    }

    private boolean storeNameExists(String storeName) {
        for(Store store : stores.values()) {
            if(store.getStoreName().equals(storeName)) {
                return true;
            }
        }
        return false;
    }
}
