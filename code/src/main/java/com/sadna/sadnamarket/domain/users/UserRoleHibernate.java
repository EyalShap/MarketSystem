package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.DiscriminatorType; // Add this import
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "User_roles")
public abstract class UserRoleHibernate implements UserRole{
    
    @Id
    @GeneratedValue
    private int id;

    @Column
    private String username;
    @Column
    private int storeId;
    @Column
    private String roleName;
    @Column
    private String apointee;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_orders", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permissionId")
    private List<Integer> permissions;

    public String getUsername() {
        return username;
    }
    public int getStoreId() {
        return storeId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getId() {
        return id;
    }
    public String getApointee() {
        return apointee;
    }
    public void setApointee(String apointee) {
        this.apointee = apointee;
    }

    public List<Permission> getPermissions() {
        List<Permission> permissionsRet = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            permissionsRet.add(i, Permission.values()[permissions.get(i)]);
        }
        return permissionsRet;
    }
}
