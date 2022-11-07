package com.smart.smartchat.Service.login;

import com.smart.smartchat.Bean.user;
import com.smart.smartchat.mapper.login.check_user;


import com.smart.smartchat.Controller.friendView.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class loginServiceImpl implements loginService
{
    @Autowired
    check_user check;
    @Autowired
    websocket websocket;
    @Value("${login.maxLockTime}")
    private long lockTime;
    @Value("${login.maxNumber}")
    private int MaxNumber;
    public boolean isLoginSuccess(user user){

       user user1 =  check.selectUser(user);
        if (user1!=null&&!isLock(user)){
            user.setId(user1.getId());
            deleteNumber(user);
            check.updateStatus(1,user.getUsername());
            return true;
        }
        return false;
    }
    public boolean userIsNull(user user){
        if ("".equals(user.getUsername())||"".equals(user.getPassword())){
            return true;
        }
        else{
            return false;
        }
    }
    public void lockUser(user user){
        check.insertTime(user);
    }

    @Override
    public boolean isLock(user user)
    {
        Timestamp timestamp = check.selectTime(user);
        if (timestamp ==null){
            return false;
        }
        else if (timestamp.getTime()+lockTime-System.currentTimeMillis()<=0){
            check.deleteLock(user);
            check.deleteNumber(user);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public long lockTime(user user)
    {
        return check.selectTime(user).getTime()-lockTime-System.currentTimeMillis();
    }
    @Override
    public Integer selectNumber(user user)
    {
       return check.selectNumber(user);
    }

    @Override
    public void insertNumber(user user)
    {
        check.insertNumber(user);
    }
    @Override
    public  void UpdateNumber(user user){
        check.UpdateNumber(user);
    }

    @Override
    public void deleteNumber(user user)
    {
        check.deleteNumber(user);
    }
    public String loginFail(user user){
        Integer number =selectNumber(user);
        if (number !=null&&number<MaxNumber){
            UpdateNumber(user);
        }else{
            number=0;
            insertNumber(user);
        }
        number+=1;
        if (number>= MaxNumber)
        {
            if (number== MaxNumber)
            {
                lockUser(user);
            }
            long differ=lockTime(user);
            return "账户已经锁定，剩余时间："+(differ/60000+1)+"分钟";
        } else
        {
           return "账户密码错误,剩余机会：" + (MaxNumber - number) + "次";
        }

    }
}
