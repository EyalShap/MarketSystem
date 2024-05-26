package com.sadna.sadnamarket.domain.orders;

import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    int createOrder(Map<Integer, OrderDTO> storeOrdersDTO);
    List<OrderDTO> getOrders(int storeId);

    Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember);
    Map<Integer,OrderDTO> getOrderByOrderId(int orderId);
    List<OrderDTO> getAllOrders();
}
