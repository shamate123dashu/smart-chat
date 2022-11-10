package com.smart.smartchat.Service.friendView;

import com.smart.smartchat.Bean.user;


import java.util.List;


public interface friendView
{
    public List<user> checkFriend(Integer id,Integer relation);
    public String checkFriend01(String name);
    public List<String> checkFri(String username);
    public Integer exit(String username);
    public user search(String username);
    public void addRelation(String nameA,String nameB,Integer relation);
}
