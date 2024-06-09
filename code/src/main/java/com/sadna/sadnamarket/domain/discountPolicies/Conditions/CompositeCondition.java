package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public abstract class CompositeCondition extends Condition{
    Condition conditionA;
    Condition conditionB;

    public CompositeCondition(int id, Condition conditionA, Condition conditionB){
        super(id);
        this.conditionA = conditionA;
        this.conditionB = conditionB;

    }
    @Override
    public abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);
}
