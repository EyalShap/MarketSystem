package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.UserDTO;
import com.sadna.sadnamarket.domain.users.UserFacade;

import java.util.*;

public class StoreFacade {
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private OrderFacade orderFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private IStoreRepository storeRepository;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public StoreFacade(IStoreRepository storeRepository) {
        //this.userFacade = userFacade;
        //this.productFacade = productFacade;
        //this.orderFacade = orderFacade;
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

    // returns id of newly created store
    public int createStore(String founderUserName, String storeName) {
        if(!canCreateStore(founderUserName))
            throw new IllegalArgumentException(String.format("User %s can not create a new store.", founderUserName));

        return storeRepository.addStore(founderUserName, storeName);
    }
    
    public int addProductToStore( String username, int storeId, String productName, int productQuantity, int productPrice) {
        if(!canAddProductsToStore(username, storeId))
            throw new IllegalArgumentException(String.format("user %s can not add a product to store with id %d.", username, storeId));
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        int newProductId = productFacade.addProduct(storeId, productName, productQuantity, productPrice);
        storeRepository.findStoreByID(storeId).addProduct(newProductId, productQuantity);
        return newProductId;
    }

    public int deleteProduct( String username, int storeId, int productId) {
        if(!canDeleteProductsFromStore(username, storeId))
            throw new IllegalArgumentException(String.format("user %s can not delete a product from store with id %d.", username, storeId));

        storeRepository.findStoreByID(storeId).deleteProduct(productId);
        productFacade.removeProduct(storeId, productId);
        return productId;
    }

    public int updateProduct( String username, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        if(!canUpdateProductsInStore(username, storeId))
            throw new IllegalArgumentException(String.format("user %s can not update a product in store with id %d.", username, storeId));
        if(!storeRepository.findStoreByID(storeId).productExists(productId))
            throw new IllegalArgumentException(String.format("A store with id %d does not have a product with id %d.", storeId, productId));

        storeRepository.findStoreByID(storeId).setProductAmounts(productId, newQuantity);
        productFacade.updateProduct(storeId, productId, newProductName, newQuantity, newPrice);
        return productId;
    }

    public void sendStoreOwnerRequest(String currentOwnerUsername, String newOwnerUsername, int storeId) {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(currentOwnerUsername))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d.", currentOwnerUsername, storeId));
        if(storeRepository.findStoreByID(storeId).isStoreOwner(newOwnerUsername))
            throw new IllegalArgumentException(String.format("A user %s is already an owner of store %d.", newOwnerUsername, storeId));
        if(!userFacade.isExist(newOwnerUsername))
            throw new IllegalArgumentException(String.format("A user %s does not exist.", newOwnerUsername));

        userFacade.sendStoreOwnerRequest(currentOwnerUsername, newOwnerUsername, storeId);
    }

    public void sendStoreManagerRequest(String currentOwnerUsername, String newManagerUsername, int storeId, Set<Integer> permissions) {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreManager(currentOwnerUsername))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d.", currentOwnerUsername, storeId));
        if(storeRepository.findStoreByID(storeId).isStoreManager(newManagerUsername))
            throw new IllegalArgumentException(String.format("A user %s is already a manager of store %d.", newManagerUsername, storeId));
        if(!userFacade.isExist(newManagerUsername))
            throw new IllegalArgumentException(String.format("A user %s does not exist.", newManagerUsername));

