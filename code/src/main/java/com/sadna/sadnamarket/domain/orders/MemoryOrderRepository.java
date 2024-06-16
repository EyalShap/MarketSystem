package com.sadna.sadnamarket.domain.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.MemoryProductRepository;
import com.sadna.sadnamarket.service.Error;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MemoryOrderRepository implements IOrderRepository {
    private Map<Integer, Map<Integer, Order>> orders;
    private int nextOrderId;
    private static final Logger logger = LogManager.getLogger(MemoryOrderRepository.class);
    public MemoryOrderRepository() {
        orders = new HashMap<>();
        this.nextOrderId = 0;
    }
    @Override
    public synchronized int createOrder(Map<Integer, OrderDTO> storeOrdersDTO){
        if (storeOrdersDTO == null) {
            throw new IllegalArgumentException(Error.makeOrderNullError());
        }
        if (storeOrdersDTO.isEmpty()) {
            throw new IllegalArgumentException(Error.makeOrderEmptyError());
        }
        for (Map.Entry<Integer, OrderDTO> entry : storeOrdersDTO.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException(Error.makeOrderStoreNullError(entry.getKey()));
            }
            if(entry.getValue().getProductAmounts().isEmpty()||entry.getValue().getOrderProductsJsons().isEmpty()){
                throw new IllegalArgumentException(Error.makeOrderStoreNoProductsError(entry.getKey()));
            }

        }
        Map<Integer, Order> storeOrders = new HashMap<>();
        for (Map.Entry<Integer, OrderDTO> entry : storeOrdersDTO.entrySet()) {
            Order order = DTOToOrder(entry.getValue());
            storeOrders.put(entry.getKey(),order);
        }
        orders.put(nextOrderId,storeOrders);
        nextOrderId++;
        return nextOrderId-1;
    }

    private Order DTOToOrder( OrderDTO ordersDTO) {
        String memberName=ordersDTO.getMemberName();
        String storeNameWhenOrdered = ordersDTO.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(ordersDTO.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(ordersDTO.getOrderProductsJsons());
        Order order = new Order(memberName,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return order;
    }

    @Override
    public List<ProductDataPrice> getOrders(int storeId) {
        List<ProductDataPrice> productDataPrices=new LinkedList<>();
        for (int orderId : orders.keySet()) {
            if(orders.get(orderId).containsKey(storeId)){
                Order order = orders.get(orderId).get(storeId);
                Map<Integer, String> orderProductsJsons=order.getOrderProductsJsons();
                for (String productsJsons: orderProductsJsons.values() ) {
                        productDataPrices.add(fromJson(productsJsons));
                }
            }
        }
        if(productDataPrices.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderStoreNoOrdersError(storeId));
        }
        return productDataPrices;
    }

    private OrderDTO orderToDTO(Order order){
        String memberName=order.getMemberName();
        String storeNameWhenOrdered = order.getStoreNameWhenOrdered();
        Map<Integer, Integer> copiedProductAmounts=new HashMap<>();
        copiedProductAmounts.putAll(order.getProductAmounts());
        Map<Integer, String> copiedProductsJsons = new HashMap<>();
        copiedProductsJsons.putAll(order.getOrderProductsJsons());
        OrderDTO orderDTO = new OrderDTO(memberName,storeNameWhenOrdered,copiedProductAmounts,copiedProductsJsons);
        return orderDTO;
    }

//    public Map<Integer,Map<Integer,OrderDTO>> getOrdersByMember(String nameMember) {
//        if(nameMember==null){
//            throw new IllegalArgumentException(Error.makeOrderNameNullError());
//        }
//        if(nameMember.isEmpty()){
//            throw new IllegalArgumentException(Error.makeOrderNameEmptyError());
//        }
//        Map<Integer,List<ProductDataPrice>> productDataPrices=new HashMap<>();
//        Map<Integer,Map<Integer,OrderDTO>> ordersByMember = new HashMap<>();
//        for (Map.Entry<Integer, Map<Integer, Order>> outerEntry : orders.entrySet()) {
//            Integer outerKey = outerEntry.getKey();
//            Map<Integer, Order> innerMap = outerEntry.getValue();
//            Map<Integer, OrderDTO> storeOrderDTO = new HashMap<>();
//            //test
//            List<ProductDataPrice> test=new LinkedList<>();
//            //test
//            for (Map.Entry<Integer, Order> innerEntry : innerMap.entrySet()) {
//                Order order = innerEntry.getValue();
//                if (order.getMemberName().equals(nameMember)) {
//                    Map<Integer, String> orderProductsJsons = order.getOrderProductsJsons();
//                    for (String productsJsons: orderProductsJsons.values() ) {
//                        test.add(fromJson(productsJsons));
//                    }
//                    OrderDTO orderDTO= orderToDTO(order);
//                    storeOrderDTO.put(innerEntry.getKey(),orderDTO);
//                }
//            }
//            if (!storeOrderDTO.isEmpty()) {
//                ordersByMember.put(outerKey, storeOrderDTO);
//                productDataPrices.put(outerKey,test);
//            }
//        }
//        if(ordersByMember.isEmpty()){
//            throw new IllegalArgumentException(Error.makeOrderNoOrdersForUserError(nameMember));
//        }
//        return ordersByMember;
//    }


    public Map<Integer,List<ProductDataPrice>> getProductDataPriceByMember(String nameMember) {
        if(nameMember==null){
            throw new IllegalArgumentException(Error.makeOrderNameNullError());
        }
        if(nameMember.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderNameEmptyError());
        }
        Map<Integer,List<ProductDataPrice>> ans=new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Order>> outerEntry : orders.entrySet()) {
            Integer outerKey = outerEntry.getKey();
            Map<Integer, Order> innerMap = outerEntry.getValue();
            List<ProductDataPrice> productDataPrices=new LinkedList<>();
            for (Map.Entry<Integer, Order> innerEntry : innerMap.entrySet()) {
                Order order = innerEntry.getValue();
                if (order.getMemberName().equals(nameMember)) {
                    Map<Integer, String> orderProductsJsons = order.getOrderProductsJsons();
                    for (String productsJsons: orderProductsJsons.values() ) {
                        productDataPrices.add(fromJson(productsJsons));
                    }
                }
            }
            if(productDataPrices.size()!=0)
                ans.put(outerKey,productDataPrices);
        }
        if(ans.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderNoOrdersForUserError(nameMember));
        }
        return ans;
    }



    public static ProductDataPrice fromJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, ProductDataPrice.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Map<Integer,OrderDTO> getOrderByOrderId(int orderId) {
        Map<Integer,OrderDTO> orderDTOByOrderId = new HashMap<>();
        if(orders.containsKey(orderId)){
            Map<Integer,Order> orderByOrderId = orders.get(orderId);
            for (Integer storeId : orderByOrderId.keySet()) {
                OrderDTO orderDTO=orderToDTO(orderByOrderId.get(storeId));
                orderDTOByOrderId.put(storeId,orderDTO);
            }
        }
        else {
            throw new IllegalArgumentException(Error.makeOrderDoesntExistError(orderId));
        }
        return orderDTOByOrderId;
    }

    public List<OrderDTO> getAllOrders(){
        List<OrderDTO> allOrders = new LinkedList<>();
        for (Map.Entry<Integer, Map<Integer, Order>> outerEntry : orders.entrySet()) {
            Map<Integer, Order> innerMap = outerEntry.getValue();
            for (Map.Entry<Integer, Order> innerEntry : innerMap.entrySet()) {
                allOrders.add(orderToDTO(innerEntry.getValue()));
            }
        }
        if(allOrders.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderNoOrdersError());
        }
        return allOrders;
    }

    public void resetOrders(){
        orders = new HashMap<>();
    }


}
