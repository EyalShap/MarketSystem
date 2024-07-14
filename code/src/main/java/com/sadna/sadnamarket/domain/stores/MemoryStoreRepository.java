package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;
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
    public void setStoreBankAccount(int storeId, String ownerUsername, BankAccountDTO bankAccountDTO) {
        Store store = findStoreByID(storeId);
        synchronized (store) {
            if (!store.isStoreOwner(ownerUsername))
                throw new IllegalArgumentException(Error.makeStoreUserCannotSetBankAccountError(ownerUsername, storeId));

            bankAccountDTO.setStore(store);
            store.setBankAccount(bankAccountDTO);
        }
    }

    @Override
    public BankAccountDTO getStoreBankAccount(int storeId) {
        Store store = findStoreByID(storeId);
        return store.getBankAccount();
    }

    /*@Override
    public boolean areStoresEqual(StoreDTO s1, StoreDTO s2) {
        if(!s1.equals(s2))
            return false;
        return Objects.equals(s1.getProductAmounts(), s2.getProductAmounts());
    }*/

    @Override
    public boolean areProductsInStore(int storeId, Set<Integer> productIds) {
        Store store = findStoreByID(storeId);
        return store.hasProducts(productIds);
    }

    @Override
    public void addManagerToStore(String username, int storeId) {
        Store store = findStoreByID(storeId);
        store.addStoreManager(username);
    }

    @Override
    public void addOwnerToStore(String username, int storeId) {
        Store store = findStoreByID(storeId);
        store.addStoreOwner(username);
    }

    @Override
    public void addOrderIdToStore(int storeId, int orderId) {
        findStoreByID(storeId).addOrderId(orderId);
    }

    @Override
    public void changeStoreState(String username, int storeId, boolean open, UserFacade userFacade) {
        Store store = findStoreByID(storeId);
        if (!store.getFounderUsername().equals(username))
            throw new IllegalArgumentException(Error.makeStoreUserCannotCloseStoreError(username, storeId));

        if(!open) {
            store.closeStore();
        }
        else {
            store.reopenStore();
        }

        notifyAboutStore(open, store, userFacade);
    }

    private void notifyAboutStore(boolean open, Store store, UserFacade userFacade) {
        String format = open ? "The store \"%s\" was reopened." : "The store \"%s\" was closed.";
        String msg = String.format(format, store.getStoreInfo().getStoreName());
        Set<String> ownerUsernames = store.getOwnerUsernames();
        Set<String> managerUsernames = store.getManagerUsernames();
        for (String ownerUsername : ownerUsernames) {
            userFacade.notify(ownerUsername, msg);
        }
        for (String managerUsername : managerUsernames) {
            userFacade.notify(managerUsername, msg);
        }
    }

    @Override
    public Map<ProductDTO, Integer> getProductsInfoAndFilter(ProductFacade productFacade, int storeId, String username, String productName, String category, double price, double minProductRank) {
        Store store = findStoreByID(storeId);
        if (!store.getIsActive()) {
            if (!store.isStoreOwner(username) && !store.isStoreManager(username)) {
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            }
        }

        Map<Integer, Integer> productAmounts = store.getProductAmounts();
        List<Integer> storeProductIds = new ArrayList<>(productAmounts.keySet());
        List<ProductDTO> filteredProducts = productFacade.getFilteredProducts(storeProductIds, productName, price, category, minProductRank);
        Map<ProductDTO, Integer> res = new HashMap<>();
        for (ProductDTO product : filteredProducts)
            res.put(product, productAmounts.get(product.getProductID()));
        return res;
    }

    @Override
    public boolean hasPermission(String username, Store store, Permission permission, UserFacade userFacade) {
        if (!userFacade.isLoggedIn(username))
            return false;

        if (store.isStoreOwner(username))
            return true;

        if (store.isStoreManager(username))
            return userFacade.checkPremssionToStore(username, store.getStoreId(), permission);

        return false;
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
    public void addProductToStore(int storeId, int productId, int amount) {
        if(!findStoreByID(storeId).getIsActive())
            throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
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
        Store store = findStoreByID(storeId);
        return store.getProductAmount(productId);
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
    public Set<String> updateStockInStore(int storeId, List<CartItemDTO> cart, UserFacade userFacade, String username) {
        Store store = findStoreByID(storeId);
        Set<String> errors = store.updateStock(cart);
        if(errors.size() == 0) {
            for(String owner : store.getOwnerUsernames()){
                userFacade.notify(owner, "User " + (username != null ? username+" " : "") + "made a purchase in your store " + store.getStoreInfo().getStoreName());
            }
        }
        return errors;
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
