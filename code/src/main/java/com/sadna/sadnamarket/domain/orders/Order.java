package com.sadna.sadnamarket.domain.orders;


import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "member_name")
    private String memberName;

    @Column(name = "store_id")
    private Integer storeId;
    @Column(name = "store_name_when_ordered")
    private String storeNameWhenOrdered;
    @ElementCollection
    @CollectionTable(name = "Order_Product_Amounts", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "amount")
    private Map<Integer, Integer> productAmounts;
    @ElementCollection
    @CollectionTable(name = "Order_Products_Jsons", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "json")
    private Map<Integer, String> orderProductsJsons;


    @ManyToOne
    @JoinColumn(name = "order_wrapper_id")
    private OrderWrapper orderWrapper;

    public Order(){

    }

    public Order(String memberName,String storeNameWhenOrdered,Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        this.memberName=memberName;
        this.storeNameWhenOrdered=storeNameWhenOrdered;
        this.productAmounts=productAmounts;
        this.orderProductsJsons=orderProductsJsons;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getStoreNameWhenOrdered() {
        return storeNameWhenOrdered;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public Map<Integer, String> getOrderProductsJsons() {
        return orderProductsJsons;
    }

}

