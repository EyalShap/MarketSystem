package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFounder extends StoreOwner {
    private static final Logger logger = LogManager.getLogger(StoreFounder.class);

    public StoreFounder(int storeId, String apointee) {
        super(storeId, apointee);
        logger.info("Entering StoreFounder constructor with storeId={} and apointee={}", storeId, apointee);
        logger.info("Exiting StoreFounder constructor");
    }

    @Override
    public boolean hasPermission(Permission permission) {
        logger.info("Entering hasPermission with permission={}", permission);
        logger.info("Exiting hasPermission with result=true");
        return true;
    }

    @Override
    public void leaveRole(UserRoleVisitor userRoleVisitor, int storeId, Member member, UserFacade userFacade) {
        logger.info("Entering leaveRole with userRoleVisitor={}, storeId={}, member={}, and userFacade={}", userRoleVisitor, storeId, member, userFacade);
        userRoleVisitor.visitStoreFounder(this, this.getStoreId(), member);
        logger.info("Exiting leaveRole");
    }

    @Override
    public String toString() {
        logger.info("Entering toString");
        String result = "store founder of store: " + getStoreId();
        logger.info("Exiting toString with result={}", result);
        return result;
    }
}
