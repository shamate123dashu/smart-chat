package com.smart.smartchat.Service.register;

import com.smart.smartchat.Bean.user;

public interface registerService
{
    public boolean isRepeat(user user);
    public boolean isSatisfyDemand(user user);
    public boolean isNull(user user);
    public boolean addUser(user user);
}
