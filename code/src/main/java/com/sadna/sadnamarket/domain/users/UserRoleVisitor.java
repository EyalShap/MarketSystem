package com.sadna.sadnamarket.domain.users;

public class UserRoleVisitor {

  public void visitStoreManager(StoreManager role, int storeId,Member member) {
    member.removeRole(role);
  }


  public void visitStoreOwner(StoreOwner role, int storeId,Member member,UserFacade userFacade) {
    for (String username : role.getAppointers()) {
        userFacade.leaveRole(username,storeId);
    }
  }
  public void visitStoreFounder(StoreFounder role, int storeId,Member member) {
    throw new IllegalStateException("owner cant leave the job");
  }

}
