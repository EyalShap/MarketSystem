package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.service.Error;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalTime;
import java.util.*;
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
    public Store findStoreByName(String storeName) {
        synchronized (stores) {
            for(Store store : stores.values()) {
                if(store.getStoreInfo().getStoreName().equals(storeName)) {
                    return store;
                }
            }
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithNameError(storeName));
        }
    }

    @Override
    public void setStoreBankAccount(BankAccountDTO bankAccountDTO) {
    }

    @Override
    public BankAccountDTO getStoreBankAccount(int storeId) {
        Store store = findStoreByID(storeId);
        return store.getBankAccount();
    }

    @Override
    public boolean areStoresEqual(StoreDTO s1, StoreDTO s2) {
        if(!s1.equals(s2))
            return false;
        return Objects.equals(s1.getProductAmounts(), s2.getProductAmounts());
    }

    @Override
    public boolean areProductsInStore(int storeId, Set<Integer> productIds) {
        Store store = findStoreByID(storeId);
        return store.hasProducts(productIds);
    }

    @Override
    public Map<ProductDTO, Integer> getProductsInfoAndFilter(ProductFacade productFacade, int storeId, String productName, String category, double price, double minProductRank) {
        Store store = findStoreByID(storeId);

        Map<Integer, Integer> productAmounts = store.getProductAmounts();
        List<Integer> storeProductIds = new ArrayList<>(productAmounts.keySet());
        List<ProductDTO> filteredProducts = productFacade.getFilteredProducts(storeProductIds, productName, price, category, minProductRank);
        Map<ProductDTO, Integer> res = new HashMap<>();
        for (ProductDTO product : filteredProducts)
            res.put(product, productAmounts.get(product.getProductID()));
        return res;
    }

    /*@Override
    public Map<Integer, Integer> getStoreProducts(int storeId) {
        return findStoreByID(storeId).getProductAmounts();
    }*/

    @Override
    public Set<Integer> getAllStoreIds() {
        synchronized (stores) {
            return stores.keySet();
        }
    }

    /*@Override
    public void deleteStore(int storeId) {
        findStoreByID(storeId).closeStore();
    }*/

    @Override
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber) {
        synchronized (stores) {
            if (storeExists(nextStoreId))
                throw new IllegalArgumentException(
                        Error.makeStoreWithIdAlreadyExistsError(nextStoreId));
            if (storeNameExists(storeName))
                throw new IllegalArgumentException(
                        Error.makeStoreWithNameAlreadyExistsError(storeName));

            Store createdStore = new Store(nextStoreId, founderUsername, new StoreInfo(storeName, address, email, phoneNumber));
            stores.put(nextStoreId, createdStore);
            nextStoreId++;
            return nextStoreId - 1;
        }
    }

    public boolean storeExists(int storeId) {
        return stores.containsKey(storeId);
    }

    @Override
    public void saveStore(Store store) {
    }

    @Override
    public void addProductToStore(int storeId, int productId, int amount) {
        if (productExists(storeId, productId))
            throw new IllegalArgumentException(Error.makeStoreProductAlreadyExistsError(productId));

        findStoreByID(storeId).addProduct(productId, amount);
    }

    @Override
    public boolean productExists(int storeId, int productId) {
        return findStoreByID(storeId).productExists(productId);
    }

    @Override
    public boolean hasProductInStock(int storeId, int productId, int amount) {
        return findStoreByID(storeId).hasProductInAmount(productId, amount);
    }

    @Override
    public int getProductAmountInStore(int storeId, int productId) {
        return findStoreByID(storeId).getProductAmount(productId);
    }

    @Override
    public void deleteProductFromStore(int storeId, int productId) {
        Store store = findStoreByID(storeId);
        store.deleteProduct(productId);
    }

    @Override
    public void updateProductAmountInStore(int storeId, int productId, int newAmount) {
        findStoreByID(storeId).setProductAmounts(productId, newAmount);
    }

    @Override
    public StoreDTO getStoreDTO(int storeId) {
        return findStoreByID(storeId).getStoreDTO();
    }

    @Override
    public Set<String> checkCartInStore(int storeId, List<CartItemDTO> cart) {
        return findStoreByID(storeId).checkCart(cart);
    }

    @Override
    public Set<String> updateStockInStore(int storeId, List<CartItemDTO> cart) {
        return findStoreByID(storeId).updateStock(cart);
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
