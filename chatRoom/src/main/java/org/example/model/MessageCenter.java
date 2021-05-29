package org.example.model;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * Description:保存websocket需要的信息（所有客户端session）
 * User: starry
 * Date: 2021 -05 -27
 * Time: 9:48
 */
public class MessageCenter {

    /**
     * 支持线程安全的map结构，并且满足高并发（读写，读读并发，写写互斥，加锁粒度）
     */
    private static final ConcurrentHashMap<Integer, Session>  clients = new ConcurrentHashMap<>();

    /**
     * 阻塞队列(无边界的队列)
     */
    private static BlockingQueue<String> queue = new LinkedBlockingQueue<>();


    private static volatile MessageCenter center;

    private MessageCenter(){}

    public static MessageCenter getInstance() {
        //单例模式，双重效验锁的单例模式
        if (center == null) {
            synchronized (MessageCenter.class) {
                if (center == null) {
                    center = new MessageCenter();
                    new Thread(() -> {  //启动一个线程，不停地从阻塞队列中拿数据
                        while (true) {
                            try {
                                String message = queue.take();  //阻塞式获取数据，如果队列为空，阻塞等待
                                sendMessage(message);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }
        return center;
    }

    /**
     * 不直接发消息，先把消息存放再队列中，由另一个线程去发
     */
    public void addMessage(String message) {
        queue.add(message);
    }

    /**
     * websocket建立连接时，添加用户id和客户端session，保存起来
     */
    public void addOnlineUser(Integer userId, Session session) {
        clients.put(userId,session);
    }

    /**
     * 关闭websocket连接，和出错时，删除客户端session
     */
    public void delOnlineUser(Integer userId) {
        clients.remove(userId);
    }

    /**
     * 接收到某用户的消息时，转发到所有客户端
     * 存在一个消息转发所有客户端，存在新能问题
     * 如果接收到的信息数量m很多，同时在线的用户数量n也很多，
     * 那么要转发的次数就是m*n次，每个接收消息都是一个线程，
     * 都要等待websocket中的onmessage回调方法执行完，性能差
     * TODO：优化（使用阻塞队列的方式解决：并行并发的执行任务提交和执行任务）
     */
    public static void sendMessage(String message) {
        try {
            //遍历map的所有数据，然后转发到所有session对象中
            Enumeration<Session> e = clients.elements();
            while (e.hasMoreElements()) {
                Session session = e.nextElement();
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
