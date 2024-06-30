package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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

    public boolean areStoresEqual(StoreDTO s1, StoreDTO s2);

    public boolean areProductsInStore(int storeId, Set<Integer> productIds);

    public Map<ProductDTO, Integer> getProductsInfoAndFilter(ProductFacade productFacade, int storeId, String productName, String category, double price, double minProductRank);
}