package com.sadna.sadnamarket.domain.stores;

import java.time.LocalTime;
import java.util.Set;

public interface IStoreRepository {
    public Store findStoreByID(int storeId);
    public Store findStoreByName(String storeName);
    public Set<Integer> getAllStoreIds();
    public void deleteStore(int storeId);
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber);
    public boolean storeExists(int storeId);

}
