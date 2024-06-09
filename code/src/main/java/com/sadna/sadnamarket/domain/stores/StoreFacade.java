package com.sadna.sadnamarket.domain.stores;

import java.time.LocalTime;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.buyPolicies.BuyType;
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
import com.sadna.sadnamarket.service.Error;

public class StoreFacade {
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private OrderFacade orderFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private IStoreRepository storeRepository;

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

    public int createStore(String founderUserName, String storeName, String address, String email, String phoneNumber, LocalTime[] openingHours, LocalTime[] closingHours) {
        if (!userFacade.isLoggedIn(founderUserName))
            throw new IllegalArgumentException(
                    Error.makeStoreUserHasToBeLoggedInError(founderUserName));

        int storeId = storeRepository.addStore(founderUserName, storeName, address, email, phoneNumber, openingHours, closingHours);
        userFacade.addStoreFounder(founderUserName, storeId);

        // adding default buy policies (laws)
        List<BuyType> buyTypes1 = new ArrayList<>();
        List<BuyType> buyTypes2 = new ArrayList<>();
        buyTypes1.add(BuyType.immidiatePurchase);
        buyTypes2.add(BuyType.immidiatePurchase);
        try {
            // this will not throw an exception since all the parameters are legal
            int policyId1 = buyPolicyFacade.createCategoryAgeLimitBuyPolicy("Alcohol", buyTypes1, 18, -1, founderUserName);
            int policyId2 = buyPolicyFacade.createCategoryHourLimitBuyPolicy("Alcohol", buyTypes2, LocalTime.of(6, 0), LocalTime.of(23, 0), founderUserName);
            buyPolicyFacade.addBuyPolicyToStore(founderUserName, storeId, policyId1);
            buyPolicyFacade.addBuyPolicyToStore(founderUserName, storeId, policyId2);
        }
        catch (Exception e) {}
        return storeId;
    }

    public int addProductToStore(String username, int storeId, String productName, int productQuantity,
            double productPrice, String category, double rank, double productWeight) {
        if (!hasPermission(username, storeId, Permission.ADD_PRODUCTS))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddProductError(username, storeId));
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        if (!isStoreActive(storeId))
            throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

