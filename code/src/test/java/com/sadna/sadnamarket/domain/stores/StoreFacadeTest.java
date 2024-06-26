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

class StoreFacadeTest {
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

    @BeforeEach
    public void setUp() {
        this.idFilter = new SimpleFilterProvider().addFilter("idFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
        objectMapper.registerModule(new JavaTimeModule());

        setUpFacades();
        registerUsers();
        generateStore0();
        addPermissions();
        addProducts0();
    }

    private void setUpFacades() {
        IOrderRepository orderRepo = new MemoryOrderRepository();
        this.orderFacade = new OrderFacade(orderRepo);

        this.storeRepo = new MemoryStoreRepository();
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

    private void addPermissions() {
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Mr. Krabs", 0);
        userFacade.accept("Mr. Krabs", 1);

        Set<Permission> permissions = new HashSet<>();
        Collections.addAll(permissions, Permission.ADD_PRODUCTS,
                Permission.DELETE_PRODUCTS,
                Permission.UPDATE_PRODUCTS,
                Permission.CLOSE_STORE,
                Permission.REOPEN_STORE,
                Permission.ADD_OWNER,
                Permission.ADD_MANAGER,
                Permission.ADD_BUY_POLICY,
                Permission.ADD_DISCOUNT_POLICY,
                Permission.VIEW_ROLES
        );
        storeFacade.addManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 0, permissions);
    }

    private void addProducts0() {
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Shokolad Parah", 1000, 5.5, "Chocolate", 4,2);
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Kif Kef", 982, 4.2, "Chocolate", 4.3,2);
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Klik Cariot", 312, 7, "Chocolate", 5, 2);
    }

    private void addProducts1() {
        storeFacade.addProductToStore("Mr. Krabs", 1, "Beer", 1000, 12, "Alcohol", 4,1);
        storeFacade.addProductToStore("Mr. Krabs", 1, "Wine", 1000, 50, "Alcohol", 4.7,1);
    }

    private StoreInfo generateStore0Info() {
        return new StoreInfo("Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403");
    }

    private StoreInfo generateStore1Info() {
        return new StoreInfo("Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");
    }

    private Store generateStoreObject0() {
        Store s = new Store(0, "WillyTheChocolateDude", generateStore0Info());
        s.addProduct(0, 1000);
        s.addProduct(1, 982);
        s.addProduct(2, 312);
        s.addStoreManager("Mr. Krabs");
        return s;
    }

    private Store generateStoreObject1() {
        return new Store(1, "Mr. Krabs", generateStore1Info());
    }

    private void generateStore0() {
        storeFacade.createStore("WillyTheChocolateDude", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403");
    }

    private void generateStore1() throws JsonProcessingException {
        storeFacade.createStore("Mr. Krabs", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");
        addProducts1();
        int policyId1 = buyPolicyFacade.createCategoryHolidayBuyPolicy("Chocolate", List.of(BuyType.immidiatePurchase), "WillyTheChocolateDude");
        int policyId2 = buyPolicyFacade.createProductAmountBuyPolicy(2, List.of(BuyType.immidiatePurchase), 3, 10, "WillyTheChocolateDude");
        int policyId3 = buyPolicyFacade.createOrBuyPolicy(policyId1, policyId2, "WillyTheChocolateDude");
        buyPolicyFacade.addBuyPolicyToStore("WillyTheChocolateDude", 0, policyId3);
    }

    @Test
    void createStoreSuccess() {
        Set<Integer> expected0 = new HashSet<>();
        Set<Integer> expected1 = new HashSet<>();
        Collections.addAll(expected0, 0);
        Collections.addAll(expected1, 0, 1);

        assertEquals(expected0, storeFacade.getAllStoreIds());

        storeFacade.createStore("Mr. Krabs", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");

        assertEquals(expected1, storeFacade.getAllStoreIds());

        assertEquals(generateStoreObject0().getStoreDTO(), storeFacade.getStoreInfo("WillyTheChocolateDude", 0));
        assertEquals(generateStoreObject1().getStoreDTO(), storeFacade.getStoreInfo("Mr. Krabs", 1));
    }

    @Test
    void createStoreNoUser() {
        NoSuchElementException expected = assertThrows(NoSuchElementException.class, () -> {
            storeFacade.createStore("Dana", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");
        });

        String expectedMessage = Error.makeMemberUserDoesntExistError("Dana");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreUserNotLoggedIn() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Alice", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120");
        });

        String expectedMessage = Error.makeStoreUserHasToBeLoggedInError("Alice");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreAlreadyExists() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Mr. Krabs", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403");
        });

