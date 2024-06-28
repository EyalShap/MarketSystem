package com.sadna.sadnamarket.domain.users;
import javax.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;



@Entity
@Table(name = "permissions")
public class PermissionHibernate {
    @Column
    private int roleId;
    @Column
    private int permissionId;
    public int getRoleId() {
        return roleId;
    }
    public int getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
