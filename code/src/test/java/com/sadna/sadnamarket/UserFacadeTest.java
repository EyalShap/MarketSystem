package com.sadna.sadnamarket;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import com.sadna.sadnamarket.domain.users.IUserRepository;
import com.sadna.sadnamarket.domain.users.Member;
import com.sadna.sadnamarket.domain.users.Permission;
import com.sadna.sadnamarket.domain.users.UserFacade;

public class UserFacadeTest {

    @Mock
    private IUserRepository iUserRepo;

    @Mock
    private UserFacade userFacade;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnterAsGuest() {
        when(iUserRepo.addGuest()).thenReturn(1);
        int guestId = userFacade.enterAsGuest();
        assertEquals(1, guestId);
        verify(iUserRepo, times(1)).addGuest();
    }

    @Test
    public void testExitGuest() {
        userFacade.exitGuest(1);
        verify(iUserRepo, times(1)).deleteGuest(1);
    }

    @Test
    public void testNotify() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        userFacade.notify("testUser", "Test message");
        verify(member, times(1)).addNotification("Test message");
    }

    @Test
    public void testLogin() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        userFacade.login("testUser", "password");
        verify(member, times(1)).setLogin(true);
    }

    @Test
    public void testLogout() {
        Member member = mock(Member.class);
        when(iUserRepo.hasMember("testUser")).thenReturn(true);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        when(iUserRepo.addGuest()).thenReturn(2);

        int guestId = userFacade.logout("testUser");
        assertEquals(2, guestId);
        verify(member, times(1)).logout();
        verify(iUserRepo, times(1)).addGuest();
    }

    @Test(expected = NoSuchElementException.class)
    public void testLogoutUserNotFound() {
        when(iUserRepo.hasMember("testUser")).thenReturn(false);
        userFacade.logout("testUser");
    }

    @Test
    public void testRegister() {
        userFacade.register("newUser", "password");
        verify(iUserRepo, times(1)).store(any(Member.class));
    }

    @Test
    public void testAddStoreManager() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        userFacade.addStoreManager("testUser", 1);
        verify(member, times(1)).addRole(any(StoreManager.class));
    }

    @Test
    public void testAddStoreOwner() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        userFacade.addStoreOwner("testUser", 1);
        verify(member, times(1)).addRole(any(StoreOwner.class));
    }

    @Test
    public void testAddStoreFounder() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        userFacade.addStoreFounder("testUser", 1);
        verify(member, times(1)).addRole(any(StoreFounder.class));
    }

    @Test
    public void testAddPermissionToStore() {
        Member member = mock(Member.class);
        when(iUserRepo.getMember("testUser")).thenReturn(member);
        Permission permission = mock(Permission.class);
        userFacade.addPremssionToStore("testUser", 1, permission);
        verify(member, times(1)).addPermissionToRole(permission, 1);
    }
}