        int newProductId = productFacade.addProduct(storeId, productName, productPrice, category, rank, productWeight);
        storeRepository.findStoreByID(storeId).addProduct(newProductId, productQuantity);
        return newProductId;
    }

    public int deleteProduct(String username, int storeId, int productId) {
        if (!hasPermission(username, storeId, Permission.DELETE_PRODUCTS))
            throw new IllegalArgumentException(Error.makeStoreUserCannotDeleteProductError(username, storeId));

        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store.getProductAmounts()) {
            store.deleteProduct(productId);
            productFacade.removeProduct(storeId, productId);
        }
        return productId;
    }

    public int updateProduct(String username, int storeId, int productId, String newProductName, int newQuantity,
            double newPrice, String newCategory, double newRank) {
        if (!hasPermission(username, storeId, Permission.UPDATE_PRODUCTS))
            throw new IllegalArgumentException(Error.makeStoreUserCannotUpdateProductError(username, storeId));
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store.getProductAmounts()) {
            if (!store.productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));

            store.setProductAmounts(productId, newQuantity);
            productFacade.updateProduct(storeId, productId, newProductName, newPrice, newCategory, newRank);
            return productId;
        }
    }

    public int updateProductAmount(String username, int storeId, int productId, int newQuantity) {
        if (!hasPermission(username, storeId, Permission.UPDATE_PRODUCTS))
            throw new IllegalArgumentException(Error.makeStoreUserCannotUpdateProductError(username, storeId));
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            if (!store.productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));

            store.setProductAmounts(productId, newQuantity);
            return productId;
        }
    }

    public void sendStoreOwnerRequest(String currentOwnerUsername, String newOwnerUsername, int storeId) {
        if(getIsOwner(currentOwnerUsername, storeId, newOwnerUsername)){
            throw new IllegalArgumentException(Error.makeStoreUserAlreadyOwnerError(newOwnerUsername, storeId));
        }
        if (!canAddOwnerToStore(storeId, currentOwnerUsername, newOwnerUsername))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddOwnerError(currentOwnerUsername, newOwnerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

            userFacade.addOwnerRequest(currentOwnerUsername, newOwnerUsername, storeId);
        }
    }

    public void sendStoreManagerRequest(String currentOwnerUsername, String newManagerUsername, int storeId) {
        if(getIsOwner(currentOwnerUsername, storeId, newManagerUsername)){
            throw new IllegalArgumentException(Error.makeStoreUserAlreadyOwnerError(newManagerUsername, storeId));
        }
        if(getIsManager(currentOwnerUsername, storeId, newManagerUsername)){
            throw new IllegalArgumentException(Error.makeStoreUserAlreadyManagerError(newManagerUsername, storeId));
        }
        if (!canAddManagerToStore(storeId, currentOwnerUsername, newManagerUsername))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddManagerError(currentOwnerUsername, newManagerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

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
        if(!getIsManager(currentOwnerUsername, storeId, newManagerUsername)){
            throw new IllegalArgumentException(Error.makeMemberUserHasNoRoleError());
        }
        if (!canAddPermissionToManager(storeId, currentOwnerUsername, newManagerUsername))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddManagerPermissionsError(currentOwnerUsername, newManagerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

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

    public void removeManagerPermission(String currentOwnerUsername, String newManagerUsername, int storeId, Permission permission) {
        if(!getIsManager(currentOwnerUsername, storeId, newManagerUsername)){
            throw new IllegalArgumentException(Error.makeMemberUserHasNoRoleError());
        }
        if (!canAddPermissionToManager(storeId, currentOwnerUsername, newManagerUsername))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddManagerPermissionsError(currentOwnerUsername, newManagerUsername, storeId));

        synchronized (storeRepository) {
            if (!storeRepository.storeExists(storeId))
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            if (!isStoreActive(storeId))
                throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));

            userFacade.removePremssionFromStore(currentOwnerUsername, newManagerUsername, storeId, permission);
        }
    }

    public boolean closeStore(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).getFounderUsername().equals(username))
            throw new IllegalArgumentException(Error.makeStoreUserCannotCloseStoreError(username, storeId));

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

    public boolean isStoreActive(int storeId) {
        return storeRepository.findStoreByID(storeId).getIsActive();
    }

    public List<MemberDTO> getOwners(String username, int storeId) {
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(Error.makeStoreUserCannotGetRolesInfoError(username,storeId));
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
            throw new IllegalArgumentException(Error.makeStoreUserCannotGetRolesInfoError(username, storeId));
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

    /*public List<MemberDTO> getSellers(String username, int storeId) {
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
    }*/

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
        if (!storeRepository.findStoreByID(storeId).isStoreOwner(username) && !userFacade.isSystemManager(username))
            throw new IllegalArgumentException(Error.makeStoreUserCannotStoreHistoryError(username, storeId));

        List<OrderDTO> orderDTOS = new LinkedList<>();
        for (int orderId : storeRepository.findStoreByID(storeId).getOrderIds()) {
            OrderDTO order = orderFacade.getOrderByOrderId(orderId).get(storeId);
            orderDTOS.add(order);
        }

        return orderDTOS;
    }

    public StoreDTO getStoreInfo(String username, int storeId) {
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            if (!isStoreActive(storeId)) {
                if (!store.isStoreOwner(username) && !userFacade.isSystemManager(username))
                    throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
            }

            return store.getStoreDTO();
        }
    }
  
  public synchronized ProductDTO getProductInfo(String username, int productId) {
        int storeId = getStoreOfProduct(productId);
        if(storeId < 0){
            throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));
        }
        if (!isStoreActive(storeId)) {
            if (!storeRepository.findStoreByID(storeId).isStoreOwner(username) && !userFacade.isSystemManager(username))
                throw new IllegalArgumentException(Error.makeStoreOfProductIsNotActiveError(productId));
        }

        return productFacade.getProductDTO(productId);
    }

    private int getStoreOfProduct(int produceId){
        for(int storeId : storeRepository.getAllStoreIds()){
            if(hasProductInStock(storeId, produceId, 0)){
                return storeId;
            }
        }
        return -1;
    }

    public Map<ProductDTO, Integer> getProductsInfoAndFilter(String username, int storeId, String productName, String category,
                                                             double price, double minProductRank) throws JsonProcessingException {
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            if (!isStoreActive(storeId)) {
                if (!store.isStoreOwner(username) && !store.isStoreManager(username)) {
                    throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
                }
            }

            List<Integer> storeProductIds = new ArrayList<>(store.getProductAmounts().keySet());
            List<ProductDTO> filteredProducts = productFacade.getFilteredProducts(storeProductIds, productName, price, category, minProductRank);
            Map<ProductDTO, Integer> res = new HashMap<>();
            for (ProductDTO product : filteredProducts)
                res.put(product, store.getProductAmounts().get(product.getProductID()));
            return res;
        }
    }

    public void setStoreBankAccount(String ownerUsername, int storeId, BankAccountDTO bankAccount) {
        Store store = storeRepository.findStoreByID(storeId);
        synchronized (store) {
            if (!store.isStoreOwner(ownerUsername))
                throw new IllegalArgumentException(Error.makeStoreUserCannotSetBankAccountError(ownerUsername, storeId));

            store.setBankAccount(bankAccount);
        }
    }

    public synchronized BankAccountDTO getStoreBankAccount(int storeId) {
        Store store = storeRepository.findStoreByID(storeId);
        return store.getBankAccount();
    }

    public int getProductAmount(int storeId, int productId) {
        Store store = storeRepository.findStoreByID(storeId);

        if (!isStoreActive(storeId))
            throw new IllegalArgumentException(Error.makeStoreWithIdNotActiveError(storeId));
        synchronized (store.getProductAmounts()) {
            if (!store.productExists(productId))
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));

            return store.getProductAmounts().get(productId);
        }
    }

    /*public String addSeller(int storeId, String adderUsername, String sellerUsername) {
        if (!canAddSellerToStore(storeId, adderUsername, sellerUsername))
            throw new IllegalArgumentException(
                    String.format("A user %s can not add sellers to store with id %d.", adderUsername, storeId));
        if (!userFacade.isExist(sellerUsername))
            throw new IllegalArgumentException(String.format("A user %s does not exist.", sellerUsername));

        storeRepository.findStoreByID(storeId).addSeller(sellerUsername);
        return sellerUsername;
    }*/

