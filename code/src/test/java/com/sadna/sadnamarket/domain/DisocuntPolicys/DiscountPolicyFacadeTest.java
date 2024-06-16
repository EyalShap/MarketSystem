package com.sadna.sadnamarket.domain.DisocuntPolicys;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    List<CartItemDTO> cart;
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
}
