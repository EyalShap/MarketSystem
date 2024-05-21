package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class MemoryRepo implements IUserRepository {
    private static HashMap<String,Member> members;

    public MemoryRepo() {
        members=new HashMap<>();
    }

    @Override
    public Member getMember(String userName) {
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
        members.put(member.getName(),member);
    }

   

   
}
