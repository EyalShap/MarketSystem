package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdditionDiscount extends CompositeDiscount{
    public AdditionDiscount(int id, Discount discountA, Discount discountB) {
        super(id, discountA, discountB);
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        List<ProductDataPrice> newListProductsPrice= new ArrayList<>();
        double NewPriceA;
        double NewPriceB;
        ProductDataPrice currentProduct;
        List<Double> oldNewPrices = new ArrayList<>();
        for(ProductDataPrice productDataPrice : listProductsPrice){
            oldNewPrices.add(productDataPrice.getNewPrice());
            newListProductsPrice.add(productDataPrice.deepCopy());
        }
        discountA.giveDiscount(productDTOMap, listProductsPrice);
        discountB.giveDiscount(productDTOMap, newListProductsPrice);
        //loop over every item and merge both discounts
        for(int i = 0; i <listProductsPrice.size(); i++){
            currentProduct = listProductsPrice.get(i);
            NewPriceA  = currentProduct.getNewPrice();
            NewPriceB  = newListProductsPrice.get(i).getNewPrice();
            //NewPriceA = oldNewPrice - discountA; NewPriceB = oldNewPrice - discountB;
            //NewPriceA - (oldNewPrice - NewPriceB) = oldNewPrice - discountA - discountB
            currentProduct.setNewPrice(NewPriceA - (oldNewPrices.get(i) - NewPriceB));

        }
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        return true;
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        List<ProductDataPrice> newListProductsPriceA= new ArrayList<>();
        List<ProductDataPrice> newListProductsPriceB= new ArrayList<>();
        double countTotalNewNewPrice = 0;
        double countTotalOldNewPrice = 0;
        double NewPriceA;
        double NewPriceB;
        ProductDataPrice currentProduct;
        List<Double> oldNewPrices = new ArrayList<>();
        for(ProductDataPrice productDataPrice : listProductsPrice){
            countTotalOldNewPrice = countTotalOldNewPrice + productDataPrice.getNewPrice();
            oldNewPrices.add(productDataPrice.getNewPrice());
            newListProductsPriceA.add(productDataPrice.deepCopy());
            newListProductsPriceB.add(productDataPrice.deepCopy());

        }
        discountA.giveDiscount(productDTOMap, newListProductsPriceA);
        discountB.giveDiscount(productDTOMap, newListProductsPriceB);
        //loop over every item and merge both discounts
        for(int i = 0; i <newListProductsPriceA.size(); i++){
            currentProduct = newListProductsPriceA.get(i);
            NewPriceA  = currentProduct.getNewPrice();
            NewPriceB  = newListProductsPriceB.get(i).getNewPrice();
            //NewPriceA = oldNewPrice - discountA; NewPriceB = oldNewPrice - discountB;
            //NewPriceA - (oldNewPrice - NewPriceB) = oldNewPrice - discountA - discountB
            currentProduct.setNewPrice(NewPriceA - (oldNewPrices.get(i) - NewPriceB));
        }

        for(ProductDataPrice productDataPrice : newListProductsPriceA){
            countTotalNewNewPrice = countTotalNewNewPrice + productDataPrice.getNewPrice();
        }
        return countTotalOldNewPrice - countTotalNewNewPrice;
    }

}
