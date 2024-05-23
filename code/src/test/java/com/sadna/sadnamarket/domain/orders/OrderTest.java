package com.sadna.sadnamarket.domain.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class OrderTest {

    private MemoryOrderRepository memoryOrderRepository;
    @BeforeEach
    void setUp(){
        memoryOrderRepository = new MemoryOrderRepository();
        memoryOrderRepository.resetOrders();
    }

    @Test
    void givenNullStoreOrder(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.createOrder(null);
        });
        assertEquals("The order is null.", exception.getMessage());
    }

    @Test
    void givenEmptyStoreOrder(){
        Map<Integer,OrderDTO> storeOrder = new HashMap<>();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.createOrder(storeOrder);
        });
        assertEquals("The order is empty.", exception.getMessage());
    }

    @Test
    void givenNullOrderToStore(){
        Map<Integer,OrderDTO> storeOrder = new HashMap<>();
        storeOrder.put(1,null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.createOrder(storeOrder);
        });
        assertEquals("The store with ID: 1 has null order.", exception.getMessage());
    }

    @Test
    void givenEmptyOrderToStore(){
        Map<Integer,OrderDTO> storeOrder = new HashMap<>();
        OrderDTO orderDTO=new OrderDTO(1,"store",new HashMap<>(),new HashMap<>());
        storeOrder.put(1,orderDTO);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.createOrder(storeOrder);
        });
        assertEquals("The store with ID: 1 has no products.", exception.getMessage());
    }

    @Test
    void givenStoreAndOrder(){
        Map<Integer,OrderDTO> storeOrder = new HashMap<>();
        Map<Integer,Integer> productAmounts=new HashMap<>();
        productAmounts.put(1,1);
        Map<Integer,String> orderProductsJsons=new HashMap<>();
        orderProductsJsons.put(1,"banana");
        OrderDTO orderDTO=new OrderDTO(1,"store",productAmounts,orderProductsJsons);
        storeOrder.put(1,orderDTO);

        int orderId = memoryOrderRepository.createOrder(storeOrder);

        assertEquals(orderId, 0);
    }
    @Test
    void givenBadStoreId(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.getOrders(1);
        });
        assertEquals("The store with ID: 1 has no orders.", exception.getMessage());
    }

    @Test
    void givenStoreIdToGetOrders(){
        Map<Integer,OrderDTO> storeOrder = new HashMap<>();
        Map<Integer,Integer> productAmounts=new HashMap<>();
        productAmounts.put(1,1);
        Map<Integer,String> orderProductsJsons=new HashMap<>();
        orderProductsJsons.put(1,"banana");
        OrderDTO orderDTO=new OrderDTO(1,"store",productAmounts,orderProductsJsons);
        storeOrder.put(1,orderDTO);
        memoryOrderRepository.createOrder(storeOrder);
        List<OrderDTO> orders= memoryOrderRepository.getOrders(1);

        assertEquals(orders.size(), 1);
    }

}
