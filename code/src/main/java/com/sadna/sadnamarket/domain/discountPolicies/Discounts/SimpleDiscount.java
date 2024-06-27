package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class SimpleDiscount extends Discount {
    //percentage is (100-0)
    private final double percentage;
    private Integer productID;
    private String categoryName;
    private final Condition condition;

    public SimpleDiscount(int id, double percentage, Condition condition){
        super(id);
        this.percentage = percentage;
        productID = null;
        categoryName = null;
        this.condition = condition;
    }

    public void setOnProductID(int productID) {
        this.productID = productID;
    }

    public void setOnCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setOnStore() {
        ;
    }

    @Override
    public void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        int itemID;
        int thisProductID;
        String thisCategoryName;
        //if the item not under the specified category or the o=product id than contu=inue to the next loop
        //without giving him the discount
        if(checkCond(productDTOMap,listProductsPrice)){
            for(ProductDataPrice item : listProductsPrice){
                itemID = item.getId();
                if(productID != null){
                    thisProductID = productDTOMap.get(itemID).getProductID();
                    if(thisProductID != productID) {
                        continue;
                    }
                }
                else if(categoryName != null){
                    thisCategoryName = productDTOMap.get(itemID).getProductCategory();
                    if(!thisCategoryName.equals(categoryName)) {
                        continue;
                    }
                }
                setNewPrice(item);
            }
        }
    }

    @Override
    public void giveDiscountWithoutCondition(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        int itemID;
        int thisProductID;
        String thisCategoryName;
        //if the item not under the specified category or the o=product id than contu=inue to the next loop
        //without giving him the discount
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            if(productID != null){
                thisProductID = productDTOMap.get(itemID).getProductID();
                if(thisProductID != productID) {
                    continue;
                }
            }
            else if(categoryName != null){
                thisCategoryName = productDTOMap.get(itemID).getProductCategory();
                if(!thisCategoryName.equals(categoryName)) {
                    continue;
                }
            }
            setNewPrice(item);
        }
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        return condition.checkCond(productDTOMap, listProductsPrice);
    }

    @Override
    public double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        int itemID;
        int thisProductID;
        String thisCategoryName;
        double total = 0;
        double oldNewPrice;
        //if the item not under the specified category or the has the wanted product id than continue to the next loop
        //without giving him the discount
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            if(productID != null){
                thisProductID = productDTOMap.get(itemID).getProductID();
                if(thisProductID != productID) {
                    continue;
                }
            }
            else if(categoryName != null){
                thisCategoryName = productDTOMap.get(itemID).getProductCategory();
                if(!thisCategoryName.equals(categoryName)) {
                    continue;
                }
            }
            oldNewPrice = item.getNewPrice();
            total = total + oldNewPrice*(percentage/100);
        }
        return  total;
    }



    private void setNewPrice(ProductDataPrice item){
        double oldNewPrice;
        double newNewPrice;
        oldNewPrice = item.getNewPrice();
        newNewPrice = oldNewPrice*(1 - percentage/100);
        item.setNewPrice(newNewPrice);
    }


    @Override
    public String description() {
        String addEnding;
        if(productID != null){
            addEnding = String.format("product with ID <%d>", productID);
        }
        else if(categoryName != null){
            addEnding = "products from category: " + categoryName;
        }
        else{
            addEnding ="all products";
        }
        return "If " + condition.description() + " then there is a " + percentage + "% discount on " + addEnding;
    }

}
