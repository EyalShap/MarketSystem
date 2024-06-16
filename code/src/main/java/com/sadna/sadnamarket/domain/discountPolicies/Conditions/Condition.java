package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(value = { "id" })
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Condition{
    protected int id;

    Condition(int id){
        this.id = id;
    }
    abstract public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    abstract public String description();
}
