package com.sadna.sadnamarket.domain.DisocuntPolicys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.*;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.*;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyFacadeTest extends DiscountPolicyTest{
    Map<Integer, ProductDTO> productDTOMap;
    Condition conditionTrue1;
    Condition conditionTrue2;
    Condition conditionFalse1;
    Condition conditionFalse2;

    int onStore10discountTrue1ID;
    int onCategoryDairy10discountTrue1ID;
    int onStore10discountFalse1ID;
    int onCategoryDairy10discountFalse1ID;

    ObjectMapper objectMapper = new ObjectMapper();

    List<CartItemDTO> cart;

    private String toJson(Discount discount) throws JsonProcessingException {
        return discount.getClass().getName() + "-" + objectMapper.writeValueAsString(discount);

    }

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
        onStore10discountTrue1ID = discountPolicyRepository.addOnStoreSimpleDiscount(10, conditionTrue1);

        onCategoryDairy10discountTrue1ID = discountPolicyRepository.addOnCategorySimpleDiscount(10,"dairy", conditionTrue2);

        onStore10discountFalse1ID = discountPolicyRepository.addOnStoreSimpleDiscount(10, conditionFalse1);

        onCategoryDairy10discountFalse1ID = discountPolicyRepository.addOnCategorySimpleDiscount(10,"dairy", conditionFalse2);

        productDTOMap = new HashMap<>();
        productDTOMap.put(0, productFacade.getProductDTO(0));
        productDTOMap.put(1, productFacade.getProductDTO(1));
        productDTOMap.put(2, productFacade.getProductDTO(2));

        cart = new ArrayList<>();
        cart.add(new CartItemDTO(0, 0, 1));
        cart.add(new CartItemDTO(0, 1, 1));
        cart.add(new CartItemDTO(0, 2, 3));
    }

    @Test
    public void checkAddingDiscountToStore() throws Exception {
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountFalse1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountFalse1ID, "hila");

        List<ProductDataPrice> listProductDataPrices = discountPolicyFacade.calculatePrice(0, cart);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(16.2 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(32.4 , listProductDataPrices.get(2).getNewPrice());
    }

    @Test
    public void checkRemovingDiscountToStore() throws Exception {
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountFalse1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountFalse1ID, "hila");
        discountPolicyFacade.removeDiscountPolicyFromStore(0, onCategoryDairy10discountTrue1ID, "hila");
        List<ProductDataPrice> listProductDataPrices = discountPolicyFacade.calculatePrice(0, cart);
        assertEquals(90 , listProductDataPrices.get(0).getNewPrice());
        assertEquals(18 , listProductDataPrices.get(1).getNewPrice());
        assertEquals(36 , listProductDataPrices.get(2).getNewPrice());
    }

    @Test
    public void checkMultipleStore() throws Exception {
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountTrue1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onStore10discountFalse1ID, "hila");
        discountPolicyFacade.addDiscountPolicyToStore(0, onCategoryDairy10discountFalse1ID, "hila");
        discountPolicyFacade.removeDiscountPolicyFromStore(0, onCategoryDairy10discountTrue1ID, "hila");

        discountPolicyFacade.addDiscountPolicyToStore(1, onStore10discountTrue1ID, "maki");
        discountPolicyFacade.addDiscountPolicyToStore(1, onCategoryDairy10discountTrue1ID, "maki");
        discountPolicyFacade.addDiscountPolicyToStore(1, onStore10discountFalse1ID, "maki");
        discountPolicyFacade.addDiscountPolicyToStore(1, onCategoryDairy10discountFalse1ID, "maki");

        List<ProductDataPrice> listProductDataPricesA = discountPolicyFacade.calculatePrice(0, cart);
        List<ProductDataPrice> listProductDataPricesB = discountPolicyFacade.calculatePrice(1, cart);

        assertEquals(90 , listProductDataPricesA.get(0).getNewPrice());
        assertEquals(18 , listProductDataPricesA.get(1).getNewPrice());
        assertEquals(36 , listProductDataPricesA.get(2).getNewPrice());

        assertEquals(90 , listProductDataPricesB.get(0).getNewPrice());
        assertEquals(16.2 , listProductDataPricesB.get(1).getNewPrice());
        assertEquals(32.4 , listProductDataPricesB.get(2).getNewPrice());
    }

    @Test
    public void createOnProductSimpleDiscountDiscountSuccess() throws Exception {
        int policyId = discountPolicyFacade.createOnProductSimpleDiscountPolicy(10, 0, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new TrueCondition(9));
        expected.setOnProductID(0);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createOnCategorySimpleDiscountDiscountSuccess() throws Exception {
        int policyId = discountPolicyFacade.createOnCategorySimpleDiscountPolicy(10, "dairy", "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new TrueCondition(9));
        expected.setOnCategoryName("dairy");
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createOnStoreSimpleDiscountDiscountSuccess() throws Exception {
        int policyId = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new TrueCondition(9));
        expected.setOnStore();
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createOnProductConditionDiscountDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId = discountPolicyFacade.createOnProductConditionDiscountPolicy(10,0,1, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        expected.setOnProductID(0);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createOnProductConditionDiscountDiscountFail() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId = discountPolicyFacade.createOnProductConditionDiscountPolicy(10,0,1, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 90));
        expected.setOnProductID(0);
        String expectedJson = toJson(expected);

        assertNotEquals(expectedJson, resJson);
    }

    @Test
    public void createOnCategoryConditionDiscountDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId = discountPolicyFacade.createOnCategoryConditionDiscountPolicy(10,"dairy",1, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        expected.setOnCategoryName("dairy");
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }


    @Test
    public void createOnStoreConditionDiscountDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10,1, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount expected = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        expected.setOnStore();
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createOrDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createOrDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        OrDiscount expected = new OrDiscount(0, simpleDiscount1,simpleDiscount2);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createAndDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createAndDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        AndDiscount expected = new AndDiscount(0, simpleDiscount1,simpleDiscount2);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }
    @Test
    public void createTakeMinXorDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createTakeMinXorDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        XorDiscount expected = new XorDiscount(0, simpleDiscount1,simpleDiscount2);
        expected.setMin();
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createTakeMaxXorDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createTakeMaxXorDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        XorDiscount expected = new XorDiscount(0, simpleDiscount1,simpleDiscount2);
        expected.setMax();
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createAdditionDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createAdditionDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        AdditionDiscount expected = new AdditionDiscount(0, simpleDiscount1,simpleDiscount2);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }

    @Test
    public void createMaximumDiscountSuccess() throws Exception {
        //conditionId 1 is  MinBuyCondition(100);
        int policyId1 = discountPolicyFacade.createOnStoreConditionDiscountPolicy(10, 1, "hila");
        int policyId2 = discountPolicyFacade.createOnStoreSimpleDiscountPolicy(10, "hila");
        int policyId = discountPolicyFacade.createMaximumDiscountPolicy(policyId1,policyId2, "hila");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);

        String resJson = toJson(resDiscount);

        SimpleDiscount simpleDiscount1 = new SimpleDiscount(policyId, 10, new MinBuyCondition(0, 100));
        simpleDiscount1.setOnStore();
        SimpleDiscount simpleDiscount2 =  new SimpleDiscount(policyId, 10, new TrueCondition(0));
        simpleDiscount2.setOnStore();
        MaximumDiscount expected = new MaximumDiscount(0, simpleDiscount1,simpleDiscount2);
        String expectedJson = toJson(expected);

        assertEquals(expectedJson, resJson);
    }
}
