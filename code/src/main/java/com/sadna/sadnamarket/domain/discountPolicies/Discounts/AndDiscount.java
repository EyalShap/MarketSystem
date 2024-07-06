package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "anddiscount")
public class AndDiscount extends CompositeDiscount{

    public AndDiscount(int id, Discount discountA, Discount discountB) {
        super(id, discountA, discountB);
    }
    public AndDiscount(Discount discountA, Discount discountB) {
        super(discountA, discountB);
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        if(checkCond(productDTOMap, listProductsPrice)){
            discountA.giveDiscountWithoutCondition(productDTOMap, listProductsPrice);
            discountB.giveDiscountWithoutCondition(productDTOMap, listProductsPrice);
        }
    }
    @Override
    public void giveDiscountWithoutCondition(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        discountA.giveDiscountWithoutCondition(productDTOMap, listProductsPrice);
        discountB.giveDiscountWithoutCondition(productDTOMap, listProductsPrice);
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        return discountA.checkCond(productDTOMap, listProductsPrice) && discountB.checkCond(productDTOMap, listProductsPrice);
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

    @Override
    public String description() {
        String description = "takes two discounts and if both their condition is met then both discounts apply\n";
        description = description + "discountA: " + discountA.description() + "\n";
        description = description + "discountB: " + discountB.description();
        return description;
    }
}
