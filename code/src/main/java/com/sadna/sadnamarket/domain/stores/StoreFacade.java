package com.sadna.sadnamarket.domain.stores;

import java.time.LocalTime;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;

public class StoreFacade {
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private OrderFacade orderFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private IStoreRepository storeRepository;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public StoreFacade(IStoreRepository storeRepository) {
        // this.userFacade = userFacade;
        // this.productFacade = productFacade;
        // this.orderFacade = orderFacade;
        this.storeRepository = storeRepository;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public void setOrderFacade(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    public void setBuyPolicyFacade(BuyPolicyFacade buyPolicyFacade) {
        this.buyPolicyFacade = buyPolicyFacade;
    }

    public void setDiscountPolicyFacade(DiscountPolicyFacade discountPolicyFacade) {
        this.discountPolicyFacade = discountPolicyFacade;
    }

    public int createStore(String founderUserName, String storeName, String address, String email, String phoneNumber,
            LocalTime[] openingHours, LocalTime[] closingHours) {
        if (!userFacade.isLoggedIn(founderUserName))
            throw new IllegalArgumentException(
                    String.format("User %s has to be logged in to create a store.", founderUserName));

        int storeId = storeRepository.addStore(founderUserName, storeName, address, email, phoneNumber, openingHours,
                closingHours);
        userFacade.addStoreFounder(founderUserName, storeId);
        return storeId;
    }

    public int addProductToStore(String username, int storeId, String productName, int productQuantity,
            int productPrice, String category) {
        if (!hasPermission(username, storeId, Permission.ADD_PRODUCTS))
            throw new IllegalArgumentException(
                    String.format("user %s can not add a product to store with id %d.", username, storeId));
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if (!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        int newProductId = productFacade.addProduct(storeId, productName, productPrice, category);
        storeRepository.findStoreByID(storeId).addProduct(newProductId, productQuantity);
        return newProductId;
    }

    public synchronized int deleteProduct(String username, int storeId, int productId) {
        if (!hasPermission(username, storeId, Permission.DELETE_PRODUCTS))
            throw new IllegalArgumentException(
                    String.format("user %s can not delete a product from store with id %d.", username, storeId));

        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            store.deleteProduct(productId);
            productFacade.removeProduct(storeId, productId);
        }
        return productId;
    }

    public int updateProduct(String username, int storeId, int productId, String newProductName, int newQuantity,
            int newPrice, String newCategory) {
        if (!hasPermission(username, storeId, Permission.UPDATE_PRODUCTS))
            throw new IllegalArgumentException(
                    String.format("user %s can not update a product in store with id %d.", username, storeId));
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            if (!store.productExists(productId))
                throw new IllegalArgumentException(
                        String.format("A store with id %d does not have a product with id %d.", storeId, productId));

            store.setProductAmounts(productId, newQuantity);
            productFacade.updateProduct(storeId, productId, newProductName, newPrice, newCategory);
            return productId;
        }
    }

    public void sendStoreOwnerRequest(String currentOwnerUsername, String newOwnerUsername, int storeId) {
        if (!canAddOwnerToStore(storeId, currentOwnerUsername, newOwnerUsername))
            throw new IllegalArgumentException(String.format("User %s can not add user %s as an owner to store %d.",
                    currentOwnerUsername, newOwnerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

            userFacade.addOwnerRequest(currentOwnerUsername, newOwnerUsername, storeId);
        }
    }

    public void sendStoreManagerRequest(String currentOwnerUsername, String newManagerUsername, int storeId) {
        if (!canAddManagerToStore(storeId, currentOwnerUsername, newManagerUsername))
            throw new IllegalArgumentException(String.format("User %s can not add user %s as a manager to store %d.",
                    currentOwnerUsername, newManagerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

            userFacade.addManagerRequest(currentOwnerUsername, newManagerUsername, storeId);
        }
    }

    public void addStoreOwner(String newOwnerUsername, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreOwner(newOwnerUsername);
    }

    public void addStoreManager(String newManagerUsername, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreManager(newManagerUsername);
    }

    public void addManagerPermission(String currentOwnerUsername, String newManagerUsername, int storeId,
            Set<Permission> permission) {
        if (!canAddPermissionToManager(storeId, currentOwnerUsername, newManagerUsername))
            throw new IllegalArgumentException(String.format("User %s can not add user %s as a manager to store %d.",
                    currentOwnerUsername, newManagerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

            Set<Permission> currPermissions = new HashSet(userFacade.getMemberPermissions(newManagerUsername, storeId));
            Set<Permission> toRemove = new HashSet(currPermissions);
            toRemove.removeAll(permission);

            Set<Permission> toAdd = new HashSet(permission);
            toAdd.removeAll(currPermissions);

            for (Permission p : toRemove) {
                userFacade.removePremssionFromStore(currentOwnerUsername, newManagerUsername, storeId, p);
            }
            for (Permission p : toAdd) {
                userFacade.addPremssionToStore(currentOwnerUsername, newManagerUsername, storeId, p);
            }
        }
    }

    public boolean closeStore(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).getFounderUsername().equals(username))
            throw new IllegalArgumentException(
                    String.format("User %s can not close the store with id %d (not a founder).", username, storeId));

        storeRepository.findStoreByID(storeId).closeStore();

        String msg = String.format("The store \"%s\" was closed.",
                storeRepository.findStoreByID(storeId).getStoreInfo().getStoreName());
        List<String> ownerUsernames = storeRepository.findStoreByID(storeId).getOwnerUsernames();
        List<String> managerUsernames = storeRepository.findStoreByID(storeId).getManagerUsernames();
        for (String ownerUsername : ownerUsernames) {
            userFacade.notify(ownerUsername, msg);
        }
        for (String managerUsername : managerUsernames) {
            userFacade.notify(managerUsername, msg);
        }

        return true;
    }

    private boolean isStoreActive(int storeId) {
        return storeRepository.findStoreByID(storeId).getIsActive();
    }

    public List<MemberDTO> getOwners(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format(
                    "A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        // if(!isStoreActive(storeId))
        // throw new IllegalArgumentException(String.format("A store with id %d is not
        // active.", storeId));

        List<String> ownerUsernames = storeRepository.findStoreByID(storeId).getOwnerUsernames();
        List<MemberDTO> owners = new ArrayList<>();
        for (String ownerUsername : ownerUsernames) {
            owners.add(userFacade.getMemberDTO(ownerUsername));
        }
        return owners;
    }

    public List<MemberDTO> getManagers(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format(
                    "A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        // if(!isStoreActive(storeId))
        // throw new IllegalArgumentException(String.format("A store with id %d is not
        // active.", storeId));

        List<String> managerUsernames = storeRepository.findStoreByID(storeId).getManagerUsernames();
        List<MemberDTO> managers = new ArrayList<>();
        for (String managerUsername : managerUsernames) {
            managers.add(userFacade.getMemberDTO(managerUsername));
        }
        return managers;
    }

    public List<MemberDTO> getSellers(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format(
                    "A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        // if(!isStoreActive(storeId))
        // throw new IllegalArgumentException(String.format("A store with id %d is not
        // active.", storeId));

        List<String> sellerUsernames = storeRepository.findStoreByID(storeId).getSellerUsernames();
        List<MemberDTO> sellers = new ArrayList<>();
        for (String sellerUsername : sellerUsernames) {
            sellers.add(userFacade.getMemberDTO(sellerUsername));
        }
        return sellers;
    }

    /*
     * public String getStoreOrderHisotry( String username, int storeId) throws
     * JsonProcessingException {
     * if(!storeRepository.storeExists(storeId))
     * throw new
     * IllegalArgumentException(String.format("A store with id %d does not exist.",
     * storeId));
     * if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
     * throw new IllegalArgumentException(String.
     * format("A user %s is not an owner of store %d and can not request order history."
     * , username, storeId));
     * 
     * List<OrderDTO> orders = OrderController.getInstance().getOrders(storeId);
     * String orderHistory = String.format("Order History of store %d:\n", storeId);
     * 
     * int orderIndex = 1;
     * 
     * for(OrderDTO order : orders) {
     * orderHistory +=
     * "------------------------------------------------------------\n";
     * orderHistory += getOrderInfo(order, orderIndex);
     * orderHistory += getProductsInfo(order);
     * orderHistory +=
     * "------------------------------------------------------------\n\n";
     * orderIndex++;
     * }
     * 
     * if(orderIndex == 1) {
     * orderHistory += "There are no orders.\n";
     * }
     * 
     * return orderHistory;
     * }
     */

    public List<OrderDTO> getStoreOrderHistory(String username, int storeId) throws JsonProcessingException {
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format(
                    "A user %s is not an owner of store %d and can not request order history.", username, storeId));

        List<OrderDTO> orderDTOS = new LinkedList<>();
        for (int orderId : storeRepository.findStoreByID(storeId).getOrderIds()) {
            OrderDTO order = orderFacade.getOrderByOrderId(orderId).get(storeId);
            orderDTOS.add(order);
        }

        return orderDTOS;
    }

    private String getOrderInfo(OrderDTO orderDTO, int orderIndex) {
        String res = String.format("%d. On date %s\n", orderIndex, orderDTO.getOrderDate().toString());
        res += String.format("Store name at the time of order: \"%s\"", orderDTO.getStoreName());
        res += "Order products:\n";
        return res;
    }

    private String getProductsInfo(OrderDTO orderDTO) throws JsonProcessingException {
        List<Integer> orderProductsIds = orderDTO.getProductIds();
        int productIndex = 1;
        String res = "";

        for (int productId : orderProductsIds) {
            ProductDTO productDTO = objectMapper.readValue(orderDTO.getProductDescription(productId), ProductDTO.class);
            res += String.format("%d.\n", productIndex);
            res += String.format("%d X %s\n", orderDTO.getProductAmount(productId), productDTO);
            res += String.format("Price: %d\n\n", productDTO.getProductPrice());
            productIndex++;
        }

        return res;
    }

    public synchronized StoreDTO getStoreInfo(String username, int storeId) {
        if (!isStoreActive(storeId)) {
            if (!storeRepository.findStoreByID(storeId).isStoreOwner(username)
                    && !storeRepository.findStoreByID(storeId).isStoreManager(username))
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        }

        return storeRepository.findStoreByID(storeId).getStoreDTO();
    }

    public synchronized Map<ProductDTO, Integer> getProductsInfo(String username, int storeId, String category,
            double price, double minProductRank) throws JsonProcessingException {
        Store store = storeRepository.findStoreByID(storeId);
        if (!isStoreActive(storeId)) {
            if (!store.isStoreOwner(username) && !store.isStoreManager(username)) {
                throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
            }
        }

        List<Integer> storeProductIds = new ArrayList<>(store.getProductAmounts().keySet());
        List<ProductDTO> filteredProducts = productFacade.getFilteredProducts(storeProductIds, category, price,
                minProductRank, -1);
        Map<ProductDTO, Integer> res = new HashMap<>();
        for (ProductDTO product : filteredProducts)
            res.put(product, store.getProductAmounts().get(product.getProductID()));
        return res;
    }

    public synchronized void setStoreBankAccount(String ownerUsername, int storeId, BankAccountDTO bankAccount) {
        if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        Store store = storeRepository.findStoreByID(storeId);
        if (!store.isStoreOwner(ownerUsername))
            throw new IllegalArgumentException(String.format("User %s cannot set bank account of store %d.",
                    ownerUsername, storeId));
        
        store.setBankAccount(bankAccount);
    }

    public synchronized BankAccountDTO getStoreBankAccount(int storeId) throws JsonProcessingException {
        Store store = storeRepository.findStoreByID(storeId);
        return store.getBankAccount();
    }

    public synchronized int getProductAmount(String username, int storeId, int productId)
            throws JsonProcessingException {
        if (!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        Store store = storeRepository.findStoreByID(storeId);

        if (!store.productExists(productId))
            throw new IllegalArgumentException(
                    String.format("A product with id %d does not exist in store %d.", productId, storeId));

        return store.getProductAmounts().get(productId);
    }

    public String addSeller(int storeId, String adderUsername, String sellerUsername) {
        if (!canAddSellerToStore(storeId, adderUsername, sellerUsername))
            throw new IllegalArgumentException(
                    String.format("A user %s can not add sellers to store with id %d.", adderUsername, storeId));
        if (!userFacade.isExist(sellerUsername))
            throw new IllegalArgumentException(String.format("A user %s does not exist.", sellerUsername));

        storeRepository.findStoreByID(storeId).addSeller(sellerUsername);
        return sellerUsername;
    }

    public void addBuyPolicy(String username, int storeId, String args) {
        if (!hasPermission(username, storeId, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not add buy policy to store with id %d.", username, storeId));

        buyPolicyFacade.addBuyPolicy(storeId, args);
    }

    public void addDiscountPolicy(String username, int storeId, String args) {
        if (!hasPermission(username, storeId, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(
                    String.format("User %s can not add discount policy to store with id %d.", username, storeId));

        discountPolicyFacade.addDiscountPolicy(storeId, args);

    }

    public int addOrderId(int storeId, int orderId) {
        storeRepository.findStoreByID(storeId).addOrderId(orderId);
        return orderId;
    }

    private Map<Integer, List<CartItemDTO>> getCartByStore(List<CartItemDTO> cart) {
        Map<Integer, List<CartItemDTO>> cartByStore = new HashMap<>();
        for (CartItemDTO item : cart) {
            List<CartItemDTO> storeItems = cartByStore.getOrDefault(item.getStoreId(), new ArrayList<>());
            storeItems.add(item);
            cartByStore.put(item.getStoreId(), storeItems);
        }
        return cartByStore;
    }

    public void checkCart(String username, List<CartItemDTO> cart) {

        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        String error = "";
        for (int storeId : cartByStore.keySet()) {
            Store store = storeRepository.findStoreByID(storeId);
            synchronized (store) {
                String newError1 = store.checkCart(cartByStore.get(storeId));
                if (!newError1.equals("")) {
                    error = error + newError1 + "\n";
                }
                String newError2 = buyPolicyFacade.canBuy(storeId, cartByStore.get(storeId), username);
                if (!newError2.equals(""))
                    error = error + newError2 + "\n\n";
            }
        }

        if (!error.equals("")) {
            throw new Error(error);
        }

    }

    public boolean getIsOwner(String actorUsername, int storeId, String infoUsername){
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        Store store = storeRepository.findStoreByID(storeId);
        if (!store.isStoreOwner(actorUsername))
            throw new IllegalArgumentException(String.format("User %s cannot set bank account of store %d.",
                    actorUsername, storeId));
        return store.isStoreOwner(infoUsername);
    }

    public boolean hasProductInStock(int storeId, int productId, int amount){
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        Store store = storeRepository.findStoreByID(storeId);
        return store.hasProductInAmount(productId, amount);
    }

    public boolean getIsManager(String actorUsername, int storeId, String infoUsername){
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        Store store = storeRepository.findStoreByID(storeId);
        if (!store.isStoreOwner(actorUsername))
            throw new IllegalArgumentException(String.format("User %s cannot set bank account of store %d.",
                    actorUsername, storeId));
        return store.isStoreManager(infoUsername);
    }

    public synchronized void buyCart(String username, List<CartItemDTO> cart) {
        checkCart(username, cart);

        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        for (int storeId : cartByStore.keySet()) {
            Store store = storeRepository.findStoreByID(storeId);
            store.buyCart(cartByStore.get(storeId));
        }
    }

    // return a map from store id to a List that coontain object thats stores : id,
    // amount, original price and new price
    public synchronized Map<Integer, List<ProductDataPrice>> calculatePrice(String username, List<CartItemDTO> cart) {
        Map<Integer, List<ProductDataPrice>> mapPrice = new HashMap<>();
        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        for (int storeId : cartByStore.keySet()) {
            mapPrice.put(storeId, discountPolicyFacade.calculatePrice(storeId,
                    cartByStore.get(storeId)));
        }
        return mapPrice;
    }

    private boolean hasPermission(String username, int storeId, Permission permission) {
        if (!userFacade.isLoggedIn(username))
            return false;

        Store store = storeRepository.findStoreByID(storeId);
        if (store.isStoreOwner(username))
            return true;

        if (store.isStoreManager(username))
            return userFacade.checkPremssionToStore(username, storeId, permission);

        return false;
    }

    private boolean canAddOwnerToStore(int storeId, String currOwnerUsername, String newOwnerUsername) {
        return hasPermission(currOwnerUsername, storeId, Permission.ADD_OWNER) &&
                userFacade.isExist(newOwnerUsername) &&
                (!storeRepository.findStoreByID(storeId).isStoreOwner(newOwnerUsername));
    }

    private boolean canAddManagerToStore(int storeId, String currOwnerUsername, String newManagerUsername) {
        return hasPermission(currOwnerUsername, storeId, Permission.ADD_MANAGER) &&
                userFacade.isExist(newManagerUsername) &&
                (!storeRepository.findStoreByID(storeId).isStoreManager(newManagerUsername));
    }

    private boolean canAddPermissionToManager(int storeId, String currOwnerUsername, String newManagerUsername) {
        return hasPermission(currOwnerUsername, storeId, Permission.ADD_MANAGER) &&
                userFacade.isExist(newManagerUsername) &&
                (storeRepository.findStoreByID(storeId).isStoreManager(newManagerUsername));
    }

    private boolean canAddSellerToStore(int storeId, String currOwnerUsername, String newSellerUsername) {
        return hasPermission(currOwnerUsername, storeId, Permission.ADD_SELLER) &&
                userFacade.isExist(newSellerUsername) &&
                (!storeRepository.findStoreByID(storeId).isSeller(newSellerUsername));
    }

    public StoreInfo getStoreInfo(int storeId) {
        return storeRepository.findStoreByID(storeId).getStoreInfo();
    }
}
