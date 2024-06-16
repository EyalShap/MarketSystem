package com.sadna.sadnamarket.domain.orders;

import java.util.*;

public class OrderDTO {
    private String memberName;
    //private int orderId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;

    public OrderDTO(String memberName,String storeNameWhenOrdered,Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        this.memberName=memberName;
        this.storeNameWhenOrdered=storeNameWhenOrdered;
        this.productAmounts=productAmounts;
        this.orderProductsJsons=orderProductsJsons;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getStoreNameWhenOrdered() {
        return storeNameWhenOrdered;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public Map<Integer, String> getOrderProductsJsons() {
        return orderProductsJsons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        boolean x1 = Objects.equals(memberName, orderDTO.memberName);
        boolean x2 = Objects.equals(storeNameWhenOrdered, orderDTO.storeNameWhenOrdered);
        boolean x3 = Objects.equals(productAmounts, orderDTO.productAmounts);
        boolean x4 = Objects.equals(orderProductsJsons, orderDTO.orderProductsJsons);
        return x1 && x2 && x3 && x4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberName, storeNameWhenOrdered, productAmounts, orderProductsJsons);
    }
}
