package com.sadna.sadnamarket.domain.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        int orderId = orderRepository.createOrder(ordersStore);
        for (Map.Entry<Integer, List<ProductDataPrice>> bag : storeBag.entrySet()) {
            storeFacade.addOrderId(bag.getKey(), orderId);
        }
        return orderId;
    }

    private OrderDTO productDataPriceToOrderDTO(List<ProductDataPrice> storeBag,String storeName,String memberName) {
        Map<Integer, Integer> productAmounts=new HashMap<>();
        Map<Integer, String> orderProductsJsons= new HashMap<>();
        for (ProductDataPrice product:storeBag) {
            productAmounts.put(product.getId(),product.getAmount());
            orderProductsJsons.put(product.getId(),toJson(product));
        }
        return new OrderDTO(memberName,storeName,productAmounts,orderProductsJsons);
    }

    private String toJson(ProductDataPrice productDataPrice) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(productDataPrice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductDataPrice> getOrders(int storeId) {
        return orderRepository.getOrders(storeId);
    }


    ///need to change
//    public Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember) {
//        return orderRepository.getOrdersByMember(nameMember);
//    }

    public Map<Integer,List<ProductDataPrice>> getProductDataPriceByMember(String nameMember){
        return orderRepository.getProductDataPriceByMember(nameMember);
    }
    public Map<Integer,OrderDTO> getOrderByOrderId(int orderId) {
        return orderRepository.getOrderByOrderId(orderId);
    }

    public List<OrderDTO> getAllOrders(){
        return orderRepository.getAllOrders();
    }

//    public static void main(String[] args) {
//        ProductDataPrice product = new ProductDataPrice(1, 101, "Example Product", 50, 19.99, 17.99);
//        String jsonString = "";
//
//        // Convert object to JSON string
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            jsonString = objectMapper.writeValueAsString(product);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("JSON String: " + jsonString);
//
//        // Convert JSON string back to object
//        ProductDataPrice product2 = null;
//        ObjectMapper objectMapper2 = new ObjectMapper();
//        try {
//            product2 = objectMapper2.readValue(jsonString, ProductDataPrice.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        // Check and print the deserialized object
//        if (product2 != null) {
//            int f=1;
//        } else {
//            System.out.println("Failed to deserialize JSON string.");
//        }
//
//    }


}
