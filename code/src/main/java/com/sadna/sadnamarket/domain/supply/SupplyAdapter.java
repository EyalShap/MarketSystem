package com.sadna.sadnamarket.domain.supply;

public class SupplyAdapter implements SupplyInterface {

    public boolean implemented(){
        return false;
    }

    @Override
    public boolean canMakeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        return false;
    }

    @Override
    public String makeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        return null;
    }

    @Override
    public boolean cancelOrder(String orderCode) {
        return false;
    }
}
