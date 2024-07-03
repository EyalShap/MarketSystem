package com.sadna.sadnamarket.domain.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.relational.core.sql.In;
import org.hibernate.query.Query;
//import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HibernateOrderRepository implements IOrderRepository{

    @Override
    public int createOrder(Map<Integer, OrderDTO> storeOrdersDTO, String memberName) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            OrderWrapper orderWrapper = new OrderWrapper();
            orderWrapper.setMemberName(memberName);
            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the date and time
            String formattedNow = now.format(formatter);
            orderWrapper.setDateTimeOfPurchase(formattedNow);
            session.save(orderWrapper);
            int orderID=orderWrapper.getId();

            for (Map.Entry<Integer, OrderDTO> entry : storeOrdersDTO.entrySet()) {
                OrderDTO orderDTO = entry.getValue();
                orderDTO.setStoreId(entry.getKey());
                orderDTO.setOrderWrapper(orderWrapper);
                session.save(orderDTO);
            }
            transaction.commit();
            return orderID;
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeOrderNoOrdersForUserError(memberName));
        }
    }

    @Override
    public List<ProductDataPrice> getOrders(int storeId) {
        List<ProductDataPrice> productDataPrices=new LinkedList<>();
        List<OrderDTO> orders = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM OrderDTO WHERE storeId = :storeId";
            Query<OrderDTO> query = session.createQuery(hql, OrderDTO.class);
            query.setParameter("storeId", storeId);
            //orders =session.createQuery( "select s.storeId from StoreDTO s" ).list();
            orders=query.getResultList();
            for (OrderDTO order: orders) {
                Map<Integer, String> orderProductsJsons=order.getOrderProductsJsons();
                for (String productsJsons: orderProductsJsons.values() ) {
                    productDataPrices.add(fromJson(productsJsons));
                }
            }
        }catch (Exception e) {
            throw new IllegalArgumentException(Error.makeOrderStoreNoOrdersError(storeId));
        }

        if(productDataPrices.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderStoreNoOrdersError(storeId));
        }
        return productDataPrices;
    }

    @Override
    public Map<Integer, OrderDetails> getProductDataPriceByMember(String nameMember) {
        Map<Integer, OrderDetails> ans=new HashMap<>();
        //Map<Integer, String> orderDataMap = new HashMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql1 = "SELECT ow.id, ow.dateTimeOfPurchase FROM OrderWrapper ow WHERE ow.memberName = :memberName";
            Query<Object[]> query1 = session.createQuery(hql1, Object[].class);
            query1.setParameter("memberName", nameMember);
            List<Object[]> results = query1.getResultList();
            for (Object[] result : results) {
                List<OrderDTO> orders = null;
                String hql = "FROM OrderDTO o WHERE o.orderWrapper.id = :orderWrapperId";
                Query<OrderDTO> query = session.createQuery(hql, OrderDTO.class);
                Integer orderId= (Integer)result[0];
                query.setParameter("orderWrapperId", orderId);
                orders = query.getResultList();
                String dateTime= (String)result[1];
                List<ProductDataPrice> productDataPrices = new LinkedList<>();
                for (OrderDTO order:orders) {
                    Map<Integer, String> orderProductsJsons = order.getOrderProductsJsons();
                    for (String productsJsons : orderProductsJsons.values()) {
                        productDataPrices.add(fromJson(productsJsons));
                    }
                }

                if(productDataPrices.size()!=0) {
                    OrderDetails OrderDetails=new OrderDetails(productDataPrices,dateTime);
                    ans.put(orderId,OrderDetails);
                }
            }
        }
        if(ans.isEmpty()){
            throw new IllegalArgumentException(Error.makeOrderNoOrdersForUserError(nameMember));
        }
        return ans;
    }

    @Override
    public Map<Integer, OrderDTO> getOrderByOrderId(int orderId) {
        Map<Integer, OrderDTO> ans=new HashMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM OrderDTO o WHERE o.orderWrapper.id = :orderWrapperId";
            Query<OrderDTO> query = session.createQuery(hql, OrderDTO.class);
            query.setParameter("orderWrapperId", orderId);
            List<OrderDTO> orders = query.getResultList();
            for (OrderDTO order : orders) {
                ans.put(order.getStoreId(), order);
            }
        }catch (Exception e) {
            throw new IllegalArgumentException(Error.makeOrderDoesntExistError(orderId));
        }

        return ans;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Orders";
            Query<OrderDTO> query = session.createQuery(hql, OrderDTO.class);
            orders = query.getResultList();
        }
        return orders;
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

    public static void main(String[] args) {
        Map<Integer, OrderDTO> ans=new HashMap<>();
        Map<Integer,Integer> amunt=new HashMap<>();
        amunt.put(4,8);
        Map<Integer,String> json=new HashMap<>();
        json.put(4,"{\"id\":,4\"storeId\":101,\"name\":\"store1\",\"amount\":8,\"oldPrice\":7,\"newPrice\":10}");
        OrderDTO orderDTO =new OrderDTO("צשאשמ","rami levi",amunt,json);
        Map<Integer,OrderDTO> stors =new HashMap<>();
        stors.put(4,orderDTO);
        HibernateOrderRepository a=new HibernateOrderRepository();
        a.createOrder(stors,"nisim");
       // ans=a.getOrderByOrderId(160);
        int k=6;
    }



}
