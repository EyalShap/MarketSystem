package com.sadna.sadnamarket.domain.orders;

import java.util.ArrayList;
import java.util.List;

public class OrderFacade {
    public List<OrderDTO> getOrders(int storeId) {
        // dana added this proxy function for the get store order history use case
        return new ArrayList<>();
    }

    public OrderDTO getOrder(int orderId) {
        // dana added this proxy function for the get store order history use case
        return new OrderDTO();
    }
}