<<<<<<< HEAD
=======
    public void addBuyPolicy(String username, int storeId, String args) {
        if (!hasPermission(username, storeId, Permission.ADD_BUY_POLICY))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddBuyPolicyError(username, storeId));

        buyPolicyFacade.addBuyPolicy(storeId, args);
    }

>>>>>>> master
    public void addDiscountPolicy(String username, int storeId, String args) {
        if (!hasPermission(username, storeId, Permission.ADD_DISCOUNT_POLICY))
            throw new IllegalArgumentException(Error.makeStoreUserCannotAddDiscountPolicyError(username, storeId));

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

    public void checkCart(String username, List<CartItemDTO> cart) throws Exception {
        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        String error = "";
        for (int storeId : cartByStore.keySet()) {
            Store store = storeRepository.findStoreByID(storeId);
            synchronized (store.getProductAmounts()) {
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
            throw new Exception(error);
        }

    }

    public boolean getIsOwner(String actorUsername, int storeId, String infoUsername){
        //if (!hasPermission(actorUsername, storeId, Permission.VIEW_ROLES) && !storeRepository.findStoreByID(storeId).isStoreOwner(actorUsername))
        //    throw new IllegalArgumentException(Error.makeStoreUserCannotGetRolesInfoError(actorUsername, storeId));
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        Store store = storeRepository.findStoreByID(storeId);
        return store.isStoreOwner(infoUsername);
    }

    public boolean hasProductInStock(int storeId, int productId, int amount){
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        Store store = storeRepository.findStoreByID(storeId);
        return store.hasProductInAmount(productId, amount);
    }

    public boolean getIsManager(String actorUsername, int storeId, String infoUsername){
        if (!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        Store store = storeRepository.findStoreByID(storeId);
        return store.isStoreManager(infoUsername);
    }

    public synchronized void buyCart(String username, List<CartItemDTO> cart) throws Exception {
        checkCart(username, cart);

        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        for (int storeId : cartByStore.keySet()) {
            Store store = storeRepository.findStoreByID(storeId);
            store.updateStock(cartByStore.get(storeId));
        }
    }

    // return a map from store id to a List that coontain object thats stores : id,
    // amount, original price and new price
    public Map<Integer, List<ProductDataPrice>> calculatePrice(String username, List<CartItemDTO> cart) {
        Map<Integer, List<ProductDataPrice>> mapPrice = new HashMap<>();
        Map<Integer, List<CartItemDTO>> cartByStore = getCartByStore(cart);
        for (int storeId : cartByStore.keySet()) {
            mapPrice.put(storeId, discountPolicyFacade.calculatePrice(storeId, cartByStore.get(storeId)));
        }
        return mapPrice;
    }

    public boolean hasPermission(String username, int storeId, Permission permission) {
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
        return (hasPermission(currOwnerUsername, storeId, Permission.ADD_MANAGER) ||
                storeRepository.findStoreByID(storeId).isStoreOwner(currOwnerUsername)) &&
                userFacade.isExist(newManagerUsername) &&
                (storeRepository.findStoreByID(storeId).isStoreManager(newManagerUsername));
    }


    /*private boolean canAddSellerToStore(int storeId, String currOwnerUsername, String newSellerUsername) {
        return hasPermission(currOwnerUsername, storeId, Permission.ADD_SELLER) &&
                userFacade.isExist(newSellerUsername) &&
                (!storeRepository.findStoreByID(storeId).isSeller(newSellerUsername));
    }*/

    public StoreInfo getStoreInfo(int storeId) {
        return storeRepository.findStoreByID(storeId).getStoreInfo();
    }

    public Set<Integer> getAllStoreIds() {
        return storeRepository.getAllStoreIds();
    }
}
