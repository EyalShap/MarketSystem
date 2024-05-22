package com.sadna.sadnamarket.domain.users;

import com.fasterxml.jackson.annotation.JsonFilter;

public class UserDTO {
    private int userId;

    public UserDTO() {
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
