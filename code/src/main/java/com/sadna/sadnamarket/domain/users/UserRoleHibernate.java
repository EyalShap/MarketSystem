package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.DiscriminatorType; // Add this import
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Entity;
import javax.persistence.OneToMany;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "user_roles")
public abstract class UserRoleHibernate implements UserRole{
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    protected int id;

    @Column
    protected String username;
    @Column
    protected int storeId;

    @Column
    protected String apointee;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permissionId")
    protected List<Permission> permissions;



    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "role_appointments", joinColumns = {@JoinColumn(name = "apointee")})
    @Column(name = "apointer")
    protected List<String> appointments;

    public UserRoleHibernate(int storeId) {
        this.storeId = storeId;
        permissions = new ArrayList<>();
        appointments=new ArrayList<>();
    }
    public UserRoleHibernate() {
    }
    public String getUsername() {
        return username;
    }
    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
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
        return permissions;
    }
}
