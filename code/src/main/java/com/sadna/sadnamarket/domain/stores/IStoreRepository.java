package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;

import java.time.LocalTime;
import java.util.Set;

public interface IStoreRepository {
    public Store findStoreByID(int storeId);
    public Set<Integer> getAllStoreIds();
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber);
    public boolean storeExists(int storeId);
    public void saveStore(Store store);
    public void addProductToStore(int storeId, int productId, int amount);
    public boolean productExists(int storeId, int productId);
}