        userFacade.sendStoreManagerRequest(currentOwnerUsername, newManagerUsername, storeId, permissions);
    }

    public void addStoreOwner(String newOwnerUsername, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreOwner(newOwnerUsername);
    }

    public void addStoreManager(String newManagerUsername, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreManager(newManagerUsername);
    }

    public boolean closeStore( String username, int storeId) {
        if(!storeRepository.findStoreByID(storeId).getFounderUsername().equals(username))
            throw new IllegalArgumentException(String.format("A user %s can not close the store with id %d (not a founder).", username, storeId));

        storeRepository.findStoreByID(storeId).closeStore();

        String msg = String.format("The store \"%s\" was closed.", storeRepository.findStoreByID(storeId).getStoreName());
        List<String> ownerUsernames = storeRepository.findStoreByID(storeId).getOwnerUsernames();
        List<String> managerUsernames = storeRepository.findStoreByID(storeId).getManagerUsernames();
        for(String ownerUsername : ownerUsernames) {
            userFacade.notify(ownerUsername, msg);
        }
        for(String managerUsername : managerUsernames) {
            userFacade.notify(managerUsername, msg);
        }

        return true;
    }

    private boolean isStoreActive(int storeId) {
        return storeRepository.findStoreByID(storeId).getIsActive();
    }

    public List<UserDTO> getOwners(String username, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        //if(!isStoreActive(storeId))
        //    throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<String> ownerUsernames = storeRepository.findStoreByID(storeId).getOwnerUsernames();
        List<UserDTO> owners = new ArrayList<>();
        for(String ownerUsername : ownerUsernames) {
            owners.add(userFacade.getUser(ownerUsername));
        }
        return owners;
    }

    public List<UserDTO> getManagers(String username, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        //if(!isStoreActive(storeId))
        //    throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<String> managerUsernames = storeRepository.findStoreByID(storeId).getManagerUsernames();
        List<UserDTO> managers = new ArrayList<>();
        for(String managerUsername : managerUsernames) {
            managers.add(userFacade.getUser(managerUsername));
        }
        return managers;
    }

    public List<UserDTO> getSellers(String username, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d and can not request roles information.", username, storeId));
        //if(!isStoreActive(storeId))
        //    throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<String> sellerUsernames = storeRepository.findStoreByID(storeId).getSellerUsernames();
        List<UserDTO> sellers = new ArrayList<>();
        for(String sellerUsername : sellerUsernames) {
            sellers.add(userFacade.getUser(sellerUsername));
        }
        return sellers;
    }

    /*public String getStoreOrderHisotry( String username, int storeId) throws JsonProcessingException {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d and can not request order history.", username, storeId));

        List<OrderDTO> orders = OrderController.getInstance().getOrders(storeId);
        String orderHistory = String.format("Order History of store %d:\n", storeId);

        int orderIndex = 1;

        for(OrderDTO order : orders) {
            orderHistory += "------------------------------------------------------------\n";
            orderHistory += getOrderInfo(order, orderIndex);
            orderHistory += getProductsInfo(order);
            orderHistory += "------------------------------------------------------------\n\n";
            orderIndex++;
        }

        if(orderIndex == 1) {
            orderHistory += "There are no orders.\n";
        }

        return orderHistory;
    }*/

    public String getStoreOrderHisotry( String username, int storeId) throws JsonProcessingException {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(username))
            throw new IllegalArgumentException(String.format("A user %s is not an owner of store %d and can not request order history.", username, storeId));

        List<OrderDTO> orders = new ArrayList<>();
        for(int orderId : storeRepository.findStoreByID(storeId).getOrderIds()) {
            orders.add(orderFacade.getOrder(orderId));
        }
        String orderHistory = String.format("Order History of store %d:\n", storeId);

        int orderIndex = 1;

        for(OrderDTO order : orders) {
            orderHistory += "------------------------------------------------------------\n";
            orderHistory += getOrderInfo(order, orderIndex);
            orderHistory += getProductsInfo(order);
            orderHistory += "------------------------------------------------------------\n\n";
            orderIndex++;
        }

        if(orderIndex == 1) {
            orderHistory += "There are no orders.\n";
        }

        return orderHistory;
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

        for(int productId : orderProductsIds) {
            ProductDTO productDTO = objectMapper.readValue(orderDTO.getProductDescription(productId), ProductDTO.class);
            res += String.format("%d.\n", productIndex);
            res += String.format("%d X %s\n", orderDTO.getProductAmount(productId), productDTO);
            res += String.format("Price: %d\n\n", productDTO.getProductPrice());
            productIndex++;
        }

        return res;
    }

    public StoreDTO getStoreInfo( String username, int storeId) {
        if(!isStoreActive(storeId) && !storeRepository.findStoreByID(storeId).isStoreOwner(username) && !storeRepository.findStoreByID(storeId).isStoreManager(username)) // users who are not owner of manager can't get info on a closed store
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        return storeRepository.findStoreByID(storeId).getStoreDTO();
    }

    public Map<String, Integer> getProductsInfo( String username, int storeId) throws JsonProcessingException {
        if(!isStoreActive(storeId) && !storeRepository.findStoreByID(storeId).isStoreOwner(username) && !storeRepository.findStoreByID(storeId).isStoreManager(username)) // users who are not owner of manager can't get info on a closed store
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));


        Map<Integer, Integer> productIdsAmounts = storeRepository.findStoreByID(storeId).getProductAmounts();
        Map<String, Integer> productDTOsAmounts = new HashMap<>();
        for(int productId : productIdsAmounts.keySet()) {
            int amount = productIdsAmounts.get(productId);
            productDTOsAmounts.put(objectMapper.writeValueAsString(productFacade.getProductDTO(productId)), amount);
        }
        return productDTOsAmounts;
    }

    public int buyStoreProduct(int storeId, int productId, int amount) {
        return storeRepository.findStoreByID(storeId).buyStoreProduct(productId, amount);
    }

    public String addSeller(int storeId, String adderUsername, String sellerUsername) {
        if(!canAddSellerToStore(storeId, adderUsername))
            throw new IllegalArgumentException(String.format("A user %s can not add sellers to store with id %d.", adderUsername, storeId));
        if(!userFacade.isExist(sellerUsername))
            throw new IllegalArgumentException(String.format("A user %s does not exist.", sellerUsername));

        storeRepository.findStoreByID(storeId).addSeller(sellerUsername);
        return sellerUsername;
    }

    public int addBuyPolicy( String username, int storeId) {
        if(!canAddBuyPolicy(storeId, username))
            throw new IllegalArgumentException(String.format("A user %s can not add buy policy to store with id %d.", username, storeId));

        int newPolicyId = buyPolicyFacade.addBuyPolicy();
        storeRepository.findStoreByID(storeId).addBuyPolicy(newPolicyId);
        return newPolicyId;
    }

    public int addDiscountPolicy( String username, int storeId) {
        if(!canAddDiscountPolicy(storeId, username))
            throw new IllegalArgumentException(String.format("A user %s can not add discount policy to store with id %d.", username, storeId));

        int newPolicyId = buyPolicyFacade.addBuyPolicy();
        storeRepository.findStoreByID(storeId).addDiscountPolicy(newPolicyId);
        return newPolicyId;
    }

    public int addOrderId(int storeId, int orderId) {
        storeRepository.findStoreByID(storeId).addOrderId(orderId);
        return orderId;
    }

    public boolean canCreateStore(String username) {
        // Dana added this proxy function for the store creation use case
        return true;
    }

    public boolean canAddProductsToStore(String username, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }

    public boolean canDeleteProductsFromStore(String username, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }

    public boolean canUpdateProductsInStore(String username, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }


    public UserDTO getUser(String username) {
        // Dana added this proxy function for the info request use case
        return new UserDTO();
    }



    public boolean canAddSellerToStore(int storeId, String username) {
        // Dana added this proxy function
        return true;
    }

    public boolean canAddBuyPolicy(int storeId, String username) {
        // Dana added this proxy function
        return true;
    }

    public boolean canAddDiscountPolicy(int storeId, String username) {
        // Dana added this proxy function
        return true;
    }

    public boolean isExist(String username) {
        // Dana added this proxy function
        return true;
    }

}
