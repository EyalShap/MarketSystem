package com.sadna.sadnamarket.domain.users;

import java.util.Set;

public class NewManagerRequest extends NewOwnerRequest{
    private Set<ManagerPermission> permissions;

    public NewManagerRequest(int storeId, Set<ManagerPermission> permissions) {
        super(storeId);
        this.permissions = permissions;
    }
}
