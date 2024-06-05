package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public abstract class CompositeCondition implements Condition{
    Condition conditionA;
    Condition conditionB;

    public CompositeCondition(Condition conditionA, Condition conditionB){
        this.conditionA = conditionA;
        this.conditionB = conditionB;

    }
    @Override
    public abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);
}
