package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class XorDiscount extends CompositeDiscount{
    //0 = min, 1 = max
    int minOrMax;

    public XorDiscount(int id, Discount discountA, Discount discountB){
        super(id, discountA, discountB);
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,ListProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,ListProductsPrice);
        double totalDiscountA = discountA.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        double totalDiscountB = discountB.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);

        if(condDiscountA && condDiscountB){
            if((isMin() && totalDiscountA < totalDiscountB) || (isMax() && totalDiscountA > totalDiscountB) ){
                discountA.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
            }
            else{
                discountB.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
            }
        }
        else if(condDiscountA){
            discountA.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
        }
        else if(condDiscountB){
            discountB.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
        }
    }

    //decide according to the decideing factor
    @Override
    public void giveDiscountWithoutCondition(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,ListProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,ListProductsPrice);
        double totalDiscountA = discountA.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        double totalDiscountB = discountB.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        if((isMin() && totalDiscountA < totalDiscountB) || (isMax() && totalDiscountA > totalDiscountB) ){
            discountA.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
        }
        else{
            discountB.giveDiscountWithoutCondition(productDTOMap, ListProductsPrice);
        }

    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        return discountA.checkCond(productDTOMap, ListProductsPrice) || discountB.checkCond(productDTOMap, ListProductsPrice);
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        boolean condDiscountA =discountA.checkCond(productDTOMap,ListProductsPrice);
        boolean condDiscountB =discountB.checkCond(productDTOMap,ListProductsPrice);
        double totalDiscountA = discountA.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        double totalDiscountB = discountB.giveTotalPriceDiscount(productDTOMap,ListProductsPrice);
        if(condDiscountA && condDiscountB){
            if((isMin() && totalDiscountA < totalDiscountB) || (isMax() && totalDiscountA > totalDiscountB) ){
                return totalDiscountA;
            }
            else{
                return totalDiscountB;
            }
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

    public void setMin() {
        this.minOrMax = 0;
    }

    public void setMax() {
        this.minOrMax = 1;
    }

    public boolean isMin() {
        return minOrMax == 0;
    }

    public boolean isMax() {
        return minOrMax == 1;
    }

    @Override
    public String description() {
        String description = "takes only one of two discounts based on if its condition is met and ";
        if(isMin()){
            description = description + "it saves more money\n";
        }
        else{
            description = description + "it saves less money\n";
        }
        description = description + "discountA: " + discountA.description() + "\n";
        description = description + "discountB: " + discountB.description();
        return description;
    }
}
