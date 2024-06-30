package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface IStoreRepository {
    public Store findStoreByID(int storeId);
    public Set<Integer> getAllStoreIds();
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber);
    public boolean storeExists(int storeId);
    public boolean productExists(int storeId, int productId);
    public boolean hasProductInStock(int storeId, int productId, int amount);
    public int getProductAmountInStore(int storeId, int productId);
    public void saveStore(Store store);
    public void addProductToStore(int storeId, int productId, int amount);
    public void deleteProductFromStore(int storeId, int productId);
    public void updateProductAmountInStore(int storeId, int productId, int newAmount);
    public StoreDTO getStoreDTO(int storeId);
    public Set<String> checkCartInStore(int storeId, List<CartItemDTO> cart);
    public Set<String> updateStockInStore(int storeId, List<CartItemDTO> cart);
    public Store findStoreByName(String storeName);
    public void setStoreBankAccount(BankAccountDTO bankAccountDTO);
    public BankAccountDTO getStoreBankAccount(int storeId);

}
