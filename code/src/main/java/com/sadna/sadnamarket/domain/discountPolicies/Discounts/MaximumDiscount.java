package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class MaximumDiscount extends CompositeDiscount{
    public MaximumDiscount(int id, Discount discountA, Discount discountB) {
        super(id, discountA, discountB);
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,ListProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,ListProductsPrice);
        double totalDiscountA = discountA.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        double totalDiscountB = discountB.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        if(condDiscountA && condDiscountB){
            if(totalDiscountA > totalDiscountB){
                discountA.giveDiscount(productDTOMap, ListProductsPrice);
            }
            else{
                discountB.giveDiscount(productDTOMap, ListProductsPrice);
            }
        }
        else if(condDiscountA){
            discountA.giveDiscount(productDTOMap, ListProductsPrice);
        }
        else if(condDiscountB){
            discountB.giveDiscount(productDTOMap, ListProductsPrice);
        }

    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        return true;
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,listProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,listProductsPrice);
        double totalDiscountA = discountA.giveTotalPriceDiscount(productDTOMap,listProductsPrice);
        double totalDiscountB = discountB.giveTotalPriceDiscount(productDTOMap,listProductsPrice);
        if(condDiscountA && condDiscountB){
            return Math.max(totalDiscountA, totalDiscountB);
        }
        else if(condDiscountA){
            return totalDiscountA;
        }
        else if(condDiscountB){
            return totalDiscountB;
        }
        else {
            return 0;
        }
    }
}
