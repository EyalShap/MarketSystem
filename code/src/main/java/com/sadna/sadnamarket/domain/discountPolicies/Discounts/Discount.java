package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(value = { "id" })
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Discount {
    protected int id;

    Discount(int id){
        this.id = id;
    }

    public abstract void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);
    public abstract void giveDiscountWithoutCondition(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);
    abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);

    abstract double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public int getDiscountAID() {
        return -1;
    }

    public int getDiscountBID() {
        return -1;
    }

    abstract public String description();

}
