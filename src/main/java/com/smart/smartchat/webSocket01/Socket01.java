//package com.smart.smartchat.webSocket01;
//
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//import org.springframework.stereotype.Component;
//
//
//
///**
// * @author zhengkai.blog.csdn.net
// */
//@ServerEndpoint("/imserver/{userId}")
//@Component
//public class Socket01
//{
//
//
//    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
//    private static int onlineCount = 0;
//    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
//    private static ConcurrentHashMap<String, Socket01> webSocketMap = new ConcurrentHashMap<>();
//    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
//    private Session session;
//    /**接收userId*/
//    private String userId="";
//
//    /**
//     * 连接建立成功调用的方法*/
//    @OnOpen
//    public void onOpen(Session session,@PathParam("userId") String userId) {
//        this.session = session;
//        this.userId=userId;
//        if(webSocketMap.containsKey(userId)){
//            webSocketMap.remove(userId);//更新操作
//            webSocketMap.put(userId,this);
//            //加入set中
//        }else{
//            webSocketMap.put(userId,this);
//            //加入set中
//            addOnlineCount();
//            //在线数加1
//        }
//
//
//
//        try {
//            sendMessage("连接成功");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        if(webSocketMap.containsKey(userId)){
//            webSocketMap.remove(userId);
//            //从set中删除
//            subOnlineCount();
//        }
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param news 客户端发送过来的消息*/
//    @OnMessage
//    public void onMessage(String news) {
//        System.out.println(news);
//    }
//
//    /**
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//
//        error.printStackTrace();
//    }
//    /**
//     * 实现服务器主动推送
//     */
//    public void sendMessage(String news) throws IOException {
//        this.session.getBasicRemote().sendText(news);
//    }
//
//
//    /**
//     * 发送自定义消息
//     * */
//
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        Socket01.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        Socket01.onlineCount--;
//    }
//}
