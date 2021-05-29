package org.example.servlet;

import org.example.dao.MessageDao;
import org.example.dao.UserDao;
import org.example.model.Message;
import org.example.model.MessageCenter;
import org.example.util.Util;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:服务器websocket
 * User: starry
 * Date: 2021 -05 -27
 * Time: 8:54
 */

@ServerEndpoint("/message/{userId}")
public class MessageWebsocket {

    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId, Session session) throws IOException {
        // 1.把每个客户端的session都保存起来，之后转发消息到所有客户端要用
//        MessageCenter.addOnlineUser(userId,session);
        MessageCenter.getInstance().addOnlineUser(userId,session);
        // 2.查询本客户端（用户）上次登录前的消息（数据库查）
        List<Message> list = MessageDao.queryByLastLogout(userId);
        // 3.发送当前用户在上次登录后的消息
        for (Message m : list) {
            session.getBasicRemote().sendText(Util.serialize(m));
        }
        System.out.println("建立连接" + userId);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // 1.遍历保存的所有session，每个都发送消息
//        MessageCenter.sendMessage(message);
        MessageCenter.getInstance().addMessage(message);
        // 2.消息还要保存在数据库，
        // （1）反序列化json字符串为message对象
        Message msg = Util.deserialize(message, Message.class);
        // （2）message设置接收消息的时间
//        msg.setSendTime(new Date());
        // （3）插入数据库
        int n = MessageDao.insert(msg);

        System.out.println("接收到的消息：" + message);
    }

    @OnClose
    public void onClose(@PathParam("userId") Integer userId) {
        //1.本客户端关闭连接，要在之前保存的session集合中，删除
//        MessageCenter.delOnlineUser(userId);
        MessageCenter.getInstance().delOnlineUser(userId);
        //2.建立连接要获取用户上次登录以后的消息，所以关闭长连接就是代表用户退出
        //更新用户的上次登录时间
        int n = UserDao.updateLastLogout(userId);
        System.out.println("关闭连接");
    }

    @OnError
    public void onError(@PathParam("userId") Integer userId, Throwable throwable) {
        System.out.println("出错了");
        //和关闭连接的操作一样
//        MessageCenter.delOnlineUser(userId);
        MessageCenter.getInstance().delOnlineUser(userId);
        throwable.printStackTrace();
    }

}
