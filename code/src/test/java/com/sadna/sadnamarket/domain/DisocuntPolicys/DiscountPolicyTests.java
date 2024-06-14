package com.sadna.sadnamarket.domain.DisocuntPolicys;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.IConditionRespository;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MemoryConditionRepository;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.IDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.MemoryDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.users.MemoryRepo;
import com.sadna.sadnamarket.domain.users.UserFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class DiscountPolicyTests {

    private IConditionRespository conditionRepository;
    private IDiscountPolicyRepository discountPolicyRepository;

    protected AuthFacade authFacade;
    private ProductFacade productFacade;

    private StoreFacade storeFacade;
    private DiscountPolicyFacade discountPolicyFacade;


    @BeforeEach
    public void setUp() {
        conditionRepository = new MemoryConditionRepository();
        discountPolicyRepository = new MemoryDiscountPolicyRepository();
        productFacade = new ProductFacade();
        storeFacade = new StoreFacade(new MemoryStoreRepository());
        OrderFacade orderFacade = new OrderFacade(new MemoryOrderRepository());
        UserFacade userFacade = new UserFacade(new MemoryRepo(), storeFacade, orderFacade);
        authFacade = new AuthFacade(new AuthRepositoryMemoryImpl(), userFacade);

        this.discountPolicyFacade.setProductFacade(productFacade);
        this.discountPolicyFacade.setStoreFacade(storeFacade);

        this.storeFacade.setUserFacade(userFacade);
        this.storeFacade.setOrderFacade(orderFacade);
        this.storeFacade.setProductFacade(productFacade);
        this.storeFacade.setDiscountPolicyFacade(discountPolicyFacade);

        storeFacade.createStore("WillyTheChocolateDude", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403");


    }

    @Test
    public void checkMinBuyCondition() throws Exception {
        int minBuyConditionID = conditionRepository.createMinBuyCondition(100);
        Condition minBuyCondition = conditionRepository.findConditionByID(minBuyConditionID);
        int discountOnStoreID = discountPolicyRepository.addOnStoreSimpleDiscount(50, minBuyCondition);
        discountPolicyFacade.addDiscountPolicyToStore(1,discountOnStoreID, "perry");
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
