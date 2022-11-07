package com.smart.smartchat.Service.login;

import com.smart.smartchat.Bean.user;


public interface loginService
{
    public boolean isLoginSuccess(user user);
    public boolean userIsNull(user user);
    public default void lockUser(user user){}
    public default boolean isLock(user user){return false;}
    public default long lockTime(user user){
        return 0;
    }
    public default Integer selectNumber(user user){return null;}
    public default void insertNumber(user user){}
    public default void UpdateNumber(user user){}
    public default void deleteNumber(user user){}
    public String loginFail(user user);
}
