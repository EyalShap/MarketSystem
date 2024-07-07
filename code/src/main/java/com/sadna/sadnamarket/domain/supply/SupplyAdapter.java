package com.sadna.sadnamarket.domain.supply;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SupplyAdapter implements SupplyInterface {

    private ExternalSupplyAPI service;

    public SupplyAdapter(){
        service = new ExternalSupplyAPI();
    }

    public boolean implemented(){
        return true;
    }

    @Override
    public boolean canMakeOrder(OrderDetailsDTO orderDetails, AddressDTO address) throws JsonProcessingException {
        String resp = service.handshake();
        return resp.equals("OK");
    }

    @Override
    public String makeOrder(OrderDetailsDTO orderDetails, AddressDTO address) throws JsonProcessingException {
        String resp = service.supply(
                address.getOrdererName(),
                address.getAddressLine1(),
                address.getCity(),
                address.getCountry(),
                address.getZipCode());
        if(resp.equals("-1")){
            throw new UnsupportedOperationException("Supply transaction failed");
        }
        return resp;
    }

    @Override
    public boolean cancelOrder(String orderCode) throws JsonProcessingException {
        String resp = service.cancelSupply(orderCode);
        return resp.equals("1");
    }
}
