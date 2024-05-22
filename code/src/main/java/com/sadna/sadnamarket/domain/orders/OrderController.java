package com.sadna.sadnamarket.domain.orders;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    private static OrderController instance;
    private Map<Integer, Map<Integer, Order>> orders; // map of <orderId, storeId> - Order
    // (each order is splitted to the order from each store)

    private OrderController() {
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }




}
