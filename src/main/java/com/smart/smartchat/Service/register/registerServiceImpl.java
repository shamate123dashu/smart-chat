package com.smart.smartchat.Service.register;

import com.smart.smartchat.Bean.user;
import com.smart.smartchat.mapper.register.addUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class registerServiceImpl implements registerService
{
    @Value("${register.minLength}")
    private Integer minLength;
    @Value("${register.maxLength}")
    private Integer maxLength;
    @Autowired
    private addUser addUser01;
    public boolean isRepeat(user user){
        if (addUser01.checkRepeat(user.getUsername())==null){
            return false;
        }

     return true;
    }
    public boolean isSatisfyDemand(user user){
        int length = user.getUsername().length();
        int length1 = user.getPassword().length();
        if(minLength<length&&length<maxLength&&minLength<length1&&length<maxLength){
            return true;
        }
        return false;
    }

    @Override
    public boolean isNull(user user)
        {
        if ("".equals(user.getUsername())||"".equals(user.getPassword())){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addUser(user user)
    {
        if (addUser01.addUser(user)==1){
            return true;
        }
        return false;
    }


}
