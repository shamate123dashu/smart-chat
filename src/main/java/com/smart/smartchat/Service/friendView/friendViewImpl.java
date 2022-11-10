package com.smart.smartchat.Service.friendView;

import com.alibaba.fastjson.JSON;
import com.smart.smartchat.Bean.user;
import com.smart.smartchat.mapper.friendView.checkFriend;
import com.smart.smartchat.mapper.mess.sendMess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class friendViewImpl implements friendView
{
    @Autowired
    checkFriend checkFriend;
    @Autowired
    sendMess sendMess;

    @Override
    public List<user> checkFriend(Integer id, Integer relation)
    {

        return checkFriend.check(id, relation);
    }

    @Override
    public String checkFriend01(String name)
    {
        Integer id = checkFriend.selectId(name);
        Map<String, List<user>> map01 = new HashMap<>();
        for (int i = 0; i < 4; i++)
        {
            map01.put(String.valueOf(i), checkFriend.check(id, i));
        }
        String mess = JSON.toJSONString(map01);
        return mess;
    }
    public List<String> checkFri(String username){
        List<String> names = checkFriend.selectName(username);
        return names;
    }

    @Override
    public Integer exit(String username)
    {
       return checkFriend.exit(username);
    }

    @Override
    public user search(String username)
    {
        return checkFriend.search(username);
    }

    @Override
    public void addRelation(String nameA,String nameB, Integer relation)
    {
        String newMess="(已拒绝)";
        String oldmess="请求添加您为好友！";
        if (relation!=-1){
            Integer id = checkFriend.selectId(nameA);
            System.out.println(id);
            Integer fid= checkFriend.selectId(nameB);
            System.out.println(fid);
            checkFriend.addRelation(id,fid,relation);
            checkFriend.addRelation(fid,id,relation);
            newMess="(已接受)";
        }
        sendMess.updateMessage(1,oldmess+newMess,nameA,nameB,0,oldmess);
    }

}
