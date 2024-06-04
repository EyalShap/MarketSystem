package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class simpleCondition implements Discount{
    double percentage;
    String productName;
    String categoryName;
    boolean chosePath; // promise there is CategoryName Xor ProductName
    Condition condition;

    public simpleCondition(double percentage, Condition condition){
        this.percentage = percentage;
        productName = null;
        categoryName = null;
        chosePath = false;
    }

    public void setOnProductName(String productName) {
        if(!chosePath) {
            this.productName = productName;
        }
        chosePath = true;
    }

    public void setOnCategoryName(String categoryName) {
        if(!chosePath){
            this.categoryName = categoryName;
        }
        chosePath = true;
    }

    public void setOnStore() {
        chosePath = true;
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {

    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        return condition.checkCond(productDTOMap, ListProductsPrice);
    }
}
