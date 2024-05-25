package com.sadna.sadnamarket.domain.discountPolicies;

import java.util.Date;
import java.util.List;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

public abstract class DiscountPolicy {
    int id;
    int percent;
    Date endDate;
    // note every product in store is the root category
    String category;
    List<Integer> productsId;

    public DiscountPolicy(String args) {
        id = id;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for percent
    public int getPercent() {
        return percent;
    }

    // Setter for percent
    public void setPercent(int percent) {
        this.percent = percent;
    }

    // Getter for endDate
    public Date getEndDate() {
        return endDate;
    }

    // Setter for endDate
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter for category
    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for productsId
    public List<Integer> getProductsId() {
        return productsId;
    }

    // Setter for productsId
    public void setProductsId(List<Integer> productsId) {
        this.productsId = productsId;
    }

    public abstract int giveDiscount(ProductDTO productDTO, CartItemDTO cartItemDTO);

}
