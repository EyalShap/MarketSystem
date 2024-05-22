package com.sadna.sadnamarket.domain.stores;

import java.util.Set;

public interface IStoreRepository {
    public Store findStoreByID(int storeId);
    public Set<Integer> getAllStoreIds();
    public void deleteStore(int storeId);
    public int addStore(int founderId, String storeName);
    public boolean storeExists(int storeId);
}
