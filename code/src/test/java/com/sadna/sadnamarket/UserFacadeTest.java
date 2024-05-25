/*package com.sadna.sadnamarket;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import com.sadna.sadnamarket.domain.users.MemoryRepo;
import com.sadna.sadnamarket.domain.users.NotificationDTO;
import com.sadna.sadnamarket.domain.auth.AuthFacade;
import com.sadna.sadnamarket.domain.auth.AuthRepositoryMemoryImpl;
import com.sadna.sadnamarket.domain.users.Member;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;

public class UserFacadeTest {

    private MemoryRepo iUserRepo;
    private AuthRepositoryMemoryImpl iAuthRepo;

    private UserFacade userFacade;

    private AuthFacade authFacade;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iUserRepo=new MemoryRepo();
        this.iAuthRepo=new AuthRepositoryMemoryImpl();
        this.userFacade=new UserFacade(iUserRepo);
        this.authFacade=new AuthFacade(iAuthRepo,userFacade);
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
        userFacade.register("yosi","sami","hatuka","sami@gmail.com","0501118121");
        userFacade.notify("yosi", "yagever");
        List<NotificationDTO> ans= userFacade.getNotifications("yosi");
        assertEquals(1, ans.size());
        assertEquals("yagever", ans.get(0).getMessage());
        
    }

    @Test
    public void testLogin() {
        
        authFacade.register("yosi","12","sami","hatuka","sami@gmail.com","0501118121");
        authFacade.login("yosi","12");
        assertTrue(userFacade.isLoggedIn("yosi"));
    }

    @Test
    public void testLogout() {
        authFacade.register("yosi","12","sami","hatuka","sami@gmail.com","0501118121");
        authFacade.login("yosi","12");
        userFacade.logout("yosi");
        assertFalse(userFacade.isLoggedIn("yosi"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLogoutUserNotFound() {
        userFacade.logout("testUser");
    }

    @Test
    public void testRegister() {
        authFacade.register("yosi","12","sami","hatuka","sami@gmail.com","0501118121");
        assertDoesNotThrow(()-> iUserRepo.getMember("yosi"));
    }

    @Test
    public void testAddStoreManager() {
        
        authFacade.register("yosi","12","sami","hatuka","sami@gmail.com","0501118121");
        

    }

    // @Test
    // public void testAddStoreOwner() {
    //     Member member = mock(Member.class);
    //     when(iUserRepo.getMember("testUser")).thenReturn(member);
    //     userFacade.addStoreOwner("testUser", 1);
    //     //verify(member, times(1)).addRole(any(StoreOwner.class));
    // }

    // @Test
    // public void testAddStoreFounder() {
    //     Member member = mock(Member.class);
    //     when(iUserRepo.getMember("testUser")).thenReturn(member);
    //     userFacade.addStoreFounder("testUser", 1);
    //    // verify(member, times(1)).addRole(any(StoreFounder.class));
    // }

    // @Test
    // public void testAddPermissionToStore() {
    //     Member member = mock(Member.class);
    //     when(iUserRepo.getMember("testUser")).thenReturn(member);
    //     userFacade.addPremssionToStore("testUser", 1, Permission.ADD_PRODUCTS);
    //     verify(member, times(1)).addPermissionToRole(Permission.ADD_PRODUCTS, 1);
    // }
}*/
