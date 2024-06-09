package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.service.Error;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStoreRepository implements IStoreRepository {
    private int nextStoreId;
    private Map<Integer, Store> stores;

    public MemoryStoreRepository() {
        this.nextStoreId = 0;
        this.stores = new ConcurrentHashMap<>();
    }

    @Override
    public Store findStoreByID(int storeId) {
        synchronized (stores) {
            if (!stores.containsKey(storeId))
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));

            return stores.get(storeId);
        }
    }

    @Override
    public Set<Integer> getAllStoreIds() {
        synchronized (stores) {
            return stores.keySet();
        }
    }

    @Override
    public void deleteStore(int storeId) {
        findStoreByID(storeId).closeStore();
    }

    @Override
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours) {
        synchronized (stores) {
            if (storeExists(nextStoreId))
                throw new IllegalArgumentException(
                        Error.makeStoreWithIdAlreadyExistsError(nextStoreId));
            if (storeNameExists(storeName))
                throw new IllegalArgumentException(
                        Error.makeStoreWithNameAlreadyExistsError(storeName));

            Store createdStore = new Store(nextStoreId, founderUsername, new StoreInfo(storeName, address, email, phoneNumber, openingHours, closingHours));
            stores.put(nextStoreId, createdStore);
            nextStoreId++;
            return nextStoreId - 1;
        }
    }

    public boolean storeExists(int storeId) {
        return stores.containsKey(storeId);
    }

    private boolean storeNameExists(String storeName) {
        synchronized (stores) {
            for (Store store : stores.values()) {
                if (store.getStoreInfo().getStoreName().equals(storeName)) {
                    return true;
                }
            }
            return false;
        }
    }
}
