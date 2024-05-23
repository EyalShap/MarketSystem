package com.sadna.sadnamarket.domain.users;

public class StoreFounder extends StoreOwner{

    public StoreFounder(int storeId,String apointee) {
        super(storeId,apointee);
    }
    @Override
    public boolean hasPermission(Permission permission){
        return true;
    }
    @Override
    public void leaveRole(UserRoleVisitor userRoleVisitor,int storeId,Member member,UserFacade userFacade) {
        userRoleVisitor.visitStoreFounder(this,this.getStoreId(),member);
    }

    @Override
    public String toString(){
        return "store founder of store: "+getStoreId();
    }
    
}
