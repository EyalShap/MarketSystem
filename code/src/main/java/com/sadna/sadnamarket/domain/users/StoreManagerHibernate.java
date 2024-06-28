package com.sadna.sadnamarket.domain.users;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STORE_MANAGER")
public class StoreManagerHibernate extends  UserRoleHibernate {


    @Override
    public int getStoreId() {
       return super.getStoreId();
    }
    @Override
    public boolean hasPermission(Permission permission) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasPermission'");
    }

    @Override
    public void addPermission(Permission permission) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPermission'");
    }

    @Override
    public void removePermission(Permission permission) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePermission'");
    }

    @Override
    public List<Permission> getPermissions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPermissions'");
    }

    @Override
    public boolean isApointedByUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isApointedByUser'");
    }

    @Override
    public List<String> getAppointers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAppointers'");
    }

    @Override
    public void addAppointers(String apointee) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAppointers'");
    }

    @Override
    public void leaveRole(UserRoleVisitor userRoleVisitor, int storeId, Member member, UserFacade userFacade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRole'");
    }

    @Override
    public RequestDTO sendRequest(UserFacade userFacade, String senderName, String sentName, String reqType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendRequest'");
    }

    @Override
    public String getApointee() {
        return super.getApointee();
    }
    
}
