package com.sadna.sadnamarket.domain.stores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyFacade;
import com.sadna.sadnamarket.domain.buyPolicies.MemoryBuyPolicyRepository;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.MemoryConditionRepository;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.discountPolicies.Discounts.MemoryDiscountPolicyRepository;
import com.sadna.sadnamarket.domain.orders.IOrderRepository;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.products.IProductRepository;
import com.sadna.sadnamarket.domain.products.MemoryProductRepository;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.*;
import com.sadna.sadnamarket.service.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StoreFacadeTest {
    private StoreFacade storeFacade;
    private AuthFacade authFacade;
    private UserFacade userFacade;
    private ProductFacade productFacade;
    ObjectMapper objectMapper = new ObjectMapper();
    private LocalDate testDate=LocalDate.of(1990, 11, 11);


    @BeforeEach
    public void setUp() {
        setUpFacades();
        registerUsers();
        generateStore0();
        addPermissions();
        addProducts();
    }

    private void setUpFacades() {
        IOrderRepository orderRepo = new MemoryOrderRepository();
        OrderFacade orderFacade = new OrderFacade(orderRepo);

        IStoreRepository storeRepo = new MemoryStoreRepository();
        this.storeFacade = new StoreFacade(storeRepo);

        IUserRepository userRepo = new MemoryRepo();
        this.userFacade = new UserFacade(userRepo, storeFacade, orderFacade);

        IProductRepository productRepo = new MemoryProductRepository();
        this.productFacade = new ProductFacade(productRepo);

        BuyPolicyFacade buyPolicyFacade = new BuyPolicyFacade(new MemoryBuyPolicyRepository());
        DiscountPolicyFacade discountPolicyFacade = new DiscountPolicyFacade(new MemoryConditionRepository(), new MemoryDiscountPolicyRepository());

        this.storeFacade.setUserFacade(userFacade);
        this.storeFacade.setOrderFacade(orderFacade);
        this.storeFacade.setProductFacade(productFacade);
        this.storeFacade.setBuyPolicyFacade(buyPolicyFacade);
        this.storeFacade.setDiscountPolicyFacade(discountPolicyFacade);

        this.authFacade = new AuthFacade(new AuthRepositoryMemoryImpl(), userFacade);
    }

    private void registerUsers() {
        authFacade.register("Mr. Krabs", "654321", "Eugene", "Krabs", "eugene@gmail.com", "0521957682",testDate);
        authFacade.login("Mr. Krabs", "654321");
        authFacade.register("WillyTheChocolateDude", "123456", "Willy", "Wonka", "willy@gmail.com", "0541095600",testDate);
        authFacade.login("WillyTheChocolateDude", "123456");
        authFacade.register("Bob", "24680", "Bob", "Cohen", "bob@gmail.com", "0544219674",testDate);
        authFacade.login("Bob", "24680");
        authFacade.register("Alice", "08642", "Alice", "Levi", "alice@gmail.com", "0523176455",testDate);
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

    private void addProducts() {
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Shokolad Parah", 1000, 5.5, "Chocolate", 4,2);
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Kif Kef", 982, 4.2, "Chocolate", 4.3,2);
        storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Klik Cariot", 312, 7, "Chocolate", 5, 2);
    }

    private StoreInfo generateStore0Info() {
        LocalTime openingHour = LocalTime.of(10, 0);
        LocalTime closingHour = LocalTime.of(21, 0);
        LocalTime fridayClosingHour = LocalTime.of(14, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        return new StoreInfo("Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours);
    }

    private StoreInfo generateStore1Info() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        return new StoreInfo("Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120", openingHours, closingHours);
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
        LocalTime openingHour = LocalTime.of(10, 0);
        LocalTime closingHour = LocalTime.of(21, 0);
        LocalTime fridayClosingHour = LocalTime.of(14, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        storeFacade.createStore("WillyTheChocolateDude", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours);
    }

    private void generateStore1() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        storeFacade.createStore("Mr. Krabs", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120", openingHours, closingHours);
    }

    @Test
    void createStoreSuccess() {
        Set<Integer> expected0 = new HashSet<>();
        Set<Integer> expected1 = new HashSet<>();
        Collections.addAll(expected0, 0);
        Collections.addAll(expected1, 0, 1);

        assertEquals(expected0, storeFacade.getAllStoreIds());

        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};
        storeFacade.createStore("Mr. Krabs", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120", openingHours, closingHours);

        assertEquals(expected1, storeFacade.getAllStoreIds());

        assertEquals(generateStoreObject0().getStoreDTO(), storeFacade.getStoreInfo("WillyTheChocolateDude", 0));
        assertEquals(generateStoreObject1().getStoreDTO(), storeFacade.getStoreInfo("Mr. Krabs", 1));
    }

    @Test
    void createStoreNoUser() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};

        NoSuchElementException expected = assertThrows(NoSuchElementException.class, () -> {
            storeFacade.createStore("Dana", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120", openingHours, closingHours);
        });

        String expectedMessage = Error.makeMemberUserDoesntExistError("Dana");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreUserNotLoggedIn() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Alice", "Krusty Krab", "Bikini Bottom", "krab@gmail.com", "0541085120", openingHours, closingHours);
        });

        String expectedMessage = Error.makeStoreUserHasToBeLoggedInError("Alice");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreAlreadyExists() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(15, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Mr. Krabs", "Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours);
        });

        String expectedMessage = Error.makeStoreWithNameAlreadyExistsError("Chocolate Factory");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreParamsNotValid() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(9, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("Mr. Krabs", "", "", "", "", openingHours, closingHours);
        });

        String expectedMessage = Error.makeStoreNotValidAspectError("", "store name");
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void createStoreHoursNotValid() {
        LocalTime openingHour = LocalTime.of(9, 0);
        LocalTime closingHour = LocalTime.of(20, 0);
        LocalTime fridayClosingHour = LocalTime.of(8, 0);
        LocalTime[] openingHours = new LocalTime[]{openingHour, openingHour, openingHour, openingHour, openingHour, openingHour, null};
        LocalTime[] closingHours = new LocalTime[]{closingHour, closingHour, closingHour, closingHour, closingHour, fridayClosingHour, null};

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.createStore("WillyTheChocolateDude", "Another Chocolate Factory", "Beer Sheva", "chocolate@gmail.com", "0541075403", openingHours, closingHours);
        });

        String expectedMessage = Error.makeStoreOpeningHoursNotValid();
        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    void addProductToStoreSuccess_Owner() {
        int productId = storeFacade.addProductToStore("WillyTheChocolateDude", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        assertEquals(409, storeFacade.getProductAmount(0, productId));

        ProductDTO expected = new ProductDTO(productId, "Mekupelet", 3.5, "Chocolate", 4.2, 2,true);
        assertEquals(expected, productFacade.getProductDTO(productId));
    }

    @Test
    void addProductToStoreSuccess_ManagerWithPermissions() {
        int productId = storeFacade.addProductToStore("Mr. Krabs", 0, "Mekupelet", 409, 3.5, "Chocolate", 4.2,2);
        assertEquals(409, storeFacade.getProductAmount(0, productId));

        ProductDTO expected = new ProductDTO(productId, "Mekupelet", 3.5, "Chocolate", 4.2, 2, true);
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
        ProductDTO expected1 = new ProductDTO(0, "Cow Chocolate", 10, "Chocolate", 4.8, 2,true);
        assertEquals(expected1, productFacade.getProductDTO(0));

        storeFacade.updateProduct("Mr. Krabs", 0, 1, "Kit Kat", 100, 8, "Chocolate", 4.21);
        ProductDTO expected2 = new ProductDTO(1, "Kit Kat", 8, "Chocolate", 4.21, 2,true);
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
        // This will hopefully work on v2

        /*SupplyInterface supplyMock = Mockito.mock(SupplyInterface.class);
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
        Map<Integer, String> products = new HashMap<>();
        products.put(0, objectMapper.writeValueAsString(productFacade.getProductDTO(0)));
        products.put(1, objectMapper.writeValueAsString(productFacade.getProductDTO(1)));
        products.put(2, objectMapper.writeValueAsString(productFacade.getProductDTO(2)));
        expected.add(new OrderDTO("Mr. Krabs", "Chocolate Factory", amounts, products));

        userFacade.addProductToCart("Mr. Krabs", 0, 0, 5);
        userFacade.addProductToCart("Mr. Krabs", 0, 1, 15);
        userFacade.addProductToCart("Mr. Krabs", 0, 2, 12);

        CreditCardDTO creditCard = new CreditCardDTO("123", "456", new Date(), "291845322");
        AddressDTO address = new AddressDTO("Israel", "Tel Aviv", "A", "B", "123", "Mr. Krabs", "0541762645", "krab@gmail.com");
        userFacade.purchaseCart("Mr. Krabs", creditCard, address);

        List<OrderDTO> history = storeFacade.getStoreOrderHistory("WillyTheChocolateDude", 0);
        assertEquals(expected, history);*/
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
        ProductDTO expected = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true);
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

        ProductDTO expected = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true);
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
        ProductDTO product0 = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true);
        ProductDTO product2 = new ProductDTO(2, "Klik Cariot", 7, "Chocolate", 5, 2,true);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product0, 1000);
        expected.put(product1, 982);
        expected.put(product2, 312);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, null, null, -1, -1));
    }

    @Test
    void getProductsInfoFiltered() throws JsonProcessingException {
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product1, 982);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, "Kif Kef", null, 5, -1));
    }

    @Test
    void getProductsInfoDeleteProduct() throws JsonProcessingException {
        storeFacade.deleteProduct("WillyTheChocolateDude", 0, 0);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true);
        Map<ProductDTO, Integer> expected = new HashMap<>();
        expected.put(product1, 982);
        assertEquals(expected, storeFacade.getProductsInfoAndFilter(null, 0, null, null, 6, -1));
    }

    @Test
    void getProductsInfoStoreClosed() throws JsonProcessingException {
        storeFacade.closeStore("WillyTheChocolateDude", 0);
        ProductDTO product0 = new ProductDTO(0, "Shokolad Parah", 5.5, "Chocolate", 4, 2,true);
        ProductDTO product1 = new ProductDTO(1, "Kif Kef", 4.2, "Chocolate", 4.3, 2,true);
        ProductDTO product2 = new ProductDTO(2, "Klik Cariot", 7, "Chocolate", 5, 2,true);
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
    void addBuyPolicy() {
        // in v2
    }

    @Test
    void addDiscountPolicy() {
        // in v2
    }

    @Test
    void checkCart() {
        // in v2
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
        assertFalse(storeFacade.hasProductInStock(0, 981, 0));
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

    @Test
    void buyCart() {
        // in v2
    }

    @Test
    void calculatePrice() {
        // in v2
    }
}