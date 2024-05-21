package com.sadna.sadnamarket.domain.users;

import java.util.List;

public interface IUserRepository {
    Member getMember(String userName);
    List<Member> getAll();
    void store(Member member);
    boolean hasMember(String name);
}
