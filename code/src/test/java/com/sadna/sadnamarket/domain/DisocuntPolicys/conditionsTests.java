package com.sadna.sadnamarket.domain.DisocuntPolicys;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class conditionsTests extends DiscountPolicyTest{
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

    }
    @Test
    public void checkMinBuyConditionWhenTrue() throws Exception {
        int minBuyConditionID = conditionRepository.createMinBuyCondition(100);
        Condition minBuyCondition = conditionRepository.findConditionByID(minBuyConditionID);

        List<ProductDataPrice> listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(0,0, "eyal",1, 100,100));
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 1,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",1,40,40));

        Map<Integer, ProductDTO> cartMap = new HashMap<>();
        cartMap.put(0, productFacade.getProductDTO(0));
        cartMap.put(1, productFacade.getProductDTO(1));
        cartMap.put(2, productFacade.getProductDTO(2));

        assertTrue(minBuyCondition.checkCond(cartMap, listProductDataPrices));
    }

    @Test
    public void checkMinBuyConditionWhenFalse() throws Exception {
        int minBuyConditionID = conditionRepository.createMinBuyCondition(100);
        Condition minBuyCondition = conditionRepository.findConditionByID(minBuyConditionID);

        List<ProductDataPrice> listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 1,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",1,40,40));

        Map<Integer, ProductDTO> cartMap = new HashMap<>();
        cartMap.put(0, productFacade.getProductDTO(0));
        cartMap.put(1, productFacade.getProductDTO(1));
        cartMap.put(2, productFacade.getProductDTO(2));

        assertFalse(minBuyCondition.checkCond(cartMap, listProductDataPrices));
    }

    @Test
    public void checkMinAmountConditionWhenTrue() throws Exception {
        int minBuyConditionID1 = conditionRepository.createMinProductCondition(3, 2);
        Condition minBuyCondition1 = conditionRepository.findConditionByID(minBuyConditionID1);
        int minBuyConditionID2 = conditionRepository.createMinProductCondition(5, "dairy");
        Condition minBuyCondition2 = conditionRepository.findConditionByID(minBuyConditionID2);
        int minBuyConditionID3 = conditionRepository.createMinProductOnStoreCondition(6);
        Condition minBuyCondition3 = conditionRepository.findConditionByID(minBuyConditionID3);

        List<ProductDataPrice> listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(0,0, "eyal",1, 100,100));
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 2,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",3,40,40));

        Map<Integer, ProductDTO> cartMap = new HashMap<>();
        cartMap.put(0, productFacade.getProductDTO(0));
        cartMap.put(1, productFacade.getProductDTO(1));
        cartMap.put(2, productFacade.getProductDTO(2));

        assertTrue(minBuyCondition1.checkCond(cartMap, listProductDataPrices));
        assertTrue(minBuyCondition2.checkCond(cartMap, listProductDataPrices));
        assertTrue(minBuyCondition3.checkCond(cartMap, listProductDataPrices));

    }

    @Test
    public void checkMinAmountConditionWhenFalse() throws Exception {
        int minBuyConditionID1 = conditionRepository.createMinProductCondition(4, 2);
        Condition minBuyCondition1 = conditionRepository.findConditionByID(minBuyConditionID1);
        int minBuyConditionID2 = conditionRepository.createMinProductCondition(6, "dairy");
        Condition minBuyCondition2 = conditionRepository.findConditionByID(minBuyConditionID2);
        int minBuyConditionID3 = conditionRepository.createMinProductOnStoreCondition(7);
        Condition minBuyCondition3 = conditionRepository.findConditionByID(minBuyConditionID3);

        List<ProductDataPrice> listProductDataPrices = new ArrayList<>();
        listProductDataPrices.add(new ProductDataPrice(0,0, "eyal",1, 100,100));
        listProductDataPrices.add(new ProductDataPrice(1,0, "milk", 2,20,20));
        listProductDataPrices.add(new ProductDataPrice(2,0, "cheese",3,40,40));

        Map<Integer, ProductDTO> cartMap = new HashMap<>();
        cartMap.put(0, productFacade.getProductDTO(0));
        cartMap.put(1, productFacade.getProductDTO(1));
        cartMap.put(2, productFacade.getProductDTO(2));

        assertFalse(minBuyCondition1.checkCond(cartMap, listProductDataPrices));
        assertFalse(minBuyCondition2.checkCond(cartMap, listProductDataPrices));
        assertFalse(minBuyCondition3.checkCond(cartMap, listProductDataPrices));
    }
}
