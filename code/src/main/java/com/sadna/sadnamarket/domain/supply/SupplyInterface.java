package com.sadna.sadnamarket.domain.supply;

public interface SupplyInterface {
    public boolean canMakeOrder(OrderDetailsDTO orderDetails, AddressDTO address);
    public String makeOrder(OrderDetailsDTO orderDetails, AddressDTO address);
    public boolean cancelOrder(String orderCode);
}
