package com.sadna.sadnamarket.domain.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STORE_OWNER")
public class StoreOwnerHibernate extends UserRoleHibernate {
private static final Logger logger = LogManager.getLogger(StoreOwnerHibernate.class);
    @Override
    public boolean hasPermission(Permission permission) {
        logger.info("Entering hasPermission with permission={}", permission);
        boolean result = !(permission == Permission.REOPEN_STORE || permission == Permission.CLOSE_STORE);
        logger.info("Exiting hasPermission with result={}", result);
        return result;
    }

    @Override
    public void addPermission(Permission permission) {
        throw new IllegalStateException("Store owner has all the permissions");
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
    
}
