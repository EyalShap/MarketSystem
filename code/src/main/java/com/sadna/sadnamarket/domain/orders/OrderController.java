package com.sadna.sadnamarket.domain.orders;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderController {
    private Map<Integer, Map<Integer, Order>> orders;
    private static OrderController instance;
    private int nextOrderId;
    private OrderController() {
        orders = new HashMap<>();
        this.nextOrderId = 0;
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }

    public int createOrder(){
        nextOrderId++;
        return nextOrderId-1;
    }
    public int addStoreToOrder(int orderId,int userID,int storeId,String storeName, Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        Order order = new Order(userID,storeName,productAmounts,orderProductsJsons);
        if(orders.get(orderId) == null){
            Map<Integer,Order> storeOrder = new HashMap<>();
            storeOrder.put(storeId,order);
            orders.put(orderId,storeOrder);
        }
        else{
            orders.get(orderId).put(storeId,order);
        }
        return orderId;
    }

    public int removeStoreFromOrder(int orderId,int storeId){
        orders.get(orderId).remove(storeId);
        return orderId;
    }
    public int deleteOrder(int orderId){
        orders.remove(orderId);
        return orderId;
    }


    public List<OrderDTO> getOrders(int storeId) {
        LinkedList<OrderDTO> ordersStore = new LinkedList<OrderDTO>();
        for (int orderId : orders.keySet()) {
            if(orders.get(orderId).containsKey(storeId)){
                Order order = orders.get(orderId).get(storeId);
                OrderDTO orderDTO = orderToDTO(order);
                ordersStore.add(orderDTO);
            }
        }
        return ordersStore;
    }

    private OrderDTO orderToDTO(Order order){
        int userId = order.getUserId();
        String storeNameWhenOrdered = order.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(order.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(order.getOrderProductsJsons());
        OrderDTO orderDTO = new OrderDTO(userId,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return orderDTO;
    }

    public static void main(String[] args) {
        OrderController a=OrderController.getInstance();
       int orderId=1;
       int userID=2;
       int storeId=3;
       String storeName="sI";
       Map<Integer, Integer> productAmounts=new HashMap<>();
       productAmounts.put(1,1);
       Map<Integer, String> orderProductsJsons =new HashMap<>();
       orderProductsJsons.put(1,"as");
       a.addStoreToOrder(1,2,3,"as",productAmounts,orderProductsJsons);
       a.addStoreToOrder(1,2,4,"as",productAmounts,orderProductsJsons);
       a.removeStoreFromOrder(1,4);
       a.deleteOrder(1);

    }

}
