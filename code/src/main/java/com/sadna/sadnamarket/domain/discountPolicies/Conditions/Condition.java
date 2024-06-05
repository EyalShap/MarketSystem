package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;
import java.util.Map;

public interface Condition {
    boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);
}
