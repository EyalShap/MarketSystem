package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.service.Error;

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
    //private List<String> sellerUsernames;
    private List<Integer> orderIds;
    private BankAccountDTO bankAccount;
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
        //this.sellerUsernames = Collections.synchronizedList(new ArrayList<>());
        this.orderIds = Collections.synchronizedList(new ArrayList<>());
    }

    public int getStoreId() {
        return this.storeId;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public BankAccountDTO getBankAccount() {
        return bankAccount;
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

    /*public List<String> getSellerUsernames() {
        return this.sellerUsernames;
    }*/

    public Map<Integer, Integer> getProductAmounts() {
        return this.productAmounts;
    }

    public List<Integer> getOrderIds() {
        return this.orderIds;
    }

    public void setBankAccount(BankAccountDTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void addProduct(int productId, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException(Error.makeStoreIllegalProductAmountError(amount));

        synchronized (productAmounts) {
            if (productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductAlreadyExistsError(productId));

            productAmounts.put(productId, amount);
        }
    }

    public void deleteProduct(int productId) {
        synchronized (productAmounts) {
            if (!isActive)
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            if (!productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId,productId));

            productAmounts.remove(productId);
        }
    }

    public void setProductAmounts(int productId, int newAmount) {
        if (newAmount < 0)
            throw new IllegalArgumentException(Error.makeStoreIllegalProductAmountError(newAmount));

        synchronized (productAmounts) {
            if (!isActive)
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            if (!productExists(productId))
                throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));

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

    public Set<String> updateStock(List<CartItemDTO> cart) {
        synchronized (productAmounts) {
            Set<String> checkCartRes = checkCart(cart);
            if (checkCartRes.size() != 0)
                return checkCartRes;

            for (CartItemDTO item : cart) {
                int newAmount = productAmounts.get(item.getProductId()) - item.getAmount();
                setProductAmounts(item.getProductId(), newAmount);
            }
            return new HashSet<>();
        }
    }

    public boolean productExists(int productId) {
        return productAmounts.containsKey(productId);
    }

    public boolean hasProductInAmount(int productId, int amount) {
        return productAmounts.containsKey(productId) && amount <= productAmounts.get(productId);
    }

    public boolean isStoreOwner(String username) {
        return ownerUsernames.contains(username);
    }

    public boolean isStoreManager(String username) {
        return managerUsernames.contains(username);
    }

    /*public boolean isSeller(String username) {
        return sellerUsernames.contains(username);
    }*/

    public void addStoreOwner(String newOwnerUsername) {
        synchronized (ownerUsernames) {
            if (!isActive)
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            if (isStoreOwner(newOwnerUsername))
                throw new IllegalArgumentException(Error.makeStoreUserAlreadyOwnerError(newOwnerUsername, storeId));

            ownerUsernames.add(newOwnerUsername);
        }
    }

    public void addStoreManager(String newManagerUsername) {
        synchronized (managerUsernames) {
            if (!isActive)
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            if (isStoreManager(newManagerUsername))
                throw new IllegalArgumentException(Error.makeStoreUserAlreadyManagerError(newManagerUsername, storeId));

            managerUsernames.add(newManagerUsername);
        }
    }

    /*public synchronized void addSeller(String sellerUsername) {
        synchronized (sellerUsername) {
            if (!isActive)
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            if (isSeller(sellerUsername))
                throw new IllegalArgumentException(
                        String.format("User %s is already a seller of store %d.", sellerUsername, storeId));

            this.sellerUsernames.add(sellerUsername);
        }
    }*/

    public void closeStore() {
        synchronized (lock) {
            if (!this.isActive)
                throw new IllegalArgumentException(Error.makeStoreAlreadyClosedError(storeId));

            this.isActive = false;
        }
    }

    public void reopenStore() {
        synchronized (lock) {
            if (this.isActive)
                throw new IllegalArgumentException(Error.makeStoreAlreadyClosedError(storeId));

            this.isActive = true;
        }
    }

    public StoreDTO getStoreDTO() {
        return new StoreDTO(this);
    }

    public void addOrderId(int orderId) {
        synchronized (orderIds) {
            synchronized (lock) {
                if (!isActive)
                    throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

                if (orderIds.contains(orderId))
                    throw new IllegalArgumentException(Error.makeStoreOrderAlreadyExistsError(storeId, orderId));

                this.orderIds.add(orderId);
            }
        }
    }

    /*private boolean isStoreOpen() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfWeek().getValue(); // monday = 1, sunday = 7
        int dayIndex = day == 7 ? 0 : day;
        LocalTime dayOpeningHour = storeInfo.getOpeningHours()[dayIndex];
        LocalTime dayClosingHour = storeInfo.getClosingHours()[dayIndex];
        return now.toLocalTime().isAfter(dayOpeningHour) && now.toLocalTime().isBefore(dayClosingHour);
    }*/

    public Set<String> checkCart(List<CartItemDTO> cart) {
        Set<String> error = new HashSet<>();
        synchronized (productAmounts) {
            synchronized (lock) {
                if (!isActive) {
                    error.add(Error.makeStoreClosedError(storeId));
                    return error;
                }
                for (CartItemDTO item : cart) {
                    if (!productExists(item.getProductId()))
                        error.add(Error.makeProductDoesntExistInStoreError(storeId, item.getProductId()));
                    else if (item.getAmount() > productAmounts.get(item.getProductId()))
                        error.add(Error.makeNotEnoughInStcokError(storeId, item.getProductId(), item.getAmount(), productAmounts.get(item.getProductId())));
                }
                return error;
            }
        }
    }

    public boolean hasProducts(Set<Integer> productIds) {
        for(int productId : productIds)
            if(productId != -1) {
                if(!productExists(productId))
                    return false;
            }
        return true;
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
                //Objects.equals(sellerUsernames, store.sellerUsernames) &&
                Objects.equals(orderIds, store.orderIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, isActive, storeInfo, productAmounts, founderUsername, ownerUsernames,
                managerUsernames, orderIds, lock);
    }
}
