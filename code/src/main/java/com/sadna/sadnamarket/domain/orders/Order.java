package com.sadna.sadnamarket.domain.orders;

import java.util.Date;
import java.util.Map;

public class Order {
    // fields needed for order history
    private int orderId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;
    //other fields
}
