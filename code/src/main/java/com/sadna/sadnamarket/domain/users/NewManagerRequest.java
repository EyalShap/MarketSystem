package com.sadna.sadnamarket.domain.users;

import java.util.Set;

public class NewManagerRequest extends NewOwnerRequest{
    private Set<Permission> permissions;

    public NewManagerRequest(int storeId, Set<Permission> permissions) {
        super(storeId);
        this.permissions = permissions;
    }
}
