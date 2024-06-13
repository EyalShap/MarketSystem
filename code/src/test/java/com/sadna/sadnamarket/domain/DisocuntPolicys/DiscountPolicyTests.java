package com.sadna.sadnamarket.domain.DisocuntPolicys;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.sadna.sadnamarket.domain.discountPolicies.Conditions.IConditionRespository;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MemoryConditionRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.IDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.MemoryDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class DiscountPolicyTests {

    private IConditionRespository conditionRepository;
    private IDiscountPolicyRepository discountPolicyRepository;

    private ProductFacade productFacade;

    private StoreFacade storeFacade;


    @Before
    public void setUp() {
        conditionRepository = new MemoryConditionRepository();
        discountPolicyRepository = new MemoryDiscountPolicyRepository();
        productFacade = new ProductFacade();
        storeFacade = new StoreFacade(new MemoryStoreRepository());
    }

    @Test
    public void checkMinBuyCondition(){

    }
    @Test
    public void checkMinProductCondition(){

    }


    @Test
    public void checkTrueCondition(){

    }

    @Test
    public void checkOrCondition(){

    }

    @Test
    public void checkXorCondition(){

    }

    @Test
    public void checkAndCondition(){

    }

    @Test
    public void checkSimpleDiscount(){

    }
    @Test
    public void checkMaximumDiscount(){

    }


    @Test
    public void checkAdditionDiscount(){

    }

    @Test
    public void checkOrDiscount(){

    }

    @Test
    public void checkXorDiscount(){

    }

    @Test
    public void checkAndDiscount(){

    }

    @Test
    public void checkAddingDiscountToStore(){

    }

    @Test
    public void checkRemovingDiscountToStore(){

    }

    @Test
    public void checkCalculateDiscountToStore(){

    }
}
