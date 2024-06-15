package com.sadna.sadnamarket.domain.DisocuntPolicys;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.DiscountPolicy;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class DiscountTests extends DiscountPolicyTest{
    List<ProductDataPrice> listProductDataPrices;
    Map<Integer, ProductDTO> productDTOMap;
    Condition conditionTrue1;
    Condition conditionTrue2;
    Condition conditionFalse1;
    Condition conditionFalse2;

    DiscountPolicy onStore10DiscountTrue1;
    DiscountPolicy onCategoryDairy10DiscountTrue1;
    DiscountPolicy onStore10DiscountFalse1;
    DiscountPolicy onCategoryDairy10DiscountFalse1;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        int minBuyConditionID1 = conditionRepository.createMinBuyCondition(100);
        conditionTrue1 = conditionRepository.findConditionByID(minBuyConditionID1);
        int minBuyConditionID3 = conditionRepository.createMinProductCondition(3, 2);
        conditionTrue2 = conditionRepository.findConditionByID(minBuyConditionID3);
        int minBuyConditionID4 = conditionRepository.createMinBuyCondition(400);
        conditionFalse1 = conditionRepository.findConditionByID(minBuyConditionID4);
        int minBuyConditionID5 = conditionRepository.createMinProductCondition(4, 2);
        conditionFalse2 = conditionRepository.findConditionByID(minBuyConditionID5);
        //---
        int OnStore10discountTrue1ID = discountPolicyRepository.addOnStoreSimpleDiscount(10, conditionTrue1);
        onStore10DiscountTrue1 = discountPolicyRepository.findDiscountPolicyByID(OnStore10discountTrue1ID);

        int OnCategoryDairy10discountTrue1ID = discountPolicyRepository.addOnCategorySimpleDiscount(10,"dairy", conditionTrue2);
        onCategoryDairy10DiscountTrue1 = discountPolicyRepository.findDiscountPolicyByID(OnCategoryDairy10discountTrue1ID);;

        int OnStore10discountFalse1ID = discountPolicyRepository.addOnStoreSimpleDiscount(10, conditionFalse1);
        onStore10DiscountFalse1 = discountPolicyRepository.findDiscountPolicyByID(OnStore10discountFalse1ID);

        int OnCategoryDairy10discountFalse1ID = discountPolicyRepository.addOnCategorySimpleDiscount(10,"dairy", conditionFalse2);
        onCategoryDairy10DiscountFalse1 = discountPolicyRepository.findDiscountPolicyByID(OnCategoryDairy10discountFalse1ID);

        listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(0,0, "eyal",1, 100,100));
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 1,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",3,40,40));

        productDTOMap = new HashMap<>();
        productDTOMap.put(0, productFacade.getProductDTO(0));
        productDTOMap.put(1, productFacade.getProductDTO(1));
        productDTOMap.put(2, productFacade.getProductDTO(2));
    }



    private void resetListProductDataPrices(){
        listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(0,0, "eyal",1, 100,100));
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 1,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",3,40,40));
    }
    @Test
    public void checkConditions() throws Exception {
        assertTrue(conditionTrue1.checkCond(productDTOMap, listProductDataPrices));
        assertTrue(conditionTrue2.checkCond(productDTOMap, listProductDataPrices));
        assertFalse(conditionFalse1.checkCond(productDTOMap, listProductDataPrices));
        assertFalse(conditionFalse2.checkCond(productDTOMap, listProductDataPrices));

    }


    @Test
    public void checkSimpleDiscountOnProduct() throws Exception {
        //condition is true
        int simpleDiscountID1 = discountPolicyRepository.addOnProductSimpleDiscount(50, 0, conditionTrue1);
        DiscountPolicy simpleDiscount1 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID1);
        simpleDiscount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(50 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //condition is false now
        int simpleDiscountID2 = discountPolicyRepository.addOnProductSimpleDiscount(50, 0, conditionFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());

    }

    @Test
    public void checkSimpleDiscountOnCategory() throws Exception {
        //condition is true
        int simpleDiscountID1 = discountPolicyRepository.addOnCategorySimpleDiscount(50, "dairy", conditionTrue1);
        DiscountPolicy simpleDiscount1 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID1);
        simpleDiscount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(10 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //condition is false now
        int simpleDiscountID2 = discountPolicyRepository.addOnCategorySimpleDiscount(50, "dairy", conditionFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());

    }

    @Test
    public void checkSimpleDiscountOnStore() throws Exception {

        //condition is true
        int simpleDiscountID1 = discountPolicyRepository.addOnStoreSimpleDiscount(50, conditionTrue1);
        DiscountPolicy simpleDiscount1 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID1);
        simpleDiscount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(50 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(10 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //condition is false now
        int simpleDiscountID2 = discountPolicyRepository.addOnStoreSimpleDiscount(50, conditionFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());

    }
    @Test
    public void checkMaximumDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addMaximumDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int simpleDiscountID2 = discountPolicyRepository.addMaximumDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int simpleDiscountID3 = discountPolicyRepository.addMaximumDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount3 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID3);
        simpleDiscount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }


    @Test
    public void checkAdditionDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addAdditionDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(16 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(32 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int simpleDiscountID2 = discountPolicyRepository.addAdditionDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int simpleDiscountID3 = discountPolicyRepository.addAdditionDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount3 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID3);
        simpleDiscount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }

    @Test
    public void checkOrDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addOrDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(16.2 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(32.4 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int simpleDiscountID2 = discountPolicyRepository.addOrDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(16.2 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(32.4 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int simpleDiscountID3 = discountPolicyRepository.addOrDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount3 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID3);
        simpleDiscount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }

    @Test
    public void checkMaxXorDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addTakeMaxXorDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int simpleDiscountID2 = discountPolicyRepository.addTakeMaxXorDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int simpleDiscountID3 = discountPolicyRepository.addTakeMaxXorDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount3 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID3);
        simpleDiscount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }

    @Test
    public void checkMinXorDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addTakeMinXorDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int simpleDiscountID2 = discountPolicyRepository.addTakeMinXorDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountTrue1);
        DiscountPolicy simpleDiscount2 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID2);
        simpleDiscount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int simpleDiscountID3 = discountPolicyRepository.addTakeMinXorDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy simpleDiscount3 = discountPolicyRepository.findDiscountPolicyByID(simpleDiscountID3);
        simpleDiscount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }


    @Test
    public void checkAndDiscount() throws Exception {
        //twos condition are true
        int DiscountID1 = discountPolicyRepository.addAndDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountTrue1);
        DiscountPolicy Discount1 = discountPolicyRepository.findDiscountPolicyByID(DiscountID1);
        Discount1.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(16.2 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(32.4 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //one condition is false, one is true
        int Discount2ID2 = discountPolicyRepository.addAndDiscount(onCategoryDairy10DiscountTrue1, onStore10DiscountFalse1);
        DiscountPolicy Discount2 = discountPolicyRepository.findDiscountPolicyByID(Discount2ID2);
        Discount2.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());

        resetListProductDataPrices();
        //two conditions are false,
        int DiscountID3 = discountPolicyRepository.addAndDiscount(onCategoryDairy10DiscountFalse1, onStore10DiscountFalse1);
        DiscountPolicy Discount3 = discountPolicyRepository.findDiscountPolicyByID(DiscountID3);
        Discount3.giveDiscount(productDTOMap, listProductDataPrices);
        assertEquals(100 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(20 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(40 , listProductDataPrices.get(2).getNewPrice());
    }


}
