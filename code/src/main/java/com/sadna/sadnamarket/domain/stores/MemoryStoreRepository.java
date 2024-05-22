package com.sadna.sadnamarket.domain.stores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryStoreRepository implements IStoreRepository{
    private int nextStoreId;
    private Map<Integer, Store> stores;

    public MemoryStoreRepository() {
        this.nextStoreId = 0;
        this.stores = new HashMap<>();
    }

    @Override
    public Store findStoreByID(int storeId) {
        if(!stores.containsKey(storeId))
            throw new IllegalArgumentException(String.format("There is no store with id %d.", storeId));

        return stores.get(storeId);
    }

    @Override
    public Set<Integer> getAllStoreIds() {
        return stores.keySet();
    }

    @Override
    public void deleteStore(int storeId) {
        findStoreByID(storeId).closeStore();
    }

    @Override
    public int addStore(int founderId, String storeName) {
        if(storeExists(nextStoreId))
            throw new IllegalArgumentException(String.format("A store with the id \"%d\" already exists.", nextStoreId));
        if(storeNameExists(storeName))
            throw new IllegalArgumentException(String.format("A store with the name \"%s\" already exists.", storeName));

        Store createdStore = new Store(nextStoreId, storeName, founderId);
        stores.put(nextStoreId, createdStore);
        nextStoreId++;
        return nextStoreId - 1;
    }

    public boolean storeExists(int storeId) {
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
