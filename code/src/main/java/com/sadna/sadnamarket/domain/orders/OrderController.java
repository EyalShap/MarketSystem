package com.sadna.sadnamarket.domain.orders;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderController {
    private Map<Integer, Map<Integer, Order>> orders; // map of <orderId, storeId> - Order
    // (each order is splitted to the order from each store)
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
        int orderId = order.getOrderId();
        String storeNameWhenOrdered = order.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(order.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(order.getOrderProductsJsons());
        OrderDTO orderDTO = new OrderDTO(orderId,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return orderDTO;
    }

}
