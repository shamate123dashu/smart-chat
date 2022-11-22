package com.smart.smartchat.Service.mess;

import com.alibaba.fastjson2.JSON;
import com.smart.smartchat.Bean.text;
import com.smart.smartchat.mapper.friendView.checkFriend;
import com.smart.smartchat.mapper.mess.sendMess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.*;

@Service
public class messageImpl implements message
{
    @Autowired
    sendMess sendMess;
    @Autowired
    checkFriend checkFriend;

    @Override
    public Integer addFriends(String nameA, String nameB)
    {
        Integer id = checkFriend.selectId(nameA);
        Integer fid = checkFriend.selectId(nameB);
        if (sendMess.messNumber(id, fid) != 1)
        {
            if (!IsRepeat(nameA, nameB, "请求添加您为好友！", 0, 0))
            {
                return sendMess.insertMess(nameA, nameB, "请求添加您为好友！", 0);
            } else
            {
                return 2;
            }
        } else
        {
            return 3;
        }
    }


    /**
     * 根据参数查找信息,并将信息转换为json字符串
     *
     * @param nameA    发送方
     * @param nameB    接受方
     * @param status   是否阅读
     * @param messType 信息类型
     * @return 将信息以JSON类型返回
     */
    @Override
    public String selectMess(String nameA, String nameB, Integer status, Integer messType)
    {
        List<text> information = sendMess.selectMess(nameA, nameB, status, messType);
        Map<String, text> messMap = new HashMap<>();
        for (int i = 0; i < information.size(); i++)
        {
            messMap.put("sys" + String.valueOf(i), information.get(i));
        }
        String s = JSON.toJSONString(messMap);
        System.out.println(s);
        return s;
    }

    /**
     * 判断某条信息是否重复
     */
    public boolean IsRepeat(String nameA, String nameB, String message, Integer status, Integer messType)
    {
        List<text> messList = sendMess.selectMess(nameA, nameB, status, messType);

        if (messList.size() == 1 && messList.get(0).getMessage().equals(message))
        {
            return true;
        } else return false;
    }

    @Override
    public String chat(String nameA, String nameB, String message)
    {
        //1.存储信息；
        sendMess.insertMess(nameA, nameB, message, 1);
        text text = new text(message);
        text.setNameA(nameA);
        text.setNameB(nameB);
        text.setMessType(1);
        text.setStatus(0);
        text.setTime(new Timestamp(System.currentTimeMillis()));
        Map<String, text> map = new HashMap<>();
        map.put("mess", text);
        String s = JSON.toJSONString(map);
        System.out.println(s);
        return s;
    }

    public String getSysChat(String username)
    {
        List<text> texts0 = sendMess.selectSysMess(username);
        Map<String, text> messMap = new LinkedHashMap<>(
        );
        for (int i = 0; i < texts0.size(); i++)
        {
            messMap.put("allSys" + String.valueOf(i), texts0.get(i));
        }
        System.out.println(JSON.toJSONString(messMap));
        return JSON.toJSONString(messMap);
    }

    public String getChat(String nameA, String nameB)
    {
        List<text> texts0 = sendMess.selectAllMess(nameB, nameA, 1);
//        sendMess.updateMessageStatus(nameB, nameA, 1);
        Map<String, text> messMap = new LinkedHashMap<>(
        );
        for (int i = 0; i < texts0.size(); i++)
        {
            messMap.put("chat" + String.valueOf(i), texts0.get(i));
        }
        System.out.println(JSON.toJSONString(messMap));
        return JSON.toJSONString(messMap);
    }


    /**
     * 查找出未读的消息，然后做判断，是系统信息，还是people消息
     *
     * @param username
     * @return
     */
    public List<String> getUnreadMess(String username)
    {
        Collection<text> newest = getNewest(username);
        System.out.println(newest);
        Map<String, text> map = new HashMap<>();
        Iterator<text> iterator = newest.iterator();
        List<String> unRead = new ArrayList<>();
        while (iterator.hasNext())
        {
            map.put("mess", iterator.next());
            unRead.add(JSON.toJSONString(map));
        }
        //处理系统信息
        List<text> unreadMess = sendMess.unreadMessFor(username, 0, 0);
        if (unreadMess.size() > 0)
        {
            unRead.add(getUnreadSys(unreadMess));
        }
        System.out.println(unRead);
        return unRead;
    }

    public void hadReadFor(String nameA, String nameB)
    {
        sendMess.updateMessageStatus(nameA, nameB, 1);
    }

    /**
     * 查询未读的系统信息
     *
     * @return
     */
    private String getUnreadSys(List<text> unreadMess)
    {
        Map<String, text> map = new HashMap<>();
        map.put("sys0", unreadMess.get(0));
        return JSON.toJSONString(map);
    }

    /**
     * 筛选用户未读的最新消息。
     *
     * @param username
     * @return
     */
    private Collection<text> getNewest(String username)
    {
        List<text> unreadMess = sendMess.unreadMessFor(username, 0, 1);
        Map<String, text> map = new HashMap<>();
        for (int i = 0; i < unreadMess.size(); i++)
        {
            if (unreadMess.get(i).getNameA().equals(username))
            {
                unreadMess.get(i).setNameA(unreadMess.get(i).getNameB());
            }
            if (!map.containsKey(unreadMess.get(i).getNameA()))
            {
                System.out.println(unreadMess.get(i));
                map.put(unreadMess.get(i).getNameA(), unreadMess.get(i));
            }
        }
        return map.values();
    }
}


