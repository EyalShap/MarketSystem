package com.sadna.sadnamarket.domain.orders;

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
}
