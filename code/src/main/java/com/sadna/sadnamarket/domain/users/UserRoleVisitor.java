package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRoleVisitor {
  private static final Logger logger = LogManager.getLogger(UserRoleVisitor.class);

  public void visitStoreManager(StoreManager role, int storeId,Member member) {
    logger.info("finished visit Store Manager");
    member.removeRole(role);
  }


  public void visitStoreOwner(StoreOwner role, int storeId,Member member,UserFacade userFacade) {
    logger.info("visit Store owner");
    for (String username : role.getAppointers()) {
        userFacade.leaveRole(username,storeId);
    }
    logger.info("finished visit Store owner");
  }
  public void visitStoreFounder(StoreFounder role, int storeId,Member member) {
    logger.error("visit Store founder");
    throw new IllegalStateException("owner cant leave the job");
  }

}
