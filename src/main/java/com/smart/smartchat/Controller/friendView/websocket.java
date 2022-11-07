package com.smart.smartchat.Controller.friendView;

import com.alibaba.fastjson2.JSON;
import com.smart.smartchat.Bean.user;
import com.smart.smartchat.Service.friendView.friendView;


import com.smart.smartchat.Service.mess.message;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/friends/{username}")
@Component
public class websocket
{
    private static friendView friendView;
    private static message messageS;//关键点2

    @Autowired  //关键点3
    public void setUserService(friendView friendView, message messageS)
    {
        websocket.friendView = friendView;
        websocket.messageS = messageS;
    }

    //创建一个线程安全的map
    private static ConcurrentHashMap<String, websocket> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private static int count;

    private String username;
    private RemoteEndpoint.Basic basicRemote;


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username)
    {
        this.session = session;
        this.username = username;
        System.out.println(username);
        this.basicRemote = session.getBasicRemote();
        webSocketMap.put(username, this);
        count++;
        this.SendMessage(friendView.checkFriend01(this.username));
        this.SendOnline();
    }

    @OnMessage
    public void onMessage(String message)
    {
        Map<String, String> map = (Map) JSON.parse(message);
        if (map.containsKey("search"))
        {
            search(map);
        } else if (map.containsKey("addF"))
        {
            addFriend(map.get("addF"));
        }
    }


    @OnClose
    public void onClose()
    {
        friendView.exit(username);//退出账号，断开连接
        this.SendOnline();
        webSocketMap.remove(username);
    }

    @OnError
    public void onError(Throwable error)
    {
        error.printStackTrace();
    }

    public void SendMessage(String mess)
    {
        System.out.println(mess);
        try
        {
            this.basicRemote.sendText(mess);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //登录或者下线后，将状态发给好友。
    public void SendOnline()
    {
        List<String> names = friendView.checkFri(username);
        for (String name :
                names)
        {
            SendFriend(name, friendView.checkFriend01(name));
        }
    }

    private void search(Map<String, String> map)
    {
        String s;
        user target = friendView.search(map.get("search"));
        if (target != null)
        {
            s = "{\"search\":\"1\",\"status\":\"" + target.getStatus() + "\",\"username\":\"" + target.getUsername() + "\"}";
            System.out.println(s);
        } else
        {
            s = "{\"search\":\"0\"}";
        }
        SendMessage(s);
        System.out.println("fff");
        System.out.println(s);
    }

    private void addFriend(String nameB)
    {
        Integer Number = messageS.addFriends(username, nameB);
        SendMessage("{\"aF\":" + Number + "}");
        if (Number==1){
            SendFriend(nameB, messageS.selectMess(username,nameB,0,0));
        }
    }

    /**
     * 将信息发送给在线的目标对象
     * @param targetName
     * @param message
     */
    private void SendFriend(String targetName, String message)
    {
        websocket websocket = webSocketMap.get(targetName);
        if (websocket != null)
        {
            websocket.SendMessage(message);
        }
    }
}
