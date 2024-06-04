package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class simpleDiscount implements Discount{
    //percentage is (100-0)
    double percentage;
    String productName;
    String categoryName;
    boolean chosePath; // promise there is CategoryName Xor ProductName
    Condition condition;

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

    private void discountOnlyProduct(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        String thisProductName;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisProductName = productDTOMap.get(itemID).getProductCategory();
            if(thisProductName.equals(productName)) {
                SetNewPrice(item);
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
                SetNewPrice(item);
            }
        }
    }
    private void discountAll(List<ProductDataPrice> listProductsPrice){
        for(ProductDataPrice item : listProductsPrice){
            SetNewPrice(item);
        }
    }

    private void SetNewPrice(ProductDataPrice item){
        double oldNewPrice;
        double newNewPrice;
        oldNewPrice = item.getNewPrice();
        newNewPrice = oldNewPrice*(1 - percentage/100);
        item.setNewPrice(newNewPrice);
    }
}
