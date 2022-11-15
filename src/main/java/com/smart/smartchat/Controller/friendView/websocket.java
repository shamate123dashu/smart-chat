package com.smart.smartchat.Controller.friendView;

import com.alibaba.fastjson2.JSON;
import com.smart.smartchat.Bean.user;
import com.smart.smartchat.Service.friendView.friendView;


import com.smart.smartchat.Service.mess.message;
import com.smart.smartchat.config.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;

import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//configurator= WebSocketConfig.class告诉websocket哪个是它的配置类。然后他才将配置类中的
@ServerEndpoint(value = "/friends",configurator= WebSocketConfig.class)
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
    public void onOpen(Session session,EndpointConfig config)
    {
        this.session = session;
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.username =(String)httpSession.getAttribute("username");
        this.basicRemote = session.getBasicRemote();
        webSocketMap.put(username, this);
        count++;
        //发送自身好友在线情况
        this.SendMessage(friendView.checkFriend01(this.username));
        this.SendOnline();
    }

    @OnMessage
    public void onMessage(String message)
    {
        Map<String, String> map = (Map) JSON.parse(message);
        if (map.containsKey("mess"))
        {
            newMess(map);
        }else if (map.containsKey("GetChat")){
            getChat(map.get("GetChat"));
        }
        else if (map.containsKey("search"))
        {
            search(map);
        } else if (map.containsKey("addF"))
        {
            addFriend(map.get("addF"));
        } else if (map.containsKey("relation"))
        {
            addRelation(map);
        }
    }

    private void newMess(Map<String, String> map)
    {
        String chat = messageS.chat(username, map.get("nameA"), map.get("mess"));
        SendFriend( map.get("nameA"),chat);
    }

    private void getChat(String nameB)
    {
        SendMessage(messageS.getChat(username, nameB));
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
        //查找在线的好友
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
        } else
        {
            s = "{\"search\":\"0\"}";
        }
        SendMessage(s);
    }

    private void addFriend(String nameB)
    {
        Integer Number = messageS.addFriends(username, nameB);
        SendMessage("{\"aF\":" + Number + "}");
        if (Number == 1)
        {
            SendFriend(nameB, messageS.selectMess(username, nameB, 0, 0));
        }
    }

    /**
     * 将信息发送给在线的目标对象
     *
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

    /**
     * 根据是否同意添加好友，来添加好友关系。
     *
     * @param map
     */
    private void addRelation(Map<String,String> map)
    {
        Integer relation = Integer.parseInt(map.get("relation"));
        System.out.println(map.get("nameA"));
        friendView.addRelation(map.get("nameA"),username, relation);
        if (relation!=-1){
            System.out.println(relation);
            SendFriend(map.get("nameA"), friendView.checkFriend01(map.get("nameA")));
            this.SendMessage(friendView.checkFriend01(this.username));
        }
    }
}
