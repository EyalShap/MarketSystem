package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.List;
import java.util.Map;

public class MinBuyCondition extends Condition{

    private final int minBuy;

    public MinBuyCondition(int id, int minBuy){
        super(id);
        this.minBuy = minBuy;
    }
    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        int itemID;
        double itemPrice;
        int amount;
        double total = 0;
        //computing the total
        for(ProductDataPrice item : ListProductsPrice){
            itemID = item.getId();
            itemPrice = productDTOMap.get(itemID).getProductPrice();
            amount = item.getAmount();
            total = total + amount*itemPrice;
        }
        return minBuy <= total;
    }

    public String description() {
        return "the cart original cost (before discounts) is at least " + minBuy;
    }
}
