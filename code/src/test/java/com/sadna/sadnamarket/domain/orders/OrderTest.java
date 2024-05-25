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
        OrderDTO orderDTO=new OrderDTO("name","store",new HashMap<>(),new HashMap<>());
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
        OrderDTO orderDTO=new OrderDTO("name","store",productAmounts,orderProductsJsons);
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
        OrderDTO orderDTO = new OrderDTO("name","store",productAmounts,orderProductsJsons);
        storeOrder.put(1,orderDTO);
        memoryOrderRepository.createOrder(storeOrder);
        List<OrderDTO> orders= memoryOrderRepository.getOrders(1);

        assertEquals(orders.size(), 1);
    }

    @Test
    void givenNullNameMember(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.getOrdersByMember(null);
        });
        assertEquals("The name is null.", exception.getMessage());
    }

    @Test
    void givenEmptyNameMember(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.getOrdersByMember("");
        });
        assertEquals("The name is empty.", exception.getMessage());
    }

    @Test
    void givenNameWithoutOrders(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.getOrdersByMember("matan");
        });
        assertEquals("There are no orders for matan.", exception.getMessage());
    }
    @Test
    void givenNameGetOrders(){
        HashMap<Integer,Integer> productAmounts1=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons1=new HashMap<>();
        productAmounts1.put(1,5);
        orderProductsJsons1.put(1,"banana");
        OrderDTO orderDTO1 = new OrderDTO("matan","store1",productAmounts1,orderProductsJsons1);
        HashMap<Integer,OrderDTO> orders1 = new HashMap<>();
        orders1.put(1,orderDTO1);

        memoryOrderRepository.createOrder(orders1);
        HashMap<Integer,Integer> productAmounts2=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons2=new HashMap<>();
        productAmounts2.put(4,5);
        orderProductsJsons2.put(4,"orange");
        OrderDTO orderDTO2 = new OrderDTO("noam","store2",productAmounts2,orderProductsJsons2);
        HashMap<Integer,OrderDTO> orders2=new HashMap<>();
        orders2.put(1,orderDTO2);
        memoryOrderRepository.createOrder(orders2);

        HashMap<Integer,Integer> productAmounts3=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons3=new HashMap<>();
        productAmounts3.put(7,10);
        orderProductsJsons3.put(7,"tomato");
        productAmounts3.put(9,5);
        orderProductsJsons3.put(9,"meat");
        OrderDTO orderDTO3 = new OrderDTO("matan","store3",productAmounts3,orderProductsJsons3);
        HashMap<Integer,OrderDTO> orders3=new HashMap<>();
        orders3.put(1,orderDTO3);
        memoryOrderRepository.createOrder(orders3);
        Map<Integer,Map<Integer,OrderDTO>>OrdersByName = memoryOrderRepository.getOrdersByMember("matan");
        assertEquals(OrdersByName.size(),2);
    }

    @Test
    void givenBadOrderId(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memoryOrderRepository.getOrderByOrderId(1);
        });
        assertEquals("The order: 1 does not exist.", exception.getMessage());
    }

    @Test
    void givenOrderId(){
        MemoryOrderRepository memoryOrderRepository = new MemoryOrderRepository();
        HashMap<Integer,Integer> productAmounts1=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons1=new HashMap<>();
        productAmounts1.put(1,5);
        orderProductsJsons1.put(1,"banana");
        OrderDTO orderDTO1 = new OrderDTO("matan","store1",productAmounts1,orderProductsJsons1);
        HashMap<Integer,OrderDTO> orders1 = new HashMap<>();
        orders1.put(1,orderDTO1);
        memoryOrderRepository.createOrder(orders1);

        HashMap<Integer,Integer> productAmounts3=new HashMap<>();
        HashMap<Integer,String>orderProductsJsons3=new HashMap<>();
        productAmounts3.put(7,10);
        orderProductsJsons3.put(7,"tomato");
        productAmounts3.put(9,5);
        orderProductsJsons3.put(9,"meat");
        OrderDTO orderDTO3 = new OrderDTO("matan","store3",productAmounts3,orderProductsJsons3);
        OrderDTO orderDTO4 = new OrderDTO("matan","store4",productAmounts3,orderProductsJsons3);
        HashMap<Integer,OrderDTO> orders3=new HashMap<>();
        orders3.put(1,orderDTO3);
        orders3.put(2,orderDTO4);
        memoryOrderRepository.createOrder(orders3);

        Map<Integer,OrderDTO> storeOrder=memoryOrderRepository.getOrderByOrderId(1);
        assertEquals(storeOrder.size(),2);
    }

}
