package com.sadna.sadnamarket.domain.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFacade {
    private IOrderRepository orderRepository;
    public OrderFacade(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public int createOrder(Map<Integer, OrderDTO> storeOrdersDTO){
        return orderRepository.createOrder(storeOrdersDTO);
    }
    public List<OrderDTO> getOrders(int storeId) {
        return orderRepository.getOrders(storeId);
    }
    public Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember) {
      //  return orderRepository.getOrders(storeId);
        return new HashMap<>();
    }

    public Map<Integer,OrderDTO> getOrderByOrderId(int orderId) {
        //  return orderRepository.getOrders(storeId);
        return new HashMap<>();
    }

}
