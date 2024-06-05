package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class simpleDiscount implements Discount{
    //percentage is (100-0)
    private final double percentage;
    private String productName;
    private String categoryName;
    private boolean chosePath; // promise there is CategoryName Xor ProductName
    private Condition condition;

    public simpleDiscount(double percentage, Condition condition){
        this.percentage = percentage;
        productName = null;
        categoryName = null;
        chosePath = false;
    }

    public void setOnProductName(String productName) {
        if(!chosePath) {
            this.productName = productName;
        }
        chosePath = true;
    }

    public void setOnCategoryName(String categoryName) {
        if(!chosePath){
            this.categoryName = categoryName;
        }
        chosePath = true;
    }

    public void setOnStore() {
        chosePath = true;
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        if(checkCond(productDTOMap,listProductsPrice)){
            if(productName != null){
                discountOnlyProduct(productDTOMap, listProductsPrice);
            }
            else if(categoryName != null){
                discountOnlyCategory(productDTOMap, listProductsPrice);
            }
            else if(chosePath){
                discountAll(listProductsPrice);
            }
        }
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        return condition.checkCond(productDTOMap, listProductsPrice);
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        if(productName != null){
            return checkTotalDiscountOnlyProduct(productDTOMap, listProductsPrice);
        }
        else if(categoryName != null){
            return checkTotalDiscountOnlyCategory(productDTOMap, listProductsPrice);
        }
        else if(chosePath){
            return checkTotalDiscountAll(listProductsPrice);
        }
        return  0;
    }

    private void discountOnlyProduct(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        String thisProductName;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisProductName = productDTOMap.get(itemID).getProductCategory();
            if(thisProductName.equals(productName)) {
                setNewPrice(item);
            }
        }
    }
    private void discountOnlyCategory(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        String thisCategoryName;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisCategoryName = productDTOMap.get(itemID).getProductCategory();
            if(thisCategoryName.equals(categoryName)) {
                setNewPrice(item);
            }
        }
    }
    private void discountAll(List<ProductDataPrice> listProductsPrice){
        for(ProductDataPrice item : listProductsPrice){
            setNewPrice(item);
        }
    }

    private void setNewPrice(ProductDataPrice item){
        double oldNewPrice;
        double newNewPrice;
        oldNewPrice = item.getNewPrice();
        newNewPrice = oldNewPrice*(1 - percentage/100);
        item.setNewPrice(newNewPrice);
    }

    private double checkTotalDiscountOnlyProduct(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        double total = 0;
        double oldNewPrice;
        String thisProductName;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisProductName = productDTOMap.get(itemID).getProductCategory();
            if(thisProductName.equals(productName)) {
                oldNewPrice = item.getNewPrice();
                total = total + oldNewPrice*(percentage/100);
            }
        }
        return total;
    }
    private double checkTotalDiscountOnlyCategory(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        double total = 0;
        double oldNewPrice;
        String thisCategoryName;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisCategoryName = productDTOMap.get(itemID).getProductCategory();
            if(thisCategoryName.equals(categoryName)) {
                oldNewPrice = item.getNewPrice();
                total = total + oldNewPrice*(percentage/100);
            }
        }
        return total;
    }
    private double checkTotalDiscountAll(List<ProductDataPrice> listProductsPrice){
        double total = 0;
        double oldNewPrice;
        for(ProductDataPrice item : listProductsPrice){
            oldNewPrice = item.getNewPrice();
            total = total + oldNewPrice*(percentage/100);
        }
        return total;
    }


}
