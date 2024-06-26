package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.service.Error;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Embedded
    private StoreInfo storeInfo;

    @ElementCollection
    @CollectionTable(name = "store_products", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "amount")
    private Map<Integer, Integer> productAmounts;

    @Column(name = "founder_username")
    private String founderUsername;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_owners", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "username")
    private Set<String> ownerUsernames;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_managers", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "username")
    private Set<String> managerUsernames;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_orders", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "order_ids")
    private Set<Integer> orderIds;

    @Embedded
    private BankAccountDTO bankAccount;

    @Transient
    private final Object lock = new Object();

    public Store(int storeId, String founderUsername, StoreInfo storeInfo) {
        this.storeId = storeId;
        setAnythingButId(founderUsername, storeInfo);
    }

    public Store(String founderUsername, StoreInfo storeInfo) {
        setAnythingButId(founderUsername, storeInfo);
    }

    public Store() {}


    private void setAnythingButId(String founderUsername, StoreInfo storeInfo) {
        this.isActive = true;
        this.storeInfo = storeInfo;
        this.productAmounts = new ConcurrentHashMap<>();
        this.founderUsername = founderUsername;
        this.ownerUsernames = Collections.synchronizedSet(new HashSet<>());
        this.ownerUsernames.add(founderUsername);
        this.managerUsernames = Collections.synchronizedSet(new HashSet<>());
        this.orderIds = Collections.synchronizedSet(new HashSet<>());
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

    public Set<String> getOwnerUsernames() {
        return this.ownerUsernames;
    }

    public Set<String> getManagerUsernames() {
        return this.managerUsernames;
    }

    /*public Set<String> getSellerUsernames() {
        return this.sellerUsernames;
    }*/

    public Map<Integer, Integer> getProductAmounts() {
        return this.productAmounts;
    }

    public Set<Integer> getOrderIds() {
        return this.orderIds;
    }

    public void setBankAccount(BankAccountDTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void addProduct(int productId, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException(Error.makeStoreIllegalProductAmountError(amount));

        synchronized (productAmounts) {
            productAmounts.put(productId, amount);
        }
    }

    public void deleteProduct(int productId) {
        HashMap<Integer, Integer> newMap = new HashMap<>(productAmounts);
        newMap.remove(productId);

        synchronized (productAmounts) {
            if (!isActive)
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            if (!productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId,productId));

            productAmounts = newMap;
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
        synchronized (productAmounts) {
            return productAmounts.containsKey(productId);
        }
    }

    public boolean hasProductInAmount(int productId, int amount) {
        synchronized (productAmounts) {
            return productAmounts.containsKey(productId) && amount <= productAmounts.get(productId);
        }
    }

    public boolean isStoreOwner(String username) {
        synchronized (ownerUsernames) {
            return ownerUsernames.contains(username);
        }
    }

    public boolean isStoreManager(String username) {
        synchronized (managerUsernames) {
            return managerUsernames.contains(username);
        }
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

    public int getProductAmount(int productId) {
        synchronized (productAmounts) {
            if (!productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));
            return productAmounts.get(productId);
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
                //Objects.equals(sellerUsernames, store.sellerUsernames) &&
                Objects.equals(orderIds, store.orderIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, isActive, storeInfo, productAmounts, founderUsername, ownerUsernames,
                managerUsernames, orderIds, lock);
    }
}
