package com.sadna.sadnamarket.domain.users;

import java.util.List;

public interface IUserRepository {
    Member getMember(String userName);
    List<Member> getAll();
    void store(Member member);
    boolean hasMember(String name);
    int addGuest();
    void deleteGuest(int guestID);
    Guest getGuest(int guestID);
    List<CartItemDTO> getUserCart(String username);
    List<CartItemDTO> getGuestCart(int guestID);
}
