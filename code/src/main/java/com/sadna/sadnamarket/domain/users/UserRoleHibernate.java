package com.sadna.sadnamarket.domain.users;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.DiscriminatorType; // Add this import

import org.springframework.boot.autoconfigure.AutoConfigureOrder;

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
}
