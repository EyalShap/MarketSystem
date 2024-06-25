package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.buyPolicies.*;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MemoryConditionRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MinProductCondition;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.Discount;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.MemoryDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.SimpleDiscount;
import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.orders.*;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.products.IProductRepository;
import com.sadna.sadnamarket.domain.products.MemoryProductRepository;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.domain.users.*;
import com.sadna.sadnamarket.service.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.access.method.P;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class DBTest {
    private StoreFacade storeFacade;
    private AuthFacade authFacade;
    private UserFacade userFacade;
    private ProductFacade productFacade;
    private BuyPolicyFacade buyPolicyFacade;
    private DiscountPolicyFacade discountPolicyFacade;
    private OrderFacade orderFacade;
    ObjectMapper objectMapper = new ObjectMapper();
    private SimpleFilterProvider idFilter;
    private IStoreRepository storeRepo;
    private int storeId0;
    private int storeId1;

    @BeforeEach
    public void setUp() {
        this.idFilter = new SimpleFilterProvider().addFilter("idFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
        objectMapper.registerModule(new JavaTimeModule());

        setUpFacades();
        registerUsers();
        //generateStore0();
        //generateStore1();
    }

    private void setUpFacades() {
        IOrderRepository orderRepo = new MemoryOrderRepository();
        this.orderFacade = new OrderFacade(orderRepo);

        this.storeRepo = new HibernateStoreRepository();
        this.storeFacade = new StoreFacade(storeRepo);

        IUserRepository userRepo = new MemoryRepo();
        this.userFacade = new UserFacade(userRepo, storeFacade, orderFacade);

        IProductRepository productRepo = new MemoryProductRepository();
        this.productFacade = new ProductFacade(productRepo);

        this.buyPolicyFacade = new BuyPolicyFacade(new MemoryBuyPolicyRepository());
        this.discountPolicyFacade = new DiscountPolicyFacade(new MemoryConditionRepository(), new MemoryDiscountPolicyRepository());

        this.storeFacade.setUserFacade(userFacade);
        this.storeFacade.setOrderFacade(orderFacade);
        this.storeFacade.setProductFacade(productFacade);
        this.storeFacade.setBuyPolicyFacade(buyPolicyFacade);
        this.storeFacade.setDiscountPolicyFacade(discountPolicyFacade);

        this.authFacade = new AuthFacade(new AuthRepositoryMemoryImpl(), userFacade);
        this.buyPolicyFacade.setStoreFacade(storeFacade);
        this.buyPolicyFacade.setUserFacade(userFacade);
        this.buyPolicyFacade.setProductFacade(productFacade);

        this.discountPolicyFacade.setStoreFacade(storeFacade);
        this.discountPolicyFacade.setProductFacade(productFacade);

        this.orderFacade.setStoreFacade(storeFacade);
    }

    private void registerUsers() {
        authFacade.register("Mr. Krabs", "654321", "Eugene", "Krabs", "eugene@gmail.com", "0521957682", LocalDate.of(1942, 11, 30));
        authFacade.login("Mr. Krabs", "654321");
        authFacade.register("WillyTheChocolateDude", "123456", "Willy", "Wonka", "willy@gmail.com", "0541095600", LocalDate.of(1995, 3, 8));
        authFacade.login("WillyTheChocolateDude", "123456");
        authFacade.register("Bob", "24680", "Bob", "Cohen", "bob@gmail.com", "0544219674", LocalDate.of(2001, 12, 1));
        authFacade.login("Bob", "24680");
        authFacade.register("Alice", "08642", "Alice", "Levi", "alice@gmail.com", "0523176455", LocalDate.of(2004, 1, 18));
    }

    private void generateStore0() {
        this.storeId0 = storeFacade.createStore("WillyTheChocolateDude", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403");
    }

    private void generateStore1() {
        this.storeId1 = storeFacade.createStore("Mr. Krabs", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");
    }

    @Test
    void createStoreSuccess() {
        //storeFacade.setStoreBankAccount("WillyTheChocolateDude", storeId0, new BankAccountDTO("123", "123", "123", "WillyTheChocolateDude"));
        //storeFacade.setStoreBankAccount("Mr. Krabs", storeId1, new BankAccountDTO("123", "123", "123", "Mr. Krabs"));
        //storeFacade.addProductToStore("WillyTheChocolateDude", 5, "Banana", 9, 4, "Fruit", 2, 5);
        //storeFacade.addProductToStore("Mr. Krabs", 6, "Banana", 12, 4, "Fruit", 2, 5);
        storeFacade.addProductToStore("WillyTheChocolateDude", 5, "Apple", 9, 4, "Fruit", 2, 5);

    }

}