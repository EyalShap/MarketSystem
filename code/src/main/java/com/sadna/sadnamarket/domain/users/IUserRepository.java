package com.sadna.sadnamarket.domain.users;

import java.util.List;

public interface IUserRepository {
    Member findById(String userName);
    List<Member> getAll();
    void delete(String userName);
    void store(String userName, String password);
}
