package com.sadna.sadnamarket.domain.orders;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MemoryOrderRepository implements IOrderRepository {
    private Map<Integer, Map<Integer, Order>> orders;
    private int nextOrderId;
    public MemoryOrderRepository() {
        orders = new HashMap<>();
        this.nextOrderId = 0;
    }
    @Override
    public int createOrder(Map<Integer, OrderDTO> storeOrdersDTO){
        if (storeOrdersDTO == null) {
            throw new IllegalArgumentException("The order is null.");
        }
        if (storeOrdersDTO.isEmpty()) {
            throw new IllegalArgumentException("The order is empty.");
        }
        for (Map.Entry<Integer, OrderDTO> entry : storeOrdersDTO.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("The store with ID: " + entry.getKey() + " has null order.");
            }
            if(entry.getValue().getProductAmounts().isEmpty()||entry.getValue().getOrderProductsJsons().isEmpty()){
                throw new IllegalArgumentException("The store with ID: " + entry.getKey() + " has no products.");
            }

        }
        Map<Integer, Order> storeOrders = new HashMap<>();
        for (Map.Entry<Integer, OrderDTO> entry : storeOrdersDTO.entrySet()) {
            Order order = DTOToOrder(entry.getValue());
            storeOrders.put(entry.getKey(),order);
        }
        orders.put(nextOrderId,storeOrders);
        nextOrderId++;
        return nextOrderId-1;
    }

    private Order DTOToOrder( OrderDTO OrdersDTO) {
        int orderId = OrdersDTO.getOrderId();
        String storeNameWhenOrdered = OrdersDTO.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(OrdersDTO.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(OrdersDTO.getOrderProductsJsons());
        Order order = new Order(orderId,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return order;
    }

    @Override
    public List<OrderDTO> getOrders(int storeId) {
        LinkedList<OrderDTO> ordersStore = new LinkedList<OrderDTO>();
        for (int orderId : orders.keySet()) {
            if(orders.get(orderId).containsKey(storeId)){
                Order order = orders.get(orderId).get(storeId);
                OrderDTO orderDTO = orderToDTO(order);
                ordersStore.add(orderDTO);
            }
        }
        if(ordersStore.isEmpty()){
            throw new IllegalArgumentException("The store with ID: " + storeId + " has no orders.");
        }
        return ordersStore;
    }

    private OrderDTO orderToDTO(Order order){
        int userId = order.getOrderId();
        String storeNameWhenOrdered = order.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(order.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(order.getOrderProductsJsons());
        OrderDTO orderDTO = new OrderDTO(userId,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return orderDTO;
    }

    public void resetOrders(){
        orders = new HashMap<>();
    }



}
