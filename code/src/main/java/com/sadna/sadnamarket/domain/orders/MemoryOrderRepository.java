package com.sadna.sadnamarket.domain.orders;

import com.sadna.sadnamarket.domain.products.MemoryProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MemoryOrderRepository implements IOrderRepository {
    private Map<Integer, Map<Integer, Order>> orders;
    private int nextOrderId;
    private static final Logger logger = LogManager.getLogger(MemoryOrderRepository.class);
    public MemoryOrderRepository() {
        orders = new HashMap<>();
        this.nextOrderId = 0;
    }
    @Override
    public synchronized int createOrder(Map<Integer, OrderDTO> storeOrdersDTO){
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

    private Order DTOToOrder( OrderDTO ordersDTO) {
        String memberName=ordersDTO.getMemberName();
        String storeNameWhenOrdered = ordersDTO.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(ordersDTO.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(ordersDTO.getOrderProductsJsons());
        Order order = new Order(memberName,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
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
        String memberName=order.getMemberName();
        String storeNameWhenOrdered = order.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(order.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(order.getOrderProductsJsons());
        OrderDTO orderDTO = new OrderDTO(memberName,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return orderDTO;
    }

    public Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember) {
        if(nameMember==null){
            throw new IllegalArgumentException("The name is null.");
        }
        if(nameMember.isEmpty()){
            throw new IllegalArgumentException("The name is empty.");
        }
        Map<Integer,Map<Integer,OrderDTO>> ordersByMember = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Order>> outerEntry : orders.entrySet()) {
            Integer outerKey = outerEntry.getKey();
            Map<Integer, Order> innerMap = outerEntry.getValue();
            Map<Integer, OrderDTO> storeOrderDTO = new HashMap<>();

            for (Map.Entry<Integer, Order> innerEntry : innerMap.entrySet()) {
                Order order = innerEntry.getValue();
                if (order.getMemberName().equals(nameMember)) {
                    OrderDTO orderDTO= orderToDTO(order);
                    storeOrderDTO.put(innerEntry.getKey(),orderDTO);
                }
            }
            if (!storeOrderDTO.isEmpty()) {
                ordersByMember.put(outerKey, storeOrderDTO);
            }
        }
        if(ordersByMember.isEmpty()){
            throw new IllegalArgumentException("There are no orders for "+nameMember+".");
        }
        return ordersByMember;
    }

    //TODO
    public Map<Integer,OrderDTO> getOrderByOrderId(int orderId) {
        Map<Integer,OrderDTO> orderDTOByOrderId = new HashMap<>();
        if(orders.containsKey(orderId)){
            Map<Integer,Order> orderByOrderId = orders.get(orderId);
            for (Integer storeId : orderByOrderId.keySet()) {
                OrderDTO orderDTO=orderToDTO(orderByOrderId.get(storeId));
                orderDTOByOrderId.put(storeId,orderDTO);
            }
        }
        else {
            throw new IllegalArgumentException("The order: "+ orderId +" does not exist.");
        }
        return orderDTOByOrderId;
    }

    public List<OrderDTO> getAllOrders(){
        List<OrderDTO> allOrders = new LinkedList<>();
        for (Map.Entry<Integer, Map<Integer, Order>> outerEntry : orders.entrySet()) {
            Map<Integer, Order> innerMap = outerEntry.getValue();
            for (Map.Entry<Integer, Order> innerEntry : innerMap.entrySet()) {
                allOrders.add(orderToDTO(innerEntry.getValue()));
            }
        }
        if(allOrders.isEmpty()){
            throw new IllegalArgumentException("There are no orders.");
        }
        return allOrders;
    }

    public void resetOrders(){
        orders = new HashMap<>();
    }


}
