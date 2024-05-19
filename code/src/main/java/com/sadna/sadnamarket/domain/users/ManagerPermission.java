package com.sadna.sadnamarket.domain.users;

public enum ManagerPermission {
    ADD_PRODUCTS(0),
    DELETE_PRODUCTS(1),
    UPDATE_PRODUCTS(2);

    private final int permissionNum;

    ManagerPermission(int permissionNum) {
        this.permissionNum = permissionNum;
    }

    public int getPermissionNum() {
        return permissionNum;
    }

    public static ManagerPermission getPermission(int permissionNum) {
        for (ManagerPermission permission : ManagerPermission.values()) {
            if (permission.getPermissionNum() == permissionNum) {
                return permission;
            }
        }
        throw new IllegalArgumentException(String.format("There is no manager permission with number %d.", permissionNum));
    }
}

