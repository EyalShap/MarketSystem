package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class TrueCondition extends Condition{

    TrueCondition(int id) {
        super(id);
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        return true;
    }
}
