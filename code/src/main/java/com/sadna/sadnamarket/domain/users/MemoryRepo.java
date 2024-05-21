package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.users.Member;
import com.sadna.sadnamarket.domain.users.UserFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class MemoryRepo implements IUserRepository {
    private static HashMap<String,Member> members;
    private static HashMap<Integer,Guest> guests;
    private static HashMap<String,String> userNameAndPassword;

    public MemoryRepo() {
        members=new HashMap<>();
        userNameAndPassword=new HashMap<>();
    }

    @Override
    public Member findById(String userName) {
       if(hasMember(userName)){
            return members.get(userName);
       }
       else return null;
      
    }

    public boolean hasMember(String username){
        if(members.containsKey(username))
            return true;
        else{ 
            throw new NoSuchElementException("user doesnt exist");
        }
    }

    @Override
    public List<Member> getAll() {
        return new ArrayList<>(members.values());
    }       

    @Override
    public void store(Member member) {
        members.put()
    }

    @Override
    public void delete(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
