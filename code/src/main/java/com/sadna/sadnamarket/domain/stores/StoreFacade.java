package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // returns id of newly created store
    public int createStore(int founderId, String storeName) {
        if(!userFacade.canCreateStore(founderId))
            throw new IllegalArgumentException(String.format("User with id %d can not create a new store.", founderId));

        return storeRepository.addStore(founderId, storeName);
    }
    
    public int addProductToStore(int userId, int storeId, String productName, int productQuantity, int productPrice) {
        if(!userFacade.canAddProductsToStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not add a product to store with id %d.", userId, storeId));
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        int newProductId = productFacade.addProduct(storeId, productName, productQuantity, productPrice);
        storeRepository.findStoreByID(storeId).addProduct(newProductId, productQuantity);
        return newProductId;
    }

    public int deleteProduct(int userId, int storeId, int productId) {
        if(!userFacade.canDeleteProductsFromStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not delete a product from store with id %d.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        storeRepository.findStoreByID(storeId).deleteProduct(productId);
        productFacade.removeProduct(storeId, productId);
        return productId;
    }

    public int updateProduct(int userId, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        if(!userFacade.canUpdateProductsInStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not update a product in store with id %d.", userId, storeId));
        if(!storeRepository.findStoreByID(storeId).productExists(productId))
            throw new IllegalArgumentException(String.format("A store with id %d does not have a product with id %d.", storeId, productId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        storeRepository.findStoreByID(storeId).setProductAmounts(productId, newQuantity);
        productFacade.updateProduct(storeId, productId, newProductName, newQuantity, newPrice);
        return productId;
    }

    public void sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d.", currentOwnerId, storeId));
        if(storeRepository.findStoreByID(storeId).isStoreOwner(newOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already an owner of store %d.", newOwnerId, storeId));

        userFacade.sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
    }

    public void sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreManager(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d.", currentOwnerId, storeId));
        if(storeRepository.findStoreByID(storeId).isStoreManager(newManagerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already a manager of store %d.", newManagerId, storeId));

        userFacade.sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, permissions);
    }

    public void addStoreOwner(int newOwnerId, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreOwner(newOwnerId);
    }

    public void addStoreManager(int newManagerId, int storeId) {
        storeRepository.findStoreByID(storeId).addStoreManager(newManagerId);
    }

    public boolean closeStore(int userId, int storeId) {
        if(storeRepository.findStoreByID(storeId).getFounderId() != userId)
            throw new IllegalArgumentException(String.format("A user with id %d can not close the store with id %d (not a founder).", userId, storeId));

        storeRepository.findStoreByID(storeId).closeStore();

        String msg = String.format("The store \"%s\" was closed.", storeRepository.findStoreByID(storeId).getStoreName());
        List<Integer> ownerIds = storeRepository.findStoreByID(storeId).getOwnerIds();
        List<Integer> managerIds = storeRepository.findStoreByID(storeId).getManagerIds();
        for(int ownerId : ownerIds) {
            userFacade.notify(ownerId, msg);
        }
        for(int managerId : managerIds) {
            userFacade.notify(managerId, msg);
        }

        return true;
    }

    private boolean isStoreActive(int storeId) {
        return storeRepository.findStoreByID(storeId).getIsActive();
    }

    public List<UserDTO> getOwners(int userId, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> ownerIds = storeRepository.findStoreByID(storeId).getOwnerIds();
        List<UserDTO> owners = new ArrayList<>();
        for(int ownerId : ownerIds) {
            owners.add(userFacade.getUser(ownerId));
        }
        return owners;
    }

    public List<UserDTO> getManagers(int userId, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> managerIds = storeRepository.findStoreByID(storeId).getManagerIds();
        List<UserDTO> managers = new ArrayList<>();
        for(int managerId : managerIds) {
            managers.add(userFacade.getUser(managerId));
        }
        return managers;
    }

    public List<UserDTO> getSellers(int userId, int storeId) {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> sellerIds = storeRepository.findStoreByID(storeId).getSellerIds();
        List<UserDTO> sellers = new ArrayList<>();
        for(int sellerId : sellerIds) {
            sellers.add(userFacade.getUser(sellerId));
        }
        return sellers;
    }

    /*public String getStoreOrderHisotry(int userId, int storeId) throws JsonProcessingException {
        if(!storeRepository.storeExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request order history.", userId, storeId));

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

    public String getStoreOrderHisotry(int userId, int storeId) throws JsonProcessingException {
        if(!storeRepository.findStoreByID(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request order history.", userId, storeId));

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

    public StoreDTO getStoreInfo(int storeId) {
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        return storeRepository.findStoreByID(storeId).getStoreDTO();
    }

    public Map<String, Integer> getProductsInfo(int storeId) throws JsonProcessingException {
        if(!isStoreActive(storeId))
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
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        return storeRepository.findStoreByID(storeId).buyStoreProduct(productId, amount);
    }
}
