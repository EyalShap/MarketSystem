package com.sadna.sadnamarket.domain.orders;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;

import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    int createOrder(Map<Integer, OrderDTO> storeOrdersDTO);
    List<ProductDataPrice> getOrders(int storeId);

    Map<Integer,List<ProductDataPrice>> getProductDataPriceByMember(String nameMember);
    Map<Integer,OrderDTO> getOrderByOrderId(int orderId);
    List<OrderDTO> getAllOrders();
}
