package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import java.util.List;
import java.util.Map;

public class MinProductCondition extends Condition{
    private final int minAmount;
    private Integer productID;
    private String categoryName;
    private boolean chosePath; // promise there is CategoryName Xor ProductName

    public MinProductCondition(int id, int minAmount){
        super(id);
        this.minAmount = minAmount;
        productID = null;
        categoryName = null;
        chosePath = false;
    }

    public void setOnProductName(int productID) {
        if(!chosePath) {
            this.productID = productID;
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
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        int totalAmount;
        if(!chosePath){
            return false;
        }
        if(productID != null){
            totalAmount = countOnlyProduct(productDTOMap, listProductsPrice);
        }
        else if(categoryName != null){
            totalAmount = countOnlyCategory(productDTOMap, listProductsPrice);
        }
        else{
            totalAmount = countAll(listProductsPrice);
        }
        return minAmount <= totalAmount;
    }
    private int countOnlyCategory(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        String thisCategoryName;
        int amount;
        int total = 0;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisCategoryName = productDTOMap.get(itemID).getProductCategory();
            if(thisCategoryName.equals(categoryName)){
                amount = item.getAmount();
                total = total + amount;
            }
        }
        return total;
    }

    private int countOnlyProduct(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice){
        int itemID;
        int thisProductID;
        int amount;
        int total = 0;
        for(ProductDataPrice item : listProductsPrice){
            itemID = item.getId();
            thisProductID = productDTOMap.get(itemID).getProductID();
            if(thisProductID == productID){
                amount = item.getAmount();
                total = total + amount;
            }
        }
        return total;
    }

    private int countAll(List<ProductDataPrice> listProductsPrice){
        int amount;
        int total = 0;
        for(ProductDataPrice item : listProductsPrice){
            amount = item.getAmount();
            total = total + amount;
        }
        return total;
    }

    public String description() {
        String addEnding;
        if(productID != null){
            addEnding = String.format("product with ID <%d>", productID);
        }
        else if(categoryName != null){
            addEnding = "products from category: " + categoryName;
        }
        else{
            addEnding = "products";
        }
        return "the cart has at least " + minAmount + " " +addEnding;
    }
}
