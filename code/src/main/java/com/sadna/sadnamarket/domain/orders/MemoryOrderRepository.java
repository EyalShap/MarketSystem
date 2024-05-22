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

    private Order DTOToOrder( OrderDTO ordersDTO) {
        //int orderId = ordersDTO.getOrderId();
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
      //  int userId = order.getOrderId();
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

    public void resetOrders(){
        orders = new HashMap<>();
    }

    public static void main(String[] args) {
        MemoryOrderRepository memoryOrderRepository = new MemoryOrderRepository();
        HashMap<Integer,Integer> productAmounts1=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons1=new HashMap<>();
        productAmounts1.put(1,5);
        orderProductsJsons1.put(1,"banana");
        OrderDTO orderDTO1 = new OrderDTO("matan","store1",productAmounts1,orderProductsJsons1);
        HashMap<Integer,OrderDTO> orders1 = new HashMap<>();
        orders1.put(1,orderDTO1);
        memoryOrderRepository.createOrder(orders1);

        HashMap<Integer,Integer> productAmounts2=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons2=new HashMap<>();
        productAmounts2.put(4,5);
        orderProductsJsons2.put(4,"orange");
        OrderDTO orderDTO2 = new OrderDTO("noam","store2",productAmounts2,orderProductsJsons2);
        HashMap<Integer,OrderDTO> orders2=new HashMap<>();
        orders2.put(1,orderDTO2);
        memoryOrderRepository.createOrder(orders2);

        HashMap<Integer,Integer> productAmounts3=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons3=new HashMap<>();
        productAmounts3.put(7,10);
        orderProductsJsons3.put(7,"tomato");
        productAmounts3.put(9,5);
        orderProductsJsons3.put(9,"meat");
        OrderDTO orderDTO3 = new OrderDTO("matan","store3",productAmounts3,orderProductsJsons3);
        HashMap<Integer,OrderDTO> orders3=new HashMap<>();
        orders3.put(1,orderDTO3);
        memoryOrderRepository.createOrder(orders3);

        Map<Integer,Map<Integer,OrderDTO>>a =memoryOrderRepository.getOrdersByMember("matan");
        Map<Integer,OrderDTO>b=memoryOrderRepository.getOrderByOrderId(2);
    }

}
