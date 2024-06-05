package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrDiscount extends CompositeDiscount{

    public OrDiscount(Discount discountA, Discount discountB) {
        super(discountA, discountB);
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        if(checkCond(productDTOMap, listProductsPrice)){
            discountA.giveDiscount(productDTOMap, listProductsPrice);
            discountB.giveDiscount(productDTOMap, listProductsPrice);
        }
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        return discountA.checkCond(productDTOMap, listProductsPrice) || discountA.checkCond(productDTOMap, listProductsPrice);
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,listProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,listProductsPrice);
        double countOldNewPrice = 0;
        double countNewNewPrice = 0;

        List<ProductDataPrice> newListProductsPrice= new ArrayList<>();
        //complicated but ok:
        // I create a deep copy of ListProductsPrice and basically calculate before and after the two discounts
        for(ProductDataPrice productDataPrice : listProductsPrice){
            countOldNewPrice = countOldNewPrice + productDataPrice.getNewPrice();
            newListProductsPrice.add(productDataPrice.deepCopy());
        }
        if(condDiscountA || condDiscountB) {
            discountA.giveDiscount(productDTOMap, newListProductsPrice);
            discountB.giveDiscount(productDTOMap, newListProductsPrice);
        }
        for(ProductDataPrice productDataPrice : newListProductsPrice){
            countNewNewPrice = countNewNewPrice + productDataPrice.getNewPrice();
        }
        return countOldNewPrice - countNewNewPrice;
    }
}
