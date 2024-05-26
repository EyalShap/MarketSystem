package com.sadna.sadnamarket.domain.orders;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.stores.StoreFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFacade {
    private IOrderRepository orderRepository;
    private StoreFacade storeFacade;

    public OrderFacade(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public void setStoreFacade(StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public int createOrder(Map<Integer, List<ProductDataPrice>> storeBag,String memberName){
        Map<Integer, OrderDTO>ordersStore = new HashMap<>();
        for (Map.Entry<Integer, List<ProductDataPrice>> bag : storeBag.entrySet()) {
            String storeName = storeFacade.getStoreInfo(bag.getKey()).getStoreName();
            OrderDTO orderDTO=productDataPriceToOrderDTO(bag.getValue(),storeName,memberName);
            ordersStore.put(bag.getKey(),orderDTO);
        }
        return orderRepository.createOrder(ordersStore);
    }

    private OrderDTO productDataPriceToOrderDTO(List<ProductDataPrice> storeBag,String storeName,String memberName) {
        Map<Integer, Integer> productAmounts=new HashMap<>();
        Map<Integer, String> orderProductsJsons= new HashMap<>();
        for (ProductDataPrice product:storeBag) {
            productAmounts.put(product.getId(),product.getAmount());
            orderProductsJsons.put(product.getId(),"old price: " + product.getOldPrice()+" ,new price: "+product.getNewPrice());
        }
        return new OrderDTO(memberName,storeName,productAmounts,orderProductsJsons);
    }


    public List<OrderDTO> getOrders(int storeId) {
        return orderRepository.getOrders(storeId);
    }
    public Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember) {
        return orderRepository.getOrdersByMember(nameMember);
    }
    public Map<Integer,OrderDTO> getOrderByOrderId(int orderId) {
        return orderRepository.getOrderByOrderId(orderId);
    }

    public List<OrderDTO> getAllOrders(){
        return orderRepository.getAllOrders();
    }



}
