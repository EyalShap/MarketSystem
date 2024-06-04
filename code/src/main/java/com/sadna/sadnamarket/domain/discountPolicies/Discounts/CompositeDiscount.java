package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public abstract class CompositeDiscount implements Discount{
    Discount discountA;
    Discount discountB;
    public CompositeDiscount(Discount discountA, Discount discountB){
        this.discountA = discountA;
        this.discountB = discountB;
    }


    @Override
    public abstract void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);

    @Override
    public abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);
    @Override
    public abstract double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);


}
