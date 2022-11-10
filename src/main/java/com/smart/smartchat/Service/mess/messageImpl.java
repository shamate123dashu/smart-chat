package com.smart.smartchat.Service.mess;

import com.alibaba.fastjson2.JSON;
import com.smart.smartchat.Bean.text;
import com.smart.smartchat.mapper.friendView.checkFriend;
import com.smart.smartchat.mapper.mess.sendMess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class messageImpl implements message
{
    @Autowired
    sendMess sendMess;
    @Autowired
    checkFriend checkFriend;
    @Override
    public Integer addFriends(String nameA,String nameB)
    {
        Integer id = checkFriend.selectId(nameA);
        Integer fid = checkFriend.selectId(nameB);
        if (sendMess.messNumber(id,fid)!=1){
            if (!IsRepeat(nameA, nameB,"请求添加您为好友！",0,0))
            {
                return sendMess.insertMess(nameA, nameB,"请求添加您为好友！",0);
            }
            else{
                return 2;
            }
        }
        else {
            return 3;
        }
    }


    /**
     * 根据参数查找信息,并将信息转换为json字符串
     * @param nameA 发送方
     * @param nameB 接受方
     * @param status 是否阅读
     * @param messType 信息类型
     * @return 将信息以JSON类型返回
     */
    @Override
    public String selectMess(String nameA,String nameB,Integer status,Integer messType)
    {
        List<text> information = sendMess.selectMess(nameA,nameB,status,messType);
        Map<String, text> messMap = new HashMap<>();
        for (int i = 0; i < information.size(); i++)
        {
            if(information.get(i).getMessType()==1){
                messMap.put("mess"+String.valueOf(i),information.get(i));
            }
            else {
                messMap.put("sys"+String.valueOf(i),information.get(i));
            }
        }
        String s = JSON.toJSONString(messMap);
        System.out.println(s);
        return s;
    }

    /**
     * 判断某条信息是否重复
     */
    public boolean IsRepeat(String nameA,String nameB,String message,Integer status,Integer messType)
    {
        List<text> messList=sendMess.selectMess(nameA,nameB,status,messType);

        if (messList.size()==1&&messList.get(0).getMessage().equals(message)){
            return true;
        }
        else return false;
    }

    @Override
    public String chat(String nameA, String nameB, String message)
    {
        //1.存储信息；
        sendMess.insertMess(nameA,nameB,message,1);
        text text = new text(message);
        text.setNameA(nameA);
        text.setNameB(nameB);
        text.setMessType(1);
        text.setStatus(0);
        Map<String,text> map=new HashMap<>();
        map.put("mess0",text);
        String s = JSON.toJSONString(map);
        System.out.println(s);
        return s;
    }



}