        String expectedMessage = Error.makeStoreWithNameAlreadyExistsError("Chocolate Factory");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreParamsNotValid() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Mr. Krabs", "", "", "", "");
        });

        String expectedMessage = Error.makeStoreNotValidAspectError("", "store name");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void addProductToStoreSuccess_Owner() {
        int productId = storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        assertEquals(409, storeFacade.getProductAmount(0, productId));

        ProductDTO expected = new ProductDTO(productId, "Mekupelet", 3.5, "Chocolate", 4.2, 2,true,0);
        assertEquals(expected, productFacade.getProductDTO(productId));
    }

    @Test
    void addProductToStoreSuccess_ManagerWithPermissions() {
        int productId = storeFacade.addProductToStore("Mr. Krabs", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        assertEquals(409, storeFacade.getProductAmount(0, productId));

        ProductDTO expected = new ProductDTO(productId, "Mekupelet", 3.5, "Chocolate", 4.2, 2, true,0);
        assertEquals(expected, productFacade.getProductDTO(productId));
    }

    @Test
    void addProductToStore_StoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("WillyTheChocolateDude", 400, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });

        assertEquals(Error.makeStoreNoStoreWithIdError(400), expected.getMessage());
    }

    @Test
    void addProductToStore_StoreNotActive() {
        storeFacade.closeStore("WillyTheChocolateDude", 0);
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });

        assertEquals(Error.makeStoreWithIdNotActiveError(0), expected.getMessage());
    }

    @Test
    void addProductToStore_ParamsNotValid() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("WillyTheChocolateDude", 0, "", -57, -3.5, "", 8,2);
        });

        String expectedError = "Product information is invalid: Product name cannot be null or empty. Product price cannot be negative. Product category cannot be null or empty. Product rank must be between 0 and 5. ";
        assertEquals(expectedError, expected.getMessage());
    }

    @Test
    void addProductToStore_NoPermissions() {
        storeFacade.removeManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 0, Permission.ADD_PRODUCTS);
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("Mr. Krabs", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });
        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("Bob", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });
        IllegalArgumentException expected3 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addProductToStore("Alice", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });
        NoSuchElementException expected4 = assertThrows(NoSuchElementException.class, () -> {
            storeFacade.addProductToStore("Dana", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        });
        assertEquals(Error.makeStoreUserCannotAddProductError("Mr. Krabs", 0), expected1.getMessage());
        assertEquals(Error.makeStoreUserCannotAddProductError("Bob", 0), expected2.getMessage());
        assertEquals(Error.makeStoreUserCannotAddProductError("Alice", 0), expected3.getMessage());
        assertEquals(Error.makeMemberUserDoesntExistError("Dana"), expected4.getMessage());
    }

    @Test
    void deleteProductSuccess() {
        storeFacade.deleteProduct("WillyTheChocolateDude", 0, 0);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductAmount(0, 0);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0, 0), expected1.getMessage());
        assertFalse(productFacade.getProductDTO(0).isActiveProduct());


        storeFacade.deleteProduct("Mr. Krabs", 0, 1);

        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductAmount(0, 1);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0, 1), expected2.getMessage());
        assertFalse(productFacade.getProductDTO(1).isActiveProduct());
    }

    @Test
    void deleteProduct_StoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.deleteProduct("WillyTheChocolateDude", 5, 0);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(5), expected1.getMessage());
        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void deleteProduct_ProductDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.deleteProduct("WillyTheChocolateDude", 0, 4);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0,4), expected1.getMessage());
    }

    @Test
    void deleteProduct_NoPermission() {
        storeFacade.removeManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 0, Permission.DELETE_PRODUCTS);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.deleteProduct("Mr. Krabs", 0, 0);
        });
        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.deleteProduct("Bob", 0, 0);
        });
        IllegalArgumentException expected3 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.deleteProduct("Alice", 0, 0);
        });
        assertEquals(Error.makeStoreUserCannotDeleteProductError("Mr. Krabs", 0), expected1.getMessage());
        assertEquals(Error.makeStoreUserCannotDeleteProductError("Bob", 0), expected2.getMessage());
        assertEquals(Error.makeStoreUserCannotDeleteProductError("Alice", 0), expected3.getMessage());
        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void updateProductSuccess() {
        storeFacade.updateProduct("WillyTheChocolateDude", 0, 0, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        ProductDTO expected1 = new ProductDTO(0, "Cow Chocolate", 10, "Chocolate", 4.8, 2,true,0);
        assertEquals(expected1, productFacade.getProductDTO(0));

        storeFacade.updateProduct("Mr. Krabs", 0, 1, "Kit Kat", 100, 8, "Chocolate", 4.21);
        ProductDTO expected2 = new ProductDTO(1, "Kit Kat", 8, "Chocolate", 4.21, 2,true,0);
        assertEquals(expected2, productFacade.getProductDTO(1));
    }

    @Test
    void updateProduct_StoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProduct("WillyTheChocolateDude", 5, 0, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(5), expected1.getMessage());
    }

    @Test
    void updateProduct_ProductDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProduct("WillyTheChocolateDude", 0, 10, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0,10), expected1.getMessage());
    }

    @Test
    void updateProduct_NoPermission() {
        storeFacade.removeManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 0, Permission.UPDATE_PRODUCTS);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProduct("Mr. Krabs", 0, 0, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        });
        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProduct("Alice", 0, 0, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        });
        IllegalArgumentException expected3 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProduct("Bob", 0, 0, "Cow Chocolate", 123, 10, "Chocolate", 4.8);
        });
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Mr. Krabs", 0), expected1.getMessage());
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Alice", 0), expected2.getMessage());
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Bob", 0), expected3.getMessage());
    }

    @Test
    void updateProductAmountSuccess() {
        storeFacade.updateProductAmount("WillyTheChocolateDude", 0, 0, 12);
        storeFacade.updateProductAmount("Mr. Krabs", 0, 1, 200);
        assertEquals(12, storeFacade.getProductAmount( 0, 0));
        assertEquals(200, storeFacade.getProductAmount( 0, 1));
    }

    @Test
    void updateProductAmount_StoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("WillyTheChocolateDude", 50, 0, 12);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(50), expected1.getMessage());

        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void updateProductAmount_ProductDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("WillyTheChocolateDude", 0, 50, 12);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0, 50), expected1.getMessage());

        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void updateProductAmount_NegAmount() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("WillyTheChocolateDude", 0, 0, -903);
        });
        assertEquals(Error.makeStoreIllegalProductAmountError(-903), expected1.getMessage());

        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void updateProductAmount_NoPermissions() {
        storeFacade.removeManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 0, Permission.UPDATE_PRODUCTS);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("Mr. Krabs", 0, 0, 5);
        });
        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("Bob", 0, 0, 5);
        });
        IllegalArgumentException expected3 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.updateProductAmount("Alice", 0, 0, 5);
        });
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Mr. Krabs", 0), expected1.getMessage());
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Bob", 0), expected2.getMessage());
        assertEquals(Error.makeStoreUserCannotUpdateProductError("Alice", 0), expected3.getMessage());
        assertEquals(1000, storeFacade.getProductAmount( 0, 0));
    }

    @Test
    void sendStoreOwnerRequestSuccess() {
        storeFacade.sendStoreOwnerRequest("WillyTheChocolateDude", "Bob", 0);
        userFacade.accept("Bob", 1);

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "WillyTheChocolateDude", "Bob");
        List<MemberDTO> res = storeFacade.getOwners("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void sendStoreOwnerRequestAlreadyOwner() {
        storeFacade.sendStoreOwnerRequest("WillyTheChocolateDude", "Bob", 0);
        userFacade.accept("Bob", 1);
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.sendStoreOwnerRequest("WillyTheChocolateDude", "Bob", 0);
        });
        assertEquals(Error.makeStoreUserAlreadyOwnerError("Bob", 0), expected.getMessage());
    }

    @Test
    void sendStoreOwnerRequestNotAccepted() {
        storeFacade.sendStoreOwnerRequest("WillyTheChocolateDude", "Bob", 0);

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "WillyTheChocolateDude");
        List<MemberDTO> res = storeFacade.getOwners("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void sendStoreOwnerRequestNoUser() {
        NoSuchElementException expected = assertThrows(NoSuchElementException.class, () -> {
            storeFacade.sendStoreOwnerRequest("WillyTheChocolateDude", "Dana", 0);
        });

        assertEquals(Error.makeMemberUserDoesntExistError("Dana"), expected.getMessage());
    }

    @Test
    void sendStoreManagerRequestSuccess() {
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Bob", 0);
        userFacade.accept("Bob", 1);

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "Mr. Krabs", "Bob");
        List<MemberDTO> res = storeFacade.getManagers("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void sendStoreManagerRequestAlreadyManager() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreUserAlreadyManagerError("Mr. Krabs", 0), expected.getMessage());
    }

    @Test
    void sendStoreManagerRequestNotAccepted() {
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Bob", 0);

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "Mr. Krabs");
        List<MemberDTO> res = storeFacade.getManagers("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void addManagerPermissionSuccess() {
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Bob", 0);
        userFacade.accept("Bob", 1);
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.ADD_PRODUCTS);
        storeFacade.addManagerPermission("WillyTheChocolateDude", "Bob", 0, permissions);
        assertDoesNotThrow(() -> {
            storeFacade.addProductToStore("Bob", 0, "Nutella", 10, 12, "Chocolate", 4.8,2);
        });
    }

    @Test
    void addManagerPermissionNotManager() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.ADD_PRODUCTS);
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addManagerPermission("WillyTheChocolateDude", "Bob", 0, permissions);
        });
        assertEquals(Error.makeMemberUserHasNoRoleError(), expected.getMessage());
    }

    @Test
    void addManagerPermissionUserCanNotAssign() {
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Bob", 0);
        userFacade.accept("Bob", 1);
        storeFacade.sendStoreManagerRequest("WillyTheChocolateDude", "Alice", 0);
        userFacade.accept("Alice", 1);
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.ADD_PRODUCTS);
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addManagerPermission("Alice", "Bob", 0, permissions);
        });
        assertEquals(Error.makeStoreUserCannotAddManagerPermissionsError("Alice", "Bob", 0), expected.getMessage());
    }

    @Test
    void addManagerPermissionStoreDoesNotExist() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.ADD_PRODUCTS);
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.addManagerPermission("WillyTheChocolateDude", "Mr. Krabs", 3, permissions);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(3), expected.getMessage());
    }

    @Test
    void closeStore_Success() {
        assertTrue(storeFacade.getStoreInfo("WillyTheChocolateDude", 0).isActive());
        storeFacade.closeStore("WillyTheChocolateDude", 0);
        assertFalse(storeFacade.getStoreInfo("WillyTheChocolateDude", 0).isActive());
    }

    @Test
    void closeStore_StoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.closeStore("WillyTheChocolateDude", 9);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(9), expected1.getMessage());
    }

    @Test
    void closeStore_AlreadyClosed() {
        storeFacade.closeStore("WillyTheChocolateDude", 0);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.closeStore("WillyTheChocolateDude", 0);
        });
        assertEquals(Error.makeStoreAlreadyClosedError(0), expected1.getMessage());
    }

    @Test
    void closeStore_NoPermissions() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.closeStore("Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreUserCannotCloseStoreError("Mr. Krabs", 0), expected1.getMessage());
    }

    @Test
    void getOwnersSuccess() {
        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "WillyTheChocolateDude");
        List<MemberDTO> res = storeFacade.getOwners("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void getOwnersNotOwner() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getOwners("Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreUserCannotGetRolesInfoError("Mr. Krabs", 0), expected1.getMessage());
    }

    @Test
    void getOwnersStoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getOwners("WillyTheChocolateDude", 7);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(7), expected1.getMessage());
    }

    @Test
    void getManagersSuccess() {
        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, "Mr. Krabs");
        List<MemberDTO> res = storeFacade.getManagers("WillyTheChocolateDude", 0);
        Set<String> resUsernames = new HashSet<>();
        for(MemberDTO memberDTO : res)
            resUsernames.add(memberDTO.getUsername());
        assertEquals(expected, resUsernames);
    }

    @Test
    void getManagersNotOwner() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getManagers("Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreUserCannotGetRolesInfoError("Mr. Krabs", 0), expected1.getMessage());
    }

    @Test
    void getManagersStoreDoesNotExist() {
        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getManagers("WillyTheChocolateDude", 7);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(7), expected1.getMessage());
    }

    @Test
    void getStoreOrderHistory() throws Exception {
        SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
        PaymentInterface paymentMock = Mockito.mock(PaymentInterface.class);
        PaymentService.getInstance().setController(paymentMock);
        SupplyService.getInstance().setController(supplyMock);
        Mockito.when(supplyMock.canMakeOrder(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(supplyMock.makeOrder(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(paymentMock.creditCardValid(Mockito.any())).thenReturn(true);
        Mockito.when(paymentMock.pay(Mockito.anyDouble(), Mockito.any(), Mockito.any())).thenReturn(true);

        List<OrderDTO> expected = new ArrayList<>();
        Map<Integer, Integer> amounts = new HashMap<>();
        amounts.put(0, 5);
        amounts.put(1, 15);
        amounts.put(2, 12);
        List<ProductDataPrice> products = new ArrayList<>();
        products.add(new ProductDataPrice(0, 0, "Shokolad Parah", 5, 5.5, 5.5));
        products.add( new ProductDataPrice(1, 0, "Kif Kef", 15, 4.2, 4.2));
        products.add(new ProductDataPrice(2, 0, "Klik Cariot", 12, 7, 7));
        //expected.add(new OrderDTO("Mr. Krabs", "Chocolate Factory", amounts, products));

        userFacade.addProductToCart("Mr. Krabs", 0, 0, 5);
        userFacade.addProductToCart("Mr. Krabs", 0, 1, 15);
        userFacade.addProductToCart("Mr. Krabs", 0, 2, 12);

        CreditCardDTO creditCard = new CreditCardDTO("123", "456", new Date(), "291845322");
        AddressDTO address = new AddressDTO("Israel", "Tel Aviv", "A", "B", "123", "Mr. Krabs", "0541762645", "krab@gmail.com");
        userFacade.purchaseCart("Mr. Krabs", creditCard, address);

        List<ProductDataPrice> history = storeFacade.getStoreOrderHistory("WillyTheChocolateDude", 0);
        assertEquals(products, history);
    }

    @Test
    void getStoreInfoSuccess() {
        assertEquals(generateStoreObject0().getStoreDTO(), storeFacade.getStoreInfo(null, 0));
    }

    @Test
    void getStoreInfoStoreClosed() {
        storeFacade.closeStore("WillyTheChocolateDude", 0);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getStoreInfo(null, 0);
        });
        assertEquals(Error.makeStoreWithIdNotActiveError(0), expected1.getMessage());

        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getStoreInfo("Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreWithIdNotActiveError(0), expected2.getMessage());

        StoreDTO expected = generateStoreObject0().getStoreDTO();
        expected.setActive(false);
        StoreDTO res = storeFacade.getStoreInfo("WillyTheChocolateDude", 0);
        assertEquals(expected, res);
    }

    @Test
    void getStoreInfoStoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getStoreInfo("WillyTheChocolateDude", 50);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(50), expected.getMessage());
    }

    @Test
    void getProductInfoSuccess() {
        ProductDTO expected = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true,0);
        assertEquals(expected, storeFacade.getProductInfo("WillyTheChocolateDude", 0));
    }

    @Test
    void getProductInfoStoreClosed() {
        storeFacade.closeStore("WillyTheChocolateDude", 0);

        IllegalArgumentException expected1 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductInfo(null, 0);
        });
        assertEquals(Error.makeStoreOfProductIsNotActiveError(0), expected1.getMessage());

        IllegalArgumentException expected2 = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductInfo("Mr. Krabs", 0);
        });
        assertEquals(Error.makeStoreOfProductIsNotActiveError(0), expected2.getMessage());

        ProductDTO expected = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true,0);
        ProductDTO res = storeFacade.getProductInfo("WillyTheChocolateDude", 0);
        assertEquals(expected, res);
    }

    @Test
    void getProductInfoProductDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductInfo("WillyTheChocolateDude", 50);
        });
        assertEquals(Error.makeProductDoesntExistError(50), expected.getMessage());
    }

    @Test
    void getProductsInfoNotFiltered() throws JsonProcessingException {
        ProductDTO product0 = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true,0);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true,0);
        ProductDTO product2 = new ProductDTO(2, "Klik Cariot", 7, "Chocolate", 5, 2,true,0);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product0, 1000);
        expected.put(product1, 982);
        expected.put(product2, 312);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, null, null, -1, -1));
    }

    @Test
    void getProductsInfoFiltered() throws JsonProcessingException {
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true,0);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product1, 982);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, "Kif Kef", null, 5, -1));
    }

    @Test
    void getProductsInfoDeleteProduct() throws JsonProcessingException {
        storeFacade.deleteProduct("WillyTheChocolateDude", 0, 0);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true,0);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product1, 982);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, null, null, 6, -1));
    }

    @Test
    void getProductsInfoStoreClosed() throws JsonProcessingException {
        storeFacade.closeStore("WillyTheChocolateDude", 0);
        ProductDTO product0 = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true,0);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true,0);
        ProductDTO product2 = new ProductDTO(2, "Klik Cariot", 7, "Chocolate", 5, 2,true,0);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product0, 1000);
        expected.put(product1, 982);
        expected.put(product2, 312);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter("WillyTheChocolateDude", 0, null, null, -1, -1));

        IllegalArgumentException expectedError = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductsInfoAndFilter(null, 0, null, null, -1, -1);
        });
        assertEquals(Error.makeStoreWithIdNotActiveError(0), expectedError.getMessage());
    }

    @Test
    void getProductAmountSuccess() {
        assertEquals(1000, storeFacade.getProductAmount(0, 0));
        assertEquals(982, storeFacade.getProductAmount(0, 1));
        assertEquals(312, storeFacade.getProductAmount(0, 2));
    }

    @Test
    void getProductAmountStoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductAmount(5, 0);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(5), expected.getMessage());
    }

    @Test
    void getProductAmountProductDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getProductAmount(0, 7);
        });
        assertEquals(Error.makeStoreProductDoesntExistError(0, 7), expected.getMessage());
    }

    @Test
    void addBuyPolicy() throws Exception {
        List<BuyType> buyTypes = List.of(BuyType.immidiatePurchase);
        int policyId = buyPolicyFacade.createCategoryHolidayBuyPolicy("Fruits", List.of(BuyType.immidiatePurchase), "WillyTheChocolateDude");
        buyPolicyFacade.addBuyPolicyToStore("WillyTheChocolateDude", 0, policyId);

        BuyPolicy resBuyPolicy = buyPolicyFacade.getBuyPolicy(policyId);
        BuyPolicy expectedBuyPolicy = new HolidayBuyPolicy(policyId, buyTypes, new CategorySubject("Fruits"));

        assertEquals(expectedBuyPolicy, resBuyPolicy);
    }

    @Test
    void addDiscountPolicy() throws Exception {
        int conditionId = discountPolicyFacade.createMinProductCondition(10, 0, "WillyTheChocolateDude");
        int policyId = discountPolicyFacade.createOnProductConditionDiscountPolicy(50, 0, conditionId,"WillyTheChocolateDude");

        Discount resDiscount = discountPolicyFacade.getDiscountPolicy(policyId);
        MinProductCondition expectedCondition = new MinProductCondition(conditionId, 10);
        expectedCondition.setOnProductName(0); // set product id
        SimpleDiscount expectedDiscount = new SimpleDiscount(resDiscount.getId(), 50, expectedCondition);
        expectedDiscount.setOnProductID(0);
        String discountJson = objectMapper.writeValueAsString(resDiscount);
        String expectedJson = objectMapper.writeValueAsString(expectedDiscount);
        assertEquals(expectedJson, discountJson);
    }

    @Test
    void checkCartOnHoliday() throws JsonProcessingException {
        try (MockedStatic<HolidayBuyPolicy> mocked1 = mockStatic(HolidayBuyPolicy.class)) {
            mocked1.when(HolidayBuyPolicy::isHoliday).thenReturn(true);

            generateStore1(); // holiday -> amount(2, 3-10)

            List<CartItemDTO> cart1 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 0, 10));
            cart1.add(new CartItemDTO(0, 2, 8));
            assertDoesNotThrow(() -> {storeFacade.checkCart(null, cart1);});

            List<CartItemDTO> cart2 = new ArrayList<>();
            cart2.add(new CartItemDTO(0, 0, 10));
            cart2.add(new CartItemDTO(0, 2, 18));
            IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
                storeFacade.checkCart(null, cart2);
            });
            BuyPolicy policy1 = buyPolicyFacade.getBuyPolicy(2);
            BuyPolicy policy2 = buyPolicyFacade.getBuyPolicy(3);
            assertEquals(Error.makeOrBuyPolicyError(policy1.getErrorDescription(), policy2.getErrorDescription()), expected.getMessage());
        }
    }

    @Test
    void checkCartNotHoliday() throws JsonProcessingException {
        try (MockedStatic<HolidayBuyPolicy> mocked1 = mockStatic(HolidayBuyPolicy.class)) {
            mocked1.when(HolidayBuyPolicy::isHoliday).thenReturn(false);

            generateStore1(); // holiday -> amount(2, 3-10)

            List<CartItemDTO> cart1 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 0, 10));
            cart1.add(new CartItemDTO(0, 2, 18));
            assertDoesNotThrow(() -> {storeFacade.checkCart(null, cart1);});
        }
    }

    @Test
    void checkCartOnHolidayWithAlcohol() throws JsonProcessingException {
        try (MockedStatic<HolidayBuyPolicy> mocked1 = mockStatic(HolidayBuyPolicy.class);
             MockedStatic<HourLimitBuyPolicy> mocked2 = mockStatic(HourLimitBuyPolicy.class)) {
            mocked1.when(HolidayBuyPolicy::isHoliday).thenReturn(true);
            mocked2.when(HourLimitBuyPolicy::getCurrTime).thenReturn(LocalTime.of(12, 0));

            generateStore1(); // holiday -> amount(2, 3-10)

            List<CartItemDTO> cart1 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 0, 10));
            cart1.add(new CartItemDTO(0, 2, 8));
            cart1.add(new CartItemDTO(1, 3, 1));
            assertDoesNotThrow(() -> {storeFacade.checkCart("Mr. Krabs", cart1);});

            List<CartItemDTO> cart2 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 2, 18));
            cart1.add(new CartItemDTO(1, 3, 1));
            IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
                storeFacade.checkCart(null, cart1);
            });

            BuyPolicy policy1 = buyPolicyFacade.getBuyPolicy(2);
            BuyPolicy policy2 = buyPolicyFacade.getBuyPolicy(3);
            String expectedErr = Error.makeAgeLimitBuyPolicyError("Alcohol", 18, -1) + "\n" +
                    Error.makeOrBuyPolicyError(policy1.getErrorDescription(), policy2.getErrorDescription());

            assertEquals(expectedErr, expected.getMessage());
        }
    }

    @Test
    void checkCartWithAlcoholNotHoliday() throws JsonProcessingException {
        try (MockedStatic<HolidayBuyPolicy> mocked1 = mockStatic(HolidayBuyPolicy.class);
             MockedStatic<HourLimitBuyPolicy> mocked2 = mockStatic(HourLimitBuyPolicy.class)) {
            mocked1.when(HolidayBuyPolicy::isHoliday).thenReturn(false);
            mocked2.when(HourLimitBuyPolicy::getCurrTime).thenReturn(LocalTime.of(12, 0));

            generateStore1(); // holiday -> amount(2, 3-10)

            List<CartItemDTO> cart1 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 0, 10));
            cart1.add(new CartItemDTO(0, 2, 98));
            cart1.add(new CartItemDTO(1, 3, 1));
            assertDoesNotThrow(() -> {storeFacade.checkCart("Mr. Krabs", cart1);});

            IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
                storeFacade.checkCart(null, cart1);
            });

            String expectedErr = Error.makeAgeLimitBuyPolicyError("Alcohol", 18, -1);
            assertEquals(expectedErr, expected.getMessage());
        }
    }

    @Test
    void checkCartNotInStock() throws JsonProcessingException {
        try (MockedStatic<HolidayBuyPolicy> mocked1 = mockStatic(HolidayBuyPolicy.class);
             MockedStatic<HourLimitBuyPolicy> mocked2 = mockStatic(HourLimitBuyPolicy.class)) {
            mocked1.when(HolidayBuyPolicy::isHoliday).thenReturn(false);
            mocked2.when(HourLimitBuyPolicy::getCurrTime).thenReturn(LocalTime.of(12, 0));

            generateStore1(); // holiday -> amount(2, 3-10)

            List<CartItemDTO> cart1 = new ArrayList<>();
            cart1.add(new CartItemDTO(0, 0, 100000));
            cart1.add(new CartItemDTO(0, 2, 5));
            cart1.add(new CartItemDTO(1, 3, 100000));

            IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
                storeFacade.checkCart("Mr. Krabs", cart1);
            });

            String expectedErr = Error.makeNotEnoughInStcokError(0, 0, 100000, 1000) + "\n";
            expectedErr += Error.makeNotEnoughInStcokError(1, 3, 100000, 1000);
            assertEquals(expectedErr, expected.getMessage());
        }
    }

    @Test
    void getIsOwnerSuccess() {
        assertTrue(storeFacade.getIsOwner("WillyTheChocolateDude", 0, "WillyTheChocolateDude"));
        assertFalse(storeFacade.getIsOwner("WillyTheChocolateDude", 0, "Mr. Krabs"));
        assertTrue(storeFacade.getIsOwner("Mr. Krabs", 0, "WillyTheChocolateDude"));
        assertFalse(storeFacade.getIsOwner("Mr. Krabs", 0, "Mr. Krabs"));
    }

    @Test
    void getIsOwnerStoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getIsOwner("Mr. Krabs", 3, "WillyTheChocolateDude");
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(3), expected.getMessage());
    }

    @Test
    void hasProductInStock() {
        assertTrue(storeFacade.hasProductInStock(0, 0, 1000));
        assertTrue(storeFacade.hasProductInStock(0, 1, 982));
        assertTrue(storeFacade.hasProductInStock(0, 2, 312));

        assertFalse(storeFacade.hasProductInStock(0, 0, 1001));
        assertFalse(storeFacade.hasProductInStock(0, 1, 983));
        assertFalse(storeFacade.hasProductInStock(0, 2, 313));
        assertTrue(storeFacade.hasProductInStock(0, 981, 0));
    }

    @Test
    void hasProductInStockStoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.hasProductInStock(3, 0, 1001);
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(3), expected.getMessage());
    }

    @Test
    void getIsManagerSuccess() {
        assertTrue(storeFacade.getIsManager("WillyTheChocolateDude", 0, "Mr. Krabs"));
        assertTrue(storeFacade.getIsManager("Mr. Krabs", 0, "Mr. Krabs"));
        assertFalse(storeFacade.getIsManager("WillyTheChocolateDude", 0, "WillyTheChocolateDude"));
        assertFalse(storeFacade.getIsManager("Mr. Krabs", 0, "WillyTheChocolateDude"));
    }

    @Test
    void getIsManagerStoreDoesNotExist() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.getIsManager("Mr. Krabs", 3, "WillyTheChocolateDude");
        });
        assertEquals(Error.makeStoreNoStoreWithIdError(3), expected.getMessage());
    }

    // @Test
    // void updateStockSuccess() throws JsonProcessingException {
    //     generateStore1();
    //     List<CartItemDTO> cart = new ArrayList<>();
    //     cart.add(new CartItemDTO(0, 0, 10));
    //     cart.add(new CartItemDTO(0, 1, 30));
    //     cart.add(new CartItemDTO(0, 2, 50));
    //     cart.add(new CartItemDTO(1, 3, 10));
    //     cart.add(new CartItemDTO(1, 4, 10));

    //     Map<Integer, Integer> expectedProductAmountsBefore0 = Map.of(0, 1000, 1, 982, 2, 312);
    //     Map<Integer, Integer> expectedProductAmountsAfter0 = Map.of(0, 990, 1, 952, 2, 262);
    //     Map<Integer, Integer> expectedProductAmountsBefore1 = Map.of(3, 1000, 4, 1000);
    //     Map<Integer, Integer> expectedProductAmountsAfter1 = Map.of(3, 990, 4, 990);

    //     Store store0 = storeRepo.findStoreByID(0);
    //     Store store1 = storeRepo.findStoreByID(1);

    //     assertEquals(expectedProductAmountsBefore0, store0.getProductAmounts());
    //     assertEquals(expectedProductAmountsBefore1, store1.getProductAmounts());

    //     storeFacade.updateStock("WillyTheChocolateDude", cart);

    //     assertEquals(expectedProductAmountsAfter0, store0.getProductAmounts());
    //     assertEquals(expectedProductAmountsAfter1, store1.getProductAmounts());
    // }

    // @Test
    // void updateStockFail() throws JsonProcessingException {
    //     // only checking one fail case because all of them are checked in check cart
    //     generateStore1();
    //     List<CartItemDTO> cart = new ArrayList<>();
    //     cart.add(new CartItemDTO(0, 0, 10000));
    //     cart.add(new CartItemDTO(0, 1, 30));
    //     cart.add(new CartItemDTO(0, 2, 50));
    //     cart.add(new CartItemDTO(1, 3, 10000));
    //     cart.add(new CartItemDTO(1, 4, 10));

    //     Map<Integer, Integer> expectedProductAmounts0 = Map.of(0, 1000, 1, 982, 2, 312);
    //     Map<Integer, Integer> expectedProductAmounts1 = Map.of(3, 1000, 4, 1000);

    //     String expectedError = Error.makeNotEnoughInStcokError(0, 0, 10000, 1000) + "\n" +
    //             Error.makeNotEnoughInStcokError(1, 3, 10000, 1000);

    //     Store store0 = storeRepo.findStoreByID(0);
    //     Store store1 = storeRepo.findStoreByID(1);

    //     assertEquals(expectedProductAmounts0, store0.getProductAmounts());
    //     assertEquals(expectedProductAmounts1, store1.getProductAmounts());

    //     IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
    //         storeFacade.updateStock("WillyTheChocolateDude", cart);
    //     });
    //     assertEquals(expectedError, expected.getMessage());

    //     assertEquals(expectedProductAmounts0, store0.getProductAmounts());
    //     assertEquals(expectedProductAmounts1, store1.getProductAmounts());
    // }

    @Test
    void calculatePrice() throws Exception {
        generateStore1();
        List<CartItemDTO> cart = new ArrayList<>();
        cart.add(new CartItemDTO(0, 0, 10));
        cart.add(new CartItemDTO(0, 1, 30));
        cart.add(new CartItemDTO(0, 2, 50));
        cart.add(new CartItemDTO(1, 3, 10));
        cart.add(new CartItemDTO(1, 4, 10));

        int conditionId = discountPolicyFacade.createMinProductOnStoreCondition(30, "WillyTheChocolateDude");
        int policyId = discountPolicyFacade.createOnStoreConditionDiscountPolicy(50, conditionId, "WillyTheChocolateDude");
        discountPolicyFacade.addDiscountPolicyToStore(0, policyId, "WillyTheChocolateDude");

        List<ProductDataPrice> res = storeFacade.calculatePrice("WillyTheChocolateDude", cart);
        Set<ProductDataPrice> expected = new HashSet<>();
        expected.add(new ProductDataPrice(0, 0, "Shokolad Parah", 10, 5.5, 2.75));
        expected.add(new ProductDataPrice(1, 0, "Kif Kef", 30, 4.2, 2.1));
        expected.add(new ProductDataPrice(2, 0, "Klik Cariot", 50, 7, 3.5));
        expected.add(new ProductDataPrice(3, 1, "Beer", 10, 12, 12));
        expected.add(new ProductDataPrice(4, 1, "Wine", 10, 50, 50));

        assertEquals(expected, new HashSet<>(res));
    }
}