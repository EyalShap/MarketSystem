package com.sadna.sadnamarket;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemoryRepo;
import com.sadna.sadnamarket.domain.users.NotificationDTO;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;

public class UserFacadeTest {

    private MemoryRepo iUserRepo;
    private AuthRepositoryMemoryImpl iAuthRepo;

    private UserFacade userFacade;

    private AuthFacade authFacade;
    @Mock
    private StoreFacade storeFacade;
  
    @Mock
    private OrderFacade orderFacade;


    private String testUsername1="idanasis";
    private String testUsername2="shavirmor";
    private String testUsername3="Nir";
    private String testPassword="12";
    private int testStoreId;
    private int testStoreId2;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iUserRepo=new MemoryRepo();
        this.iAuthRepo=new AuthRepositoryMemoryImpl();
        storeFacade= mock(StoreFacade.class);
        orderFacade=mock(OrderFacade.class);
        this.userFacade=new UserFacade(iUserRepo, storeFacade,orderFacade);
        this.authFacade=new AuthFacade(iAuthRepo,userFacade);
        authFacade.register(testUsername1,testPassword,"Idan","Idan","idan@gmail.com","0501118121");
        authFacade.login(testUsername1,testPassword);
        authFacade.register(testUsername2,testPassword,"Shavit","Mor","shavit@gmail.com","05033303030");
        authFacade.login(testUsername2, testPassword);
        authFacade.register(testUsername3,testPassword,"Nir","Mor","nir@gmail.com","05033303030");
        authFacade.login(testUsername3, testPassword);
        when(storeFacade.createStore(anyString(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn(1); // Return a predefined store ID 
        testStoreId=storeFacade.createStore(testUsername1, null, null, null, null, null, null);
        when(storeFacade.createStore(anyString(), anyString(),  anyString(), anyString(), anyString(), any(), any())).thenReturn(2); // Return a predefined store ID 
        testStoreId2=storeFacade.createStore(testUsername1, null, null, null, null, null, null);
        doNothing().when(storeFacade).addStoreOwner(anyString(), anyInt());

    }

    @Test
    public void testEnterAsGuest() {
        int guestId = userFacade.enterAsGuest();
        assertEquals(1, guestId);
    }

    @Test
    public void testExitGuest() {
        assertThrows(NoSuchElementException.class, () -> userFacade.exitGuest(5));
         int guestId = userFacade.enterAsGuest();
         assertDoesNotThrow( () -> userFacade.exitGuest(guestId));
    }

    @Test
    public void testNotify() {
        userFacade.notify(testUsername1, "hi");
        List<NotificationDTO> ans= userFacade.getNotifications(testUsername1);
        assertEquals(1, ans.size());
        assertEquals("hi", ans.get(0).getMessage());
        
    }

    @Test
    public void testLogin() {    
        authFacade.register("yosi",testPassword,"sami","hatuka","sami@gmail.com","0501118121");
        authFacade.login("yosi",testPassword);
        assertTrue(userFacade.isLoggedIn("yosi"));
    }

    @Test
    public void testLogout() {
       
        userFacade.logout(testUsername1);
        assertFalse(userFacade.isLoggedIn(testUsername1));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLogoutUserNotFound() {
        userFacade.logout("testUser");
    }

    @Test
    public void testRegister() {
        authFacade.register("Jimi",testPassword,"Jimi","hatuka","Jimi@gmail.com","0501118121");
        assertDoesNotThrow(()-> iUserRepo.getMember("Jimi"));
    }
    @Test
    public void testRegisterWithSameUsername() {
        assertThrows(IllegalArgumentException.class,()->  authFacade.register(testUsername1,"12","Idan","Idan","idan@gmail.com","0501118121"));
    }


    @Test
    public void testAddStoreManager() {    
        userFacade.addStoreFounder(testUsername1 ,testStoreId);
        userFacade.addManagerRequest(testUsername1,testUsername2,testStoreId);
        assertTrue(userFacade.getNotifications(testUsername2).size()>0);
        doNothing().when(storeFacade).addStoreManager(anyString(), anyInt());
        assertDoesNotThrow(()->userFacade.accept(testUsername2, 1));
        assertTrue(userFacade.getMemberRoles(testUsername2).size()>0);
    }
    @Test
    public void testAddStoreOwner() {    
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        userFacade.addOwnerRequest(testUsername1,testUsername2,testStoreId2);
        assertTrue(userFacade.getNotifications(testUsername2).size()>0);
        assertDoesNotThrow(()->userFacade.accept(testUsername2, 1));
        assertTrue(userFacade.getMemberRoles(testUsername2).size()>0);
    }
    @Test
    public void testAddStoreOwnerFailWhichIsntRelatedToStore() {    
        when(storeFacade.createStore(anyString(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn(3);
        int testStoreId3=storeFacade.createStore(testUsername1, null, null, null, null, null, null);
        assertThrows(IllegalArgumentException.class,()->userFacade.addOwnerRequest(testUsername1,testUsername2,testStoreId3));
        assertTrue(userFacade.getNotifications(testUsername2).size()==0);
    }
    @Test
    public void testAddStoreOwnerFailWhoAppointedHim() {    
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        userFacade.addOwnerRequest(testUsername1,testUsername2,testStoreId2);
        assertTrue(userFacade.getNotifications(testUsername2).size()>0);
        assertDoesNotThrow(()->userFacade.accept(testUsername2, 1));
        assertThrows(IllegalStateException.class,()->userFacade.addOwnerRequest(testUsername2,testUsername1,testStoreId));
    }
    @Test
    public void testAddPermission() {    
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        userFacade.addManagerRequest(testUsername1,testUsername2,testStoreId2);
        assertTrue(userFacade.getNotifications(testUsername2).size()>0);
        doNothing().when(storeFacade).addStoreOwner(anyString(), anyInt());
        assertDoesNotThrow(()->userFacade.accept(testUsername2, 1));
        assertDoesNotThrow(()->userFacade.addPremssionToStore(testUsername1,testUsername2, testStoreId2,Permission.ADD_BUY_POLICY));
       assertTrue(userFacade.checkPremssionToStore(testUsername2, testStoreId2,Permission.ADD_BUY_POLICY));
    }

    @Test
    public void testFounderLeaveRoleFail(){
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        assertThrows(IllegalStateException.class,()->userFacade.leaveRole(testUsername1, testStoreId2));  
    }
    @Test
    public void testManagerLeaveRole(){
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        userFacade.addManagerRequest(testUsername1,testUsername2,testStoreId2);
        doNothing().when(storeFacade).addStoreOwner(anyString(), anyInt());
        userFacade.accept(testUsername2, 1);
        assertTrue(userFacade.getMemberRoles(testUsername2).size()>0);
        userFacade.leaveRole(testUsername2, testStoreId);
        assertTrue(userFacade.getMemberRoles(testUsername2).size()==0);
    }
    @Test
    public void testOwnerLeaveRole(){
        userFacade.addStoreFounder(testUsername1 ,testStoreId2);
        userFacade.addOwnerRequest(testUsername1,testUsername2,testStoreId);
        doNothing().when(storeFacade).addStoreOwner(anyString(), anyInt());
        userFacade.accept(testUsername2, 1);
        userFacade.addOwnerRequest(testUsername2, testUsername3, testStoreId);
        userFacade.accept(testUsername3, 1);
        assertEquals(testUsername2, userFacade.getMember(testUsername3).getRoleOfStore(testStoreId).getApointee());
        userFacade.leaveRole(testUsername2, testStoreId);
        assertTrue(userFacade.getMemberRoles(testUsername2).size()==0);
        assertTrue(userFacade.getMemberRoles(testUsername3).size()==0);
    }
    @Test
    public void testUserAddProduct(){
        userFacade.addProductToCart(testUsername1, testStoreId, 1, 2);
        List<CartItemDTO> items=userFacade.getMember(testUsername1).getCartItems();
        assertEquals(1, items.size());
        assertEquals(1, items.get(testStoreId).getProductId());
        assertEquals(2, items.get(testStoreId).getAmount());
        userFacade.addProductToCart(testUsername1, testStoreId2, 2, 3);
        items=userFacade.getMember(testUsername1).getCartItems();
        assertEquals(2, items.size());
    }
    @Test
    public void testUserRemoveProduct(){
        userFacade.addProductToCart(testUsername1, testStoreId, 1, 2);
        userFacade.removeProductFromCart(testUsername1, testStoreId2, 1);
        List<CartItemDTO> items=userFacade.getMember(testUsername1).getCartItems();
        assertEquals(0, items.size());
    }
    @Test
    public void testUserChangeAmountOfProduct(){
        userFacade.addProductToCart(testUsername1, testStoreId, 1, 2);
        userFacade.changeQuantityCart(testUsername1, testStoreId, 1, 3);
        List<CartItemDTO> items=userFacade.getMember(testUsername1).getCartItems();
        assertEquals(1, items.size());
        assertEquals(1, items.get(testStoreId).getProductId());
        assertEquals(3, items.get(testStoreId).getAmount());
    }
    @Test
    public void testGuestAddProduct(){
        int guestId = userFacade.enterAsGuest();
        userFacade.addProductToCart(guestId, testStoreId, 1, 2);
        List<CartItemDTO> items=userFacade.getCartItems(guestId);
        assertEquals(1, items.size());
        assertEquals(1, items.get(testStoreId).getProductId());
        assertEquals(2, items.get(testStoreId).getAmount());
        userFacade.addProductToCart(guestId, testStoreId2, 2, 3);
        items=userFacade.getCartItems(guestId);
        assertEquals(2, items.size());
    }
    @Test
    public void testGuestRemoveProduct(){
        int guestId = userFacade.enterAsGuest();
        userFacade.addProductToCart(guestId, testStoreId, 1, 2);
        userFacade.removeProductFromCart(guestId, testStoreId2, 1);
        List<CartItemDTO> items=userFacade.getCartItems(guestId);
        assertEquals(0, items.size());
    }
    @Test
    public void testGuestChangeAmountOfProduct(){
        int guestId = userFacade.enterAsGuest();
        userFacade.addProductToCart(guestId, testStoreId, 1, 2);
        userFacade.changeQuantityCart(guestId, testStoreId, 1, 3);
        List<CartItemDTO> items=userFacade.getCartItems(guestId);
        assertEquals(1, items.size());
        assertEquals(1, items.get(testStoreId).getProductId());
        assertEquals(3, items.get(testStoreId).getAmount());
    }

    @Test
    public void validateCartMoveWithGuestWhenLogin(){
        userFacade.logout(testUsername2);
        int guestId = userFacade.enterAsGuest();
        userFacade.addProductToCart(guestId, testStoreId, 1, 2);
        authFacade.login(testUsername2, testPassword, guestId);
        List<CartItemDTO> items=userFacade.getMember(testUsername2).getCartItems();
        assertEquals(1, items.size());
    }
    @Test
    public void validateCartDoesntMoveWithGuestWhenLoginIfNotEmpty(){
        userFacade.addProductToCart(testUsername2, testStoreId, 1, 2);
        userFacade.addProductToCart(testUsername2, testStoreId2, 2, 3);
        userFacade.logout(testUsername2);
        int guestId = userFacade.enterAsGuest();
        userFacade.addProductToCart(guestId, testStoreId, 1, 2);
        authFacade.login(testUsername2, testPassword, guestId);
        List<CartItemDTO> items=userFacade.getMember(testUsername2).getCartItems();
        assertEquals(2, items.size());
    }
}
