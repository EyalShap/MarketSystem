package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private int storeId;
    private boolean isActive;
    private StoreInfo storeInfo;
    private Map<Integer, Integer> productAmounts;
    private String founderUsername;
    private List<String> ownerUsernames;
    private List<String> managerUsernames;
    private List<String> sellerUsernames;
    private List<Integer> orderIds;
    private final Object lock = new Object();

    public Store(int storeId, String founderUsername, StoreInfo storeInfo) {
        this.storeId = storeId;
        this.isActive = true;
        this.storeInfo = storeInfo;
        this.productAmounts = new ConcurrentHashMap<>();
        this.founderUsername = founderUsername;
        this.ownerUsernames = Collections.synchronizedList(new ArrayList<>());
        this.ownerUsernames.add(founderUsername);
        this.managerUsernames = Collections.synchronizedList(new ArrayList<>());
        this.sellerUsernames = Collections.synchronizedList(new ArrayList<>());
        this.orderIds = Collections.synchronizedList(new ArrayList<>());
    }

    public int getStoreId() {
        return this.storeId;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public String getFounderUsername() {
        return this.founderUsername;
    }

    public boolean getIsActive() {
        synchronized (lock) {
            return this.isActive;
        }
    }

    public List<String> getOwnerUsernames() {
        return this.ownerUsernames;
    }

    public List<String> getManagerUsernames() {
        return this.managerUsernames;
    }

    public List<String> getSellerUsernames() {
        return this.sellerUsernames;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return this.productAmounts;
    }

    public List<Integer> getOrderIds() {
        return this.orderIds;
    }

    public void addProduct(int productId, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException(String.format("%d is an illegal amount of products.", amount));

        synchronized (productAmounts) {
            if (productExists(productId))
                throw new IllegalArgumentException(String.format("A product with id %d already exists.", productId));

            productAmounts.put(productId, amount);
        }
    }

    public void deleteProduct(int productId) {
        synchronized (productAmounts) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (!productExists(productId))
                throw new IllegalArgumentException(String.format("A product with id %d does not exist.", productId));

            productAmounts.remove(productId);
        }
    }

    public void setProductAmounts(int productId, int newAmount) {
        if (newAmount < 0)
            throw new IllegalArgumentException(String.format("%d is an illegal amount of products.", newAmount));

        synchronized (productAmounts) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (!productExists(productId))
                throw new IllegalArgumentException(String.format("A product with id %d does not exist.", productId));

            productAmounts.put(productId, newAmount);
        }
    }

    /*
     * public int buyStoreProduct(int productId, int amount) {
     * if(!checkItem(productId, amount))
     * throw new IllegalArgumentException(String.
     * format("%d of product %d can not be purchased.", amount, productId));
     * 
     * int newAmount = productAmounts.get(productId) - amount;
     * setProductAmounts(productId, newAmount);
     * return newAmount;
     * }
     */

    public void buyCart(List<CartItemDTO> cart) {
        synchronized (productAmounts) {
            if (!checkCart(cart).equals(""))
                throw new IllegalArgumentException("This cart can not be purchased.");

            for (CartItemDTO item : cart) {
                int newAmount = productAmounts.get(item.getProductId()) - item.getAmount();
                setProductAmounts(item.getProductId(), newAmount);
            }
        }
    }

    public boolean productExists(int productId) {
        return productAmounts.containsKey(productId);
    }

    public boolean isStoreOwner(String username) {
        return ownerUsernames.contains(username);
    }

    public boolean isStoreManager(String username) {
        return managerUsernames.contains(username);
    }

    public boolean isSeller(String username) {
        return sellerUsernames.contains(username);
    }

    public void addStoreOwner(String newOwnerUsername) {
        synchronized (ownerUsernames) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (isStoreOwner(newOwnerUsername))
                throw new IllegalArgumentException(
                        String.format("User %s is already a owner of store %d.", newOwnerUsername, storeId));

            ownerUsernames.add(newOwnerUsername);
        }
    }

    public void addStoreManager(String newManagerUsername) {
        synchronized (managerUsernames) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (isStoreManager(newManagerUsername))
                throw new IllegalArgumentException(
                        String.format("User %s is already a manager of store %d.", newManagerUsername, storeId));

            managerUsernames.add(newManagerUsername);
        }
    }

    public synchronized void addSeller(String sellerUsername) {
        synchronized (sellerUsername) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (isSeller(sellerUsername))
                throw new IllegalArgumentException(
                        String.format("User %s is already a seller of store %d.", sellerUsername, storeId));

            this.sellerUsernames.add(sellerUsername);
        }
    }

    public void closeStore() {
        synchronized (lock) {
            if (!this.isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is already closed.", storeId));

            this.isActive = false;
        }
    }

    public StoreDTO getStoreDTO() {
        return new StoreDTO(this);
    }

    public void addOrderId(int orderId) {
        synchronized (orderIds) {
            synchronized (lock) {
                if (!isActive)
                    throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

                if (orderIds.contains(orderId))
                    throw new IllegalArgumentException(
                            String.format("A order with id %d already exists in store %d.", orderId, storeId));

                this.orderIds.add(orderId);
            }
        }
    }

    public String checkCart(List<CartItemDTO> cart) {
        String error = "";
        synchronized (productAmounts) {
            synchronized (lock) {
                if (!isActive)
                    error = error + String.format("Store %d is closed.\n", storeId);
                for (CartItemDTO item : cart) {
                    if (!productExists(item.getProductId()))
                        error = String.format("Product %d does not exist in store %d.\n", item.getProductId(), storeId);
                    if (item.getAmount() > productAmounts.get(item.getProductId()))
                        error = String.format("You can not buy %d of product %d - there are only %d in stock.\n",
                                item.getAmount(), item.getProductId(), productAmounts.get(item.getProductId()));
                }
                return error;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Store store = (Store) o;
        StoreInfo info = store.getStoreInfo();
        if (!info.equals(storeInfo))
            return false;

        return storeId == store.storeId &&
                isActive == store.isActive &&
                Objects.equals(productAmounts, store.productAmounts) &&
                Objects.equals(founderUsername, store.founderUsername) &&
                Objects.equals(ownerUsernames, store.ownerUsernames) &&
                Objects.equals(managerUsernames, store.managerUsernames) &&
                Objects.equals(sellerUsernames, store.sellerUsernames) &&
                Objects.equals(orderIds, store.orderIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, isActive, storeInfo, productAmounts, founderUsername, ownerUsernames,
                managerUsernames, sellerUsernames, orderIds, lock);
    }
}
