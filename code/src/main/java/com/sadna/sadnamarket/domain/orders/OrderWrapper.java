package com.sadna.sadnamarket.domain.orders;
import com.sadna.sadnamarket.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "OrderWrapper")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "date_time_purchase")
    private String dateTimeOfPurchase;

    @OneToMany(mappedBy = "orderWrapper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDateTimeOfPurchase() {
        return dateTimeOfPurchase;
    }

    public void setDateTimeOfPurchase(String dateTimeOfPurchase) {
        this.dateTimeOfPurchase = dateTimeOfPurchase;
    }

//    public Set<StoreOrderDetail> getStoreOrderDetails() {
//        return storeOrderDetails;
//    }
//
//    public void setStoreOrderDetails(Set<StoreOrderDetail> storeOrderDetails) {
//        this.storeOrderDetails = storeOrderDetails;
//    }

    public static void main(String[] args) {
//        System.out.println("asdas");
//
//        int x=5;
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction transaction = null;
//
//        try {
//            transaction = session.beginTransaction();
//
//            // Create OrderWrapper
//            OrderWrapper orderWrapper = new OrderWrapper();
//            orderWrapper.setMemberName("John Doe");
//            orderWrapper.setDateTimeOfPurchase("2023-06-27T10:15:30"); // example datetime in ISO 8601 format
//
//            // Create StoreOrderDetail
//            StoreOrderDetail storeOrderDetail1 = new StoreOrderDetail();
//            storeOrderDetail1.setIdStore(1L);
//            storeOrderDetail1.setStoreNameWhenOrdered("Store 1");
//
//            Map<Integer, String> orderProductsJsons1 = new HashMap<>();
//            orderProductsJsons1.put(1, "{\"name\": \"Product 1\", \"price\": 100}");
//            orderProductsJsons1.put(2, "{\"name\": \"Product 2\", \"price\": 200}");
//            storeOrderDetail1.setOrderProductsJsons(orderProductsJsons1);
//
//            // Set the relationship
//            storeOrderDetail1.setOrderWrapper(orderWrapper);
////            orderWrapper.getStoreOrderDetails().add(storeOrderDetail1);
//
//            // Another StoreOrderDetail
//            StoreOrderDetail storeOrderDetail2 = new StoreOrderDetail();
//            storeOrderDetail2.setIdStore(2L);
//            storeOrderDetail2.setStoreNameWhenOrdered("Store 2");
//
//            Map<Integer, String> orderProductsJsons2 = new HashMap<>();
//            orderProductsJsons2.put(3, "{\"name\": \"Product 3\", \"price\": 300}");
//            orderProductsJsons2.put(4, "{\"name\": \"Product 4\", \"price\": 400}");
//            storeOrderDetail2.setOrderProductsJsons(orderProductsJsons2);
//
//            // Set the relationship
//            storeOrderDetail2.setOrderWrapper(orderWrapper);
////            orderWrapper.getStoreOrderDetails().add(storeOrderDetail2);
//
//            // Save OrderWrapper (this will also save StoreOrderDetail due to CascadeType.ALL)
//            session.save(orderWrapper);
//
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }
    }
}
