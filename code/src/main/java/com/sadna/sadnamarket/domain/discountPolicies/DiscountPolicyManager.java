package com.sadna.sadnamarket.domain.discountPolicies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sadna.sadnamarket.domain.discountPolicies.Discounts.Discount;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

public class DiscountPolicyManager {
    private List<Integer>  discountIds;
    private DiscountPolicyFacade discountPolicyFacade;
    public DiscountPolicyManager(DiscountPolicyFacade discountPolicyFacade) {
        discountIds = new ArrayList<>();
        this.discountPolicyFacade = discountPolicyFacade;
    }

    public synchronized void addDiscountPolicy(int discountPolicyId) throws Exception{
        if(discountIds.contains(discountPolicyId))
            throw new Exception();
        discountIds.add(discountPolicyId);
    }

    // for now that function dosent do anything special
    public List<ProductDataPrice> giveDiscount(List<CartItemDTO> cart, Map<Integer, ProductDTO> productDTOMap) {
        List<ProductDataPrice> listProductDataPrice = new ArrayList<>();
        for (CartItemDTO cartItemDTO : cart) {
            ProductDTO pDTO = productDTOMap.get(cartItemDTO.getProductId());
            //create the ProductDataPrice and add it to listProductDataPrice
            ProductDataPrice productDataPrice = new ProductDataPrice(cartItemDTO.getProductId(),
                    cartItemDTO.getAmount(),
                    pDTO.getProductPrice(), pDTO.getProductPrice());
            listProductDataPrice.add(productDataPrice);
        }
        for(Integer discountID : discountIds){
            Discount discount = discountPolicyFacade.getDiscount(discountID);
            discount.giveDiscount(productDTOMap, listProductDataPrice);
        }
        return listProductDataPrice;
    }

}
